package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.globalsettings

import co.touchlab.kermit.Logger
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.globalsettings.ManagementGlobalSettingsApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.storage.globalsettings.ManagementGlobalSettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.ManagementGlobalSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.mapper.globalsettings.toManagementGlobalSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.mapper.globalsettings.toManagementGlobalSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.contract.SettingsWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.model.globalsettings.ManagementGlobalSettingsPayload
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
 * Implementation of [ManagementGlobalSettingsRepository] backing in-memory flow, [ManagementGlobalSettingsStorage],
 * REST updates via [ManagementGlobalSettingsApi], and WebSocket events for live changes.
 */
class ManagementGlobalSettingsRepositoryImpl(
    private val managementGlobalSettingsApi: ManagementGlobalSettingsApi,
    private val managementGlobalSettingsStorage: ManagementGlobalSettingsStorage,
    private val webSocketService: WebSocketService,
    repositoryScope: CoroutineScope
) : ManagementGlobalSettingsRepository {

    private val updateMutex = Mutex()
    private val _settings = MutableStateFlow<ManagementGlobalSettings?>(null)

    init {
        repositoryScope.launch {
            _settings.value = managementGlobalSettingsStorage.getManagementGlobalSettings()

            webSocketService.observeEvents()
                .filter { it.type == SettingsWebSocketEventTypes.MANAGEMENT_GLOBAL_SETTINGS_UPDATED }
                .collect { frame ->
                    try {
                        val response = frame.payload?.let {
                            FoundationJson.decodeFromJsonElement<ManagementGlobalSettingsPayload>(it)
                        }

                        if (response != null) {
                            updateManagementGlobalSettings(response.toManagementGlobalSettings())
                        } else {
                            Logger.w { "Received MANAGEMENT_GLOBAL_SETTINGS_UPDATED with invalid payload" }
                        }
                    } catch (e: Exception) {
                        Logger.e(e) { "Failed to process WebSocket event" }
                    }
                }
        }
    }

    override suspend fun getManagementGlobalSettings(): AppResult<ManagementGlobalSettings> {
        _settings.value?.let { return AppResult.Success(it) }

        return updateMutex.withLock {
            val cached = _settings.value ?: managementGlobalSettingsStorage.getManagementGlobalSettings()

            if (cached != null) {
                _settings.value = cached
                AppResult.Success(cached)
            } else {
                refreshManagementGlobalSettingsInternal()
            }
        }
    }

    override suspend fun saveRemoteManagementGlobalSettings(globalSettings: ManagementGlobalSettings): AppResult<Unit> {
        val payload = globalSettings.toManagementGlobalSettingsPayload()
        return managementGlobalSettingsApi.updateManagementGlobalSettings(payload)
            .mapSuccess {
                updateManagementGlobalSettings(globalSettings)
            }
    }

    override suspend fun refreshManagementGlobalSettings(): AppResult<ManagementGlobalSettings> {
        return updateMutex.withLock {
            refreshManagementGlobalSettingsInternal()
        }
    }

    override suspend fun updateManagementGlobalSettings(globalSettings: ManagementGlobalSettings) {
        updateMutex.withLock {
            applySettingsUpdate(globalSettings)
        }
    }

    override fun observeManagementGlobalSettings(): Flow<ManagementGlobalSettings?> = _settings.asStateFlow()

    private suspend fun refreshManagementGlobalSettingsInternal(): AppResult<ManagementGlobalSettings> {
        return managementGlobalSettingsApi.getManagementGlobalSettings()
            .mapSuccess { response ->
                val settings = response.toManagementGlobalSettings()
                applySettingsUpdate(settings)
                settings
            }
    }

    private suspend fun applySettingsUpdate(globalSettings: ManagementGlobalSettings) {
        managementGlobalSettingsStorage.updateManagementGlobalSettings(globalSettings)
        _settings.value = globalSettings
    }
}
