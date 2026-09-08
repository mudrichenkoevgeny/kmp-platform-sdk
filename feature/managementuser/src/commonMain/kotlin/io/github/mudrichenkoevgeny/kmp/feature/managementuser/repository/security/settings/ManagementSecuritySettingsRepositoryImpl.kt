package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.security.settings

import co.touchlab.kermit.Logger
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.security.settings.ManagementSecuritySettingsApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.storage.security.settings.ManagementSecuritySettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.ManagementSecuritySettings
import io.github.mudrichenkoevgeny.shared.foundation.core.security.mapper.securitysettings.toManagementSecuritySettings
import io.github.mudrichenkoevgeny.shared.foundation.core.security.mapper.securitysettings.toManagementSecuritySettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.contract.SecurityWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.securitysettings.ManagementSecuritySettingsPayload
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
 * Implementation of [ManagementSecuritySettingsRepository] backing in-memory flow, [ManagementSecuritySettingsStorage],
 * REST updates via [ManagementSecuritySettingsApi], and WebSocket events for live changes.
 */
class ManagementSecuritySettingsRepositoryImpl(
    private val managementSecuritySettingsApi: ManagementSecuritySettingsApi,
    private val managementSecuritySettingsStorage: ManagementSecuritySettingsStorage,
    private val webSocketService: WebSocketService,
    repositoryScope: CoroutineScope
) : ManagementSecuritySettingsRepository {

    private val updateMutex = Mutex()
    private val _settings = MutableStateFlow<ManagementSecuritySettings?>(null)

    init {
        repositoryScope.launch {
            _settings.value = managementSecuritySettingsStorage.getManagementSecuritySettings()

            webSocketService.observeEvents()
                .filter { it.type == SecurityWebSocketEventTypes.MANAGEMENT_SECURITY_SETTINGS_UPDATED }
                .collect { frame ->
                    try {
                        val response = frame.payload?.let {
                            FoundationJson.decodeFromJsonElement<ManagementSecuritySettingsPayload>(it)
                        }

                        if (response != null) {
                            updateManagementSecuritySettings(response.toManagementSecuritySettings())
                        } else {
                            Logger.w { "Received MANAGEMENT_SECURITY_SETTINGS_UPDATED with invalid payload" }
                        }
                    } catch (e: Exception) {
                        Logger.e(e) { "Failed to process WebSocket event" }
                    }
                }
        }
    }

    override suspend fun getManagementSecuritySettings(): AppResult<ManagementSecuritySettings> {
        _settings.value?.let { return AppResult.Success(it) }

        return updateMutex.withLock {
            val cached = _settings.value ?: managementSecuritySettingsStorage.getManagementSecuritySettings()

            if (cached != null) {
                _settings.value = cached
                AppResult.Success(cached)
            } else {
                refreshManagementSecuritySettingsInternal()
            }
        }
    }

    override suspend fun saveRemoteManagementSecuritySettings(securitySettings: ManagementSecuritySettings): AppResult<Unit> {
        val payload = securitySettings.toManagementSecuritySettingsPayload()
        return managementSecuritySettingsApi.updateManagementSecuritySettings(payload)
            .mapSuccess {
                updateManagementSecuritySettings(securitySettings)
            }
    }

    override suspend fun refreshManagementSecuritySettings(): AppResult<ManagementSecuritySettings> {
        return updateMutex.withLock {
            refreshManagementSecuritySettingsInternal()
        }
    }

    override suspend fun updateManagementSecuritySettings(securitySettings: ManagementSecuritySettings) {
        updateMutex.withLock {
            applySettingsUpdate(securitySettings)
        }
    }

    override fun observeManagementSecuritySettings(): Flow<ManagementSecuritySettings?> = _settings.asStateFlow()

    private suspend fun refreshManagementSecuritySettingsInternal(): AppResult<ManagementSecuritySettings> {
        return managementSecuritySettingsApi.getManagementSecuritySettings()
            .mapSuccess { response ->
                val settings = response.toManagementSecuritySettings()
                applySettingsUpdate(settings)
                settings
            }
    }

    private suspend fun applySettingsUpdate(securitySettings: ManagementSecuritySettings) {
        managementSecuritySettingsStorage.updateManagementSecuritySettings(securitySettings)
        _settings.value = securitySettings
    }
}
