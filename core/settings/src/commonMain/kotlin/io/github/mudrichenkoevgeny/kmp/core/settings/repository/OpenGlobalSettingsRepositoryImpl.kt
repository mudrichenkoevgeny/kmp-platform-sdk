package io.github.mudrichenkoevgeny.kmp.core.settings.repository

import co.touchlab.kermit.Logger
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.core.settings.network.globalsettings.OpenGlobalSettingsApi
import io.github.mudrichenkoevgeny.kmp.core.settings.storage.globalsettings.OpenGlobalSettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.OpenGlobalSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.mapper.globalsettings.toOpenGlobalSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.contract.SettingsWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.model.globalsettings.OpenGlobalSettingsPayload
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * Default [OpenGlobalSettingsRepository]: mutex-guarded in-memory state, encrypted persistence, REST
 * refresh via [OpenGlobalSettingsApi], and subscription to [WebSocketService] events of type
 * `OPEN_GLOBAL_SETTINGS_UPDATED` (see foundation contract).
 *
 * @param openGlobalSettingsApi Network access for fetching settings.
 * @param openGlobalSettingsStorage Encrypted backing store.
 * @param webSocketService Source of push events; non-matching frame types are ignored.
 * @param repositoryScope Coroutine scope used to preload cache and collect socket events.
 */
public class OpenGlobalSettingsRepositoryImpl(
    private val openGlobalSettingsApi: OpenGlobalSettingsApi,
    private val openGlobalSettingsStorage: OpenGlobalSettingsStorage,
    private val webSocketService: WebSocketService,
    repositoryScope: CoroutineScope
) : OpenGlobalSettingsRepository {

    private val updateMutex: Mutex = Mutex()
    private val settingsStateFlow: MutableStateFlow<OpenGlobalSettings?> = MutableStateFlow(null)

    init {
        repositoryScope.launch {
            settingsStateFlow.value = openGlobalSettingsStorage.getOpenGlobalSettings()

            webSocketService.observeEvents()
                .filter { it.type == SettingsWebSocketEventTypes.OPEN_GLOBAL_SETTINGS_UPDATED }
                .collect { frame ->
                    try {
                        val response = frame.payload?.let {
                            FoundationJson.decodeFromJsonElement<OpenGlobalSettingsPayload>(it)
                        }

                        if (response != null) {
                            updateOpenGlobalSettings(response.toOpenGlobalSettings())
                        } else {
                            Logger.w { "Received OPEN_GLOBAL_SETTINGS_UPDATED with invalid payload" }
                        }
                    } catch (e: SerializationException) {
                        Logger.e(e) { "Failed to process WebSocket event" }
                    } catch (e: IllegalArgumentException) {
                        Logger.e(e) { "Failed to process WebSocket event" }
                    } catch (e: IllegalStateException) {
                        Logger.e(e) { "Failed to process WebSocket event" }
                    }
                }
        }
    }

    public override suspend fun getOpenGlobalSettings(): AppResult<OpenGlobalSettings> {
        settingsStateFlow.value?.let { return AppResult.Success(it) }

        return updateMutex.withLock {
            val cached = settingsStateFlow.value ?: openGlobalSettingsStorage.getOpenGlobalSettings()

            if (cached != null) {
                settingsStateFlow.value = cached
                AppResult.Success(cached)
            } else {
                refreshOpenGlobalSettingsInternal()
            }
        }
    }

    public override suspend fun refreshOpenGlobalSettings(): AppResult<OpenGlobalSettings> = updateMutex.withLock {
        refreshOpenGlobalSettingsInternal()
    }

    public override suspend fun updateOpenGlobalSettings(globalSettings: OpenGlobalSettings) {
        updateMutex.withLock {
            applySettingsUpdate(globalSettings)
        }
    }

    public override fun observeOpenGlobalSettings(): Flow<OpenGlobalSettings?> = settingsStateFlow.asStateFlow()

    private suspend fun refreshOpenGlobalSettingsInternal(): AppResult<OpenGlobalSettings> =
        openGlobalSettingsApi.getOpenGlobalSettings()
            .mapSuccess { response ->
                val settings = response.toOpenGlobalSettings()
                applySettingsUpdate(settings)
                settings
            }

    private suspend fun applySettingsUpdate(globalSettings: OpenGlobalSettings) {
        openGlobalSettingsStorage.updateOpenGlobalSettings(globalSettings)
        settingsStateFlow.value = globalSettings
    }
}
