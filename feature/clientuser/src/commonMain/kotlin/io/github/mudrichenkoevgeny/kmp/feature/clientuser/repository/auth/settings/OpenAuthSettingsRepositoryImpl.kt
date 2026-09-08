package io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.auth.settings

import co.touchlab.kermit.Logger
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.auth.settings.OpenAuthSettingsApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings.OpenAuthSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.settings.OpenAuthSettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.OpenAuthSettings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.auth.settings.toOpenAuthSettings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.contract.UserWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.settings.OpenAuthSettingsPayload
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
 * Implements [OpenAuthSettingsRepository] with a mutex-guarded in-memory [MutableStateFlow], persistence
 * via [OpenAuthSettingsStorage], HTTP via [OpenAuthSettingsApi], and live updates from [WebSocketService] for
 * `OPEN_AUTH_SETTINGS_UPDATED` events.
 *
 * On construction, loads cached settings from storage into the flow and subscribes to WebSocket
 * frames in [repositoryScope]; invalid payloads are logged and ignored.
 *
 * @param openAuthSettingsApi Remote read endpoint for auth settings.
 * @param openAuthSettingsStorage Encrypted or local persistence for settings snapshots.
 * @param webSocketService Source of push updates for auth settings changes.
 * @param repositoryScope Long-lived scope used for the WebSocket collector started in `init`.
 */
class OpenAuthSettingsRepositoryImpl(
    private val openAuthSettingsApi: OpenAuthSettingsApi,
    private val openAuthSettingsStorage: OpenAuthSettingsStorage,
    private val webSocketService: WebSocketService,
    repositoryScope: CoroutineScope
) : OpenAuthSettingsRepository {

    private val updateMutex = Mutex()
    private val _settings = MutableStateFlow<OpenAuthSettings?>(null)

    init {
        repositoryScope.launch {
            _settings.value = openAuthSettingsStorage.getOpenAuthSettings()

            webSocketService.observeEvents()
                .filter { it.type == UserWebSocketEventTypes.OPEN_AUTH_SETTINGS_UPDATED }
                .collect { frame ->
                    try {
                        val response = frame.payload?.let {
                            FoundationJson.decodeFromJsonElement<OpenAuthSettingsPayload>(it)
                        }

                        if (response != null) {
                            updateOpenAuthSettings(response.toOpenAuthSettings())
                        } else {
                            Logger.w { "Received OPEN_AUTH_SETTINGS_UPDATED with invalid payload" }
                        }
                    } catch (e: Exception) {
                        Logger.e(e) { "Failed to process WebSocket event" }
                    }
                }
        }
    }

    override suspend fun getOpenAuthSettings(): AppResult<OpenAuthSettings> {
        _settings.value?.let { return AppResult.Success(it) }

        return updateMutex.withLock {
            val cached = _settings.value ?: openAuthSettingsStorage.getOpenAuthSettings()

            if (cached != null) {
                _settings.value = cached
                AppResult.Success(cached)
            } else {
                refreshOpenAuthSettingsInternal()
            }
        }
    }

    override suspend fun refreshOpenAuthSettings(): AppResult<OpenAuthSettings> {
        return updateMutex.withLock {
            refreshOpenAuthSettingsInternal()
        }
    }

    override suspend fun updateOpenAuthSettings(authSettings: OpenAuthSettings) {
        updateMutex.withLock {
            applySettingsUpdate(authSettings)
        }
    }

    override fun observeOpenAuthSettings(): Flow<OpenAuthSettings?> = _settings.asStateFlow()

    private suspend fun refreshOpenAuthSettingsInternal(): AppResult<OpenAuthSettings> {
        return openAuthSettingsApi.getAuthSettings()
            .mapSuccess { response ->
                val settings = response.toOpenAuthSettings()
                applySettingsUpdate(settings)
                settings
            }
    }

    private suspend fun applySettingsUpdate(authSettings: OpenAuthSettings) {
        openAuthSettingsStorage.updateOpenAuthSettings(authSettings)
        _settings.value = authSettings
    }
}
