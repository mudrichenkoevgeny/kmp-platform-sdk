package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.settings

import co.touchlab.kermit.Logger
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.auth.settings.ManagementAuthSettingsApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.storage.auth.settings.ManagementAuthSettingsStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.ManagementAuthSettings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.auth.settings.toManagementAuthSettings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.auth.settings.toManagementAuthSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.contract.UserWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.settings.ManagementAuthSettingsPayload
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
 * Implements [ManagementAuthSettingsRepository] with a mutex-guarded in-memory [MutableStateFlow], persistence
 * via [ManagementAuthSettingsStorage], HTTP via [ManagementAuthSettingsApi], and live updates from [WebSocketService] for
 * `MANAGEMENT_AUTH_SETTINGS_UPDATED` events.
 *
 * @param managementAuthSettingsApi Remote read endpoint for auth settings.
 * @param managementAuthSettingsStorage Encrypted or local persistence for settings snapshots.
 * @param webSocketService Source of push updates for auth settings changes.
 * @param repositoryScope Long-lived scope used for the WebSocket collector started in `init`.
 */
class ManagementAuthSettingsRepositoryImpl(
    private val managementAuthSettingsApi: ManagementAuthSettingsApi,
    private val managementAuthSettingsStorage: ManagementAuthSettingsStorage,
    private val webSocketService: WebSocketService,
    repositoryScope: CoroutineScope
) : ManagementAuthSettingsRepository {

    private val updateMutex = Mutex()
    private val _settings = MutableStateFlow<ManagementAuthSettings?>(null)

    init {
        repositoryScope.launch {
            _settings.value = managementAuthSettingsStorage.getManagementAuthSettings()

            webSocketService.observeEvents()
                .filter { it.type == UserWebSocketEventTypes.MANAGEMENT_AUTH_SETTINGS_UPDATED }
                .collect { frame ->
                    try {
                        val response = frame.payload?.let {
                            FoundationJson.decodeFromJsonElement<ManagementAuthSettingsPayload>(it)
                        }

                        if (response != null) {
                            updateManagementAuthSettings(response.toManagementAuthSettings())
                        } else {
                            Logger.w { "Received MANAGEMENT_AUTH_SETTINGS_UPDATED with invalid payload" }
                        }
                    } catch (e: Exception) {
                        Logger.e(e) { "Failed to process WebSocket event" }
                    }
                }
        }
    }

    override suspend fun getManagementAuthSettings(): AppResult<ManagementAuthSettings> {
        _settings.value?.let { return AppResult.Success(it) }

        return updateMutex.withLock {
            val cached = _settings.value ?: managementAuthSettingsStorage.getManagementAuthSettings()

            if (cached != null) {
                _settings.value = cached
                AppResult.Success(cached)
            } else {
                refreshManagementAuthSettingsInternal()
            }
        }
    }

    override suspend fun saveRemoteManagementAuthSettings(authSettings: ManagementAuthSettings): AppResult<Unit> {
        val payload = authSettings.toManagementAuthSettingsPayload()
        return managementAuthSettingsApi.updateManagementAuthSettings(payload)
            .mapSuccess {
                updateManagementAuthSettings(authSettings)
            }
    }

    override suspend fun refreshManagementAuthSettings(): AppResult<ManagementAuthSettings> {
        return updateMutex.withLock {
            refreshManagementAuthSettingsInternal()
        }
    }

    override suspend fun updateManagementAuthSettings(authSettings: ManagementAuthSettings) {
        updateMutex.withLock {
            applySettingsUpdate(authSettings)
        }
    }

    override fun observeManagementAuthSettings(): Flow<ManagementAuthSettings?> = _settings.asStateFlow()

    private suspend fun refreshManagementAuthSettingsInternal(): AppResult<ManagementAuthSettings> {
        return managementAuthSettingsApi.getManagementAuthSettings()
            .mapSuccess { response ->
                val settings = response.toManagementAuthSettings()
                applySettingsUpdate(settings)
                settings
            }
    }

    private suspend fun applySettingsUpdate(managementAuthSettings: ManagementAuthSettings) {
        managementAuthSettingsStorage.updateManagementAuthSettings(managementAuthSettings)
        _settings.value = managementAuthSettings
    }
}
