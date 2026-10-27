package io.github.mudrichenkoevgeny.kmp.core.storage.repository.settings

import co.touchlab.kermit.Logger
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.core.storage.network.api.storagesettings.OpenStorageSettingsApi
import io.github.mudrichenkoevgeny.kmp.core.storage.storage.storagesettings.OpenStorageSettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.OpenStorageSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.mapper.storagesettings.toOpenStorageSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.contract.StorageWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.model.storagesettings.OpenStorageSettingsPayload
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
 * Default [OpenStorageSettingsRepository]: mutex-guarded in-memory state, encrypted persistence, REST
 * refresh via [OpenStorageSettingsApi], and subscription to [WebSocketService] events of type
 * `OPEN_STORAGE_SETTINGS_UPDATED` (see foundation contract).
 *
 * @param openStorageSettingsApi Network access for fetching settings.
 * @param openStorageSettingsStorage Encrypted backing store.
 * @param webSocketService Source of push events; non-matching frame types are ignored.
 * @param repositoryScope Coroutine scope used to preload cache and collect socket events.
 */
class OpenStorageSettingsRepositoryImpl(
    private val openStorageSettingsApi: OpenStorageSettingsApi,
    private val openStorageSettingsStorage: OpenStorageSettingsStorage,
    private val webSocketService: WebSocketService,
    repositoryScope: CoroutineScope
) : OpenStorageSettingsRepository {

    private val updateMutex = Mutex()
    private val _settings = MutableStateFlow<OpenStorageSettings?>(null)

    init {
        repositoryScope.launch {
            _settings.value = openStorageSettingsStorage.getOpenStorageSettings()

            webSocketService.observeEvents()
                .filter { it.type == StorageWebSocketEventTypes.OPEN_STORAGE_SETTINGS_UPDATED }
                .collect { frame ->
                    try {
                        val response = frame.payload?.let {
                            FoundationJson.decodeFromJsonElement<OpenStorageSettingsPayload>(it)
                        }

                        if (response != null) {
                            updateOpenStorageSettings(response.toOpenStorageSettings())
                        } else {
                            Logger.w { "Received OPEN_STORAGE_SETTINGS_UPDATED with invalid payload" }
                        }
                    } catch (e: Exception) {
                        Logger.e(e) { "Failed to process WebSocket event" }
                    }
                }
        }
    }

    override suspend fun getOpenStorageSettings(): AppResult<OpenStorageSettings> {
        _settings.value?.let { return AppResult.Success(it) }

        return updateMutex.withLock {
            val cached = _settings.value ?: openStorageSettingsStorage.getOpenStorageSettings()

            if (cached != null) {
                _settings.value = cached
                AppResult.Success(cached)
            } else {
                refreshOpenStorageSettingsInternal()
            }
        }
    }

    override suspend fun refreshOpenStorageSettings(): AppResult<OpenStorageSettings> {
        return updateMutex.withLock {
            refreshOpenStorageSettingsInternal()
        }
    }

    override suspend fun updateOpenStorageSettings(storageSettings: OpenStorageSettings) {
        updateMutex.withLock {
            applySettingsUpdate(storageSettings)
        }
    }

    override fun observeOpenStorageSettings(): Flow<OpenStorageSettings?> = _settings.asStateFlow()

    private suspend fun refreshOpenStorageSettingsInternal(): AppResult<OpenStorageSettings> {
        return openStorageSettingsApi.getStorageSettings()
            .mapSuccess { response ->
                val settings = response.toOpenStorageSettings()
                applySettingsUpdate(settings)
                settings
            }
    }

    private suspend fun applySettingsUpdate(storageSettings: OpenStorageSettings) {
        openStorageSettingsStorage.updateOpenStorageSettings(storageSettings)
        _settings.value = storageSettings
    }
}
