package io.github.mudrichenkoevgeny.kmp.core.storage.repository.management

import co.touchlab.kermit.Logger
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.core.storage.network.api.management.ManagementStorageSettingsApi
import io.github.mudrichenkoevgeny.kmp.core.storage.storage.management.ManagementStorageSettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.ManagementStorageSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.mapper.storagesettings.toManagementStorageSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.mapper.storagesettings.toManagementStorageSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.contract.StorageWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.model.storagesettings.ManagementStorageSettingsPayload
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
 * Implementation of [ManagementStorageSettingsRepository] backing in-memory flow, [ManagementStorageSettingsStorage],
 * REST updates via [ManagementStorageSettingsApi], and WebSocket events for live changes.
 *
 * @param managementStorageSettingsApi API client for management storage settings.
 * @param managementStorageSettingsStorage Encrypted storage backing store.
 * @param webSocketService WebSocket service for real-time setting updates.
 * @param repositoryScope Scope used to collect WebSocket events.
 */
class ManagementStorageSettingsRepositoryImpl(
    private val managementStorageSettingsApi: ManagementStorageSettingsApi,
    private val managementStorageSettingsStorage: ManagementStorageSettingsStorage,
    private val webSocketService: WebSocketService,
    repositoryScope: CoroutineScope
) : ManagementStorageSettingsRepository {

    private val updateMutex = Mutex()
    private val _settings = MutableStateFlow<ManagementStorageSettings?>(null)

    init {
        repositoryScope.launch {
            _settings.value = managementStorageSettingsStorage.getManagementStorageSettings()

            webSocketService.observeEvents()
                .filter { it.type == StorageWebSocketEventTypes.MANAGEMENT_STORAGE_SETTINGS_UPDATED }
                .collect { frame ->
                    try {
                        val response = frame.payload?.let {
                            FoundationJson.decodeFromJsonElement<ManagementStorageSettingsPayload>(it)
                        }

                        if (response != null) {
                            updateManagementStorageSettings(response.toManagementStorageSettings())
                        } else {
                            Logger.w { "Received MANAGEMENT_STORAGE_SETTINGS_UPDATED with invalid payload" }
                        }
                    } catch (e: Exception) {
                        Logger.e(e) { "Failed to process WebSocket event" }
                    }
                }
        }
    }

    override suspend fun getManagementStorageSettings(): AppResult<ManagementStorageSettings> {
        _settings.value?.let { return AppResult.Success(it) }

        return updateMutex.withLock {
            val cached = _settings.value ?: managementStorageSettingsStorage.getManagementStorageSettings()

            if (cached != null) {
                _settings.value = cached
                AppResult.Success(cached)
            } else {
                refreshManagementStorageSettingsInternal()
            }
        }
    }

    override suspend fun saveRemoteManagementStorageSettings(storageSettings: ManagementStorageSettings): AppResult<ManagementStorageSettings> {
        val payload = storageSettings.toManagementStorageSettingsPayload()
        return managementStorageSettingsApi.updateManagementStorageSettings(payload)
            .mapSuccess { response ->
                val updatedSettings = response.toManagementStorageSettings()
                updateManagementStorageSettings(updatedSettings)
                updatedSettings
            }
    }

    override suspend fun resetRemoteManagementStorageSettings(): AppResult<ManagementStorageSettings> {
        return managementStorageSettingsApi.resetManagementStorageSettings()
            .mapSuccess { response ->
                val settings = response.toManagementStorageSettings()
                updateManagementStorageSettings(settings)
                settings
            }
    }

    override suspend fun refreshManagementStorageSettings(): AppResult<ManagementStorageSettings> {
        return updateMutex.withLock {
            refreshManagementStorageSettingsInternal()
        }
    }

    override suspend fun updateManagementStorageSettings(storageSettings: ManagementStorageSettings) {
        updateMutex.withLock {
            applySettingsUpdate(storageSettings)
        }
    }

    override fun observeManagementStorageSettings(): Flow<ManagementStorageSettings?> = _settings.asStateFlow()

    private suspend fun refreshManagementStorageSettingsInternal(): AppResult<ManagementStorageSettings> {
        return managementStorageSettingsApi.getManagementStorageSettings()
            .mapSuccess { response ->
                val settings = response.toManagementStorageSettings()
                applySettingsUpdate(settings)
                settings
            }
    }

    private suspend fun applySettingsUpdate(storageSettings: ManagementStorageSettings) {
        managementStorageSettingsStorage.updateManagementStorageSettings(storageSettings)
        _settings.value = storageSettings
    }
}
