package io.github.mudrichenkoevgeny.kmp.core.settings.repository.globalsettings

import co.touchlab.kermit.Logger
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.core.settings.mapper.globalsettings.toGlobalSettings
import io.github.mudrichenkoevgeny.kmp.core.settings.model.globalsettings.GlobalSettings
import io.github.mudrichenkoevgeny.kmp.core.settings.network.api.globalsettings.GlobalSettingsApi
import io.github.mudrichenkoevgeny.kmp.core.settings.storage.globalsettings.GlobalSettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.contract.SettingsWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.response.GlobalSettingsResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * Default [GlobalSettingsRepository]: mutex-guarded in-memory state, encrypted persistence, REST
 * refresh via [GlobalSettingsApi], and subscription to [WebSocketService] events of type
 * `GLOBAL_SETTINGS_UPDATED` (see foundation contract).
 *
 * @param globalSettingsApi Network access for fetching settings.
 * @param globalSettingsStorage Encrypted backing store.
 * @param webSocketService Source of push events; non-matching frame types are ignored.
 * @param repositoryScope Coroutine scope used to preload cache and collect socket events.
 */
class GlobalSettingsRepositoryImpl(
    private val globalSettingsApi: GlobalSettingsApi,
    private val globalSettingsStorage: GlobalSettingsStorage,
    private val webSocketService: WebSocketService,
    repositoryScope: CoroutineScope
) : GlobalSettingsRepository {

    private val updateMutex = Mutex()
    private val _settings = MutableStateFlow<GlobalSettings?>(null)

    init {
        repositoryScope.launch {
            _settings.value = globalSettingsStorage.getGlobalSettings()

            webSocketService.observeEvents()
                .filter { it.type == SettingsWebSocketEventTypes.GLOBAL_SETTINGS_UPDATED }
                .collect { frame ->
                    try {
                        val response = frame.payload?.let {
                            FoundationJson.decodeFromJsonElement<GlobalSettingsResponse>(it)
                        }

                        if (response != null) {
                            updateGlobalSettings(response.toGlobalSettings())
                        } else {
                            Logger.w { "Received GLOBAL_SETTINGS_UPDATED with invalid payload" }
                        }
                    } catch (e: Exception) {
                        Logger.e(e) { "Failed to process WebSocket event" }
                    }
                }
        }
    }

    override suspend fun getGlobalSettings(): AppResult<GlobalSettings> {
        _settings.value?.let { return AppResult.Success(it) }

        return updateMutex.withLock {
            val cached = _settings.value ?: globalSettingsStorage.getGlobalSettings()

            if (cached != null) {
                _settings.value = cached
                AppResult.Success(cached)
            } else {
                refreshGlobalSettingsInternal()
            }
        }
    }

    override suspend fun refreshGlobalSettings(): AppResult<GlobalSettings> {
        return updateMutex.withLock {
            refreshGlobalSettingsInternal()
        }
    }

    override suspend fun updateGlobalSettings(globalSettings: GlobalSettings) {
        updateMutex.withLock {
            applySettingsUpdate(globalSettings)
        }
    }

    override fun observeGlobalSettings(): Flow<GlobalSettings?> = _settings.asStateFlow()

    private suspend fun refreshGlobalSettingsInternal(): AppResult<GlobalSettings> {
        return globalSettingsApi.getGlobalSettings()
            .mapSuccess { response ->
                val settings = response.toGlobalSettings()
                applySettingsUpdate(settings)
                settings
            }
    }

    private suspend fun applySettingsUpdate(globalSettings: GlobalSettings) {
        globalSettingsStorage.updateGlobalSettings(globalSettings)
        _settings.value = globalSettings
    }
}