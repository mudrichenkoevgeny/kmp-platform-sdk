package io.github.mudrichenkoevgeny.kmp.core.security.repository

import co.touchlab.kermit.Logger
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.core.security.network.securitysettings.OpenSecuritySettingsApi
import io.github.mudrichenkoevgeny.kmp.core.security.storage.securitysettings.OpenSecuritySettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.OpenSecuritySettings
import io.github.mudrichenkoevgeny.shared.foundation.core.security.mapper.securitysettings.toOpenSecuritySettings
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.contract.SecurityWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.securitysettings.OpenSecuritySettingsPayload
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
 * Default [OpenSecuritySettingsRepository]: mutex-guarded in-memory state, encrypted persistence, REST
 * refresh via [OpenSecuritySettingsApi], and subscription to [WebSocketService] events of type
 * `OPEN_SECURITY_SETTINGS_UPDATED` (see foundation contract).
 *
 * @param openSecuritySettingsApi Network access for fetching settings.
 * @param openSecuritySettingsStorage Encrypted backing store.
 * @param webSocketService Source of push events; non-matching frame types are ignored.
 * @param repositoryScope Coroutine scope used to preload cache and collect socket events.
 */
class OpenSecuritySettingsRepositoryImpl(
    private val openSecuritySettingsApi: OpenSecuritySettingsApi,
    private val openSecuritySettingsStorage: OpenSecuritySettingsStorage,
    private val webSocketService: WebSocketService,
    repositoryScope: CoroutineScope
) : OpenSecuritySettingsRepository {

    private val updateMutex = Mutex()
    private val _settings = MutableStateFlow<OpenSecuritySettings?>(null)

    init {
        repositoryScope.launch {
            _settings.value = openSecuritySettingsStorage.getOpenSecuritySettings()

            webSocketService.observeEvents()
                .filter { it.type == SecurityWebSocketEventTypes.OPEN_SECURITY_SETTINGS_UPDATED }
                .collect { frame ->
                    try {
                        val response = frame.payload?.let {
                            FoundationJson.decodeFromJsonElement<OpenSecuritySettingsPayload>(it)
                        }

                        if (response != null) {
                            updateOpenSecuritySettings(response.toOpenSecuritySettings())
                        } else {
                            Logger.w { "Received OPEN_SECURITY_SETTINGS_UPDATED with invalid payload" }
                        }
                    } catch (e: Exception) {
                        Logger.e(e) { "Failed to process WebSocket event" }
                    }
                }
        }
    }

    override suspend fun getOpenSecuritySettings(): AppResult<OpenSecuritySettings> {
        _settings.value?.let { return AppResult.Success(it) }

        return updateMutex.withLock {
            val cached = _settings.value ?: openSecuritySettingsStorage.getOpenSecuritySettings()

            if (cached != null) {
                _settings.value = cached
                AppResult.Success(cached)
            } else {
                refreshOpenSecuritySettingsInternal()
            }
        }
    }

    override suspend fun refreshOpenSecuritySettings(): AppResult<OpenSecuritySettings> {
        return updateMutex.withLock {
            refreshOpenSecuritySettingsInternal()
        }
    }

    override suspend fun updateOpenSecuritySettings(securitySettings: OpenSecuritySettings) {
        updateMutex.withLock {
            applySettingsUpdate(securitySettings)
        }
    }

    override fun observeOpenSecuritySettings(): Flow<OpenSecuritySettings?> = _settings.asStateFlow()

    private suspend fun refreshOpenSecuritySettingsInternal(): AppResult<OpenSecuritySettings> {
        return openSecuritySettingsApi.getSecuritySettings()
            .mapSuccess { response ->
                val settings = response.toOpenSecuritySettings()
                applySettingsUpdate(settings)
                settings
            }
    }

    private suspend fun applySettingsUpdate(securitySettings: OpenSecuritySettings) {
        openSecuritySettingsStorage.updateOpenSecuritySettings(securitySettings)
        _settings.value = securitySettings
    }
}
