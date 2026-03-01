package io.github.mudrichenkoevgeny.kmp.core.security.repository.securitysettings

import io.github.mudrichenkoevgeny.kmp.core.security.model.securitysettings.SecuritySettings
import io.github.mudrichenkoevgeny.kmp.core.security.network.api.securitysettings.SecuritySettingsApi
import io.github.mudrichenkoevgeny.kmp.core.security.storage.securitysettings.SecuritySettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import co.touchlab.kermit.Logger
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.core.security.mapper.securitysettings.toSecuritySettings
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.contract.SecurityWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.response.settings.SecuritySettingsResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.decodeFromJsonElement

class SecuritySettingsRepositoryImpl(
    private val securitySettingsApi: SecuritySettingsApi,
    private val securitySettingsStorage: SecuritySettingsStorage,
    private val webSocketService: WebSocketService,
    repositoryScope: CoroutineScope
) : SecuritySettingsRepository {

    private val updateMutex = Mutex()
    private val _settings = MutableStateFlow<SecuritySettings?>(null)

    init {
        repositoryScope.launch {
            _settings.value = securitySettingsStorage.getSecuritySettings()

            webSocketService.observeEvents()
                .filter { it.type == SecurityWebSocketEventTypes.SECURITY_SETTINGS_UPDATED }
                .collect { frame ->
                    try {
                        val response = frame.payload?.let {
                            FoundationJson.decodeFromJsonElement<SecuritySettingsResponse>(it)
                        }

                        if (response != null) {
                            updateSecuritySettings(response.toSecuritySettings())
                        } else {
                            Logger.w { "Received SECURITY_SETTINGS_UPDATED with invalid payload" }
                        }
                    } catch (e: Exception) {
                        Logger.e(e) { "Failed to process WebSocket event" }
                    }
                }
        }
    }

    override suspend fun getSecuritySettings(): AppResult<SecuritySettings> {
        _settings.value?.let { return AppResult.Success(it) }

        return updateMutex.withLock {
            val cached = _settings.value ?: securitySettingsStorage.getSecuritySettings()

            if (cached != null) {
                _settings.value = cached
                AppResult.Success(cached)
            } else {
                refreshSecuritySettingsInternal()
            }
        }
    }

    override suspend fun refreshSecuritySettings(): AppResult<SecuritySettings> {
        return updateMutex.withLock {
            refreshSecuritySettingsInternal()
        }
    }

    override suspend fun updateSecuritySettings(securitySettings: SecuritySettings) {
        updateMutex.withLock {
            applySettingsUpdate(securitySettings)
        }
    }

    override fun observeSecuritySettings(): Flow<SecuritySettings?> = _settings.asStateFlow()

    private suspend fun refreshSecuritySettingsInternal(): AppResult<SecuritySettings> {
        return securitySettingsApi.getSecuritySettings()
            .mapSuccess { response ->
                val settings = response.toSecuritySettings()
                applySettingsUpdate(settings)
                settings
            }
    }

    private suspend fun applySettingsUpdate(securitySettings: SecuritySettings) {
        securitySettingsStorage.updateSecuritySettings(securitySettings)
        _settings.value = securitySettings
    }
}