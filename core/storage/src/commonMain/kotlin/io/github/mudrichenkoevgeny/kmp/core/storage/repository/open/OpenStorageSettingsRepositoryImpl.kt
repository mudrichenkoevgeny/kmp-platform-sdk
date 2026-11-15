package io.github.mudrichenkoevgeny.kmp.core.storage.repository.open

import co.touchlab.kermit.Logger
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.core.storage.network.api.open.OpenStorageSettingsApi
import io.github.mudrichenkoevgeny.kmp.core.storage.storage.open.OpenStorageSettingsStorage
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
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * [OpenStorageSettingsRepository] backing in-memory flow, [OpenStorageSettingsStorage], REST updates via
 * [OpenStorageSettingsApi], and WebSocket events for live changes.
 *
 * @param openStorageSettingsApi API client for fetching open storage settings.
 * @param openStorageSettingsStorage Encrypted storage backing store.
 * @param webSocketService WebSocket service for real-time setting updates.
 * @param repositoryScope Scope used to collect WebSocket events.
 */
public class OpenStorageSettingsRepositoryImpl(
    private val openStorageSettingsApi: OpenStorageSettingsApi,
    private val openStorageSettingsStorage: OpenStorageSettingsStorage,
    private val webSocketService: WebSocketService,
    repositoryScope: CoroutineScope
) : OpenStorageSettingsRepository {

    private val updateMutex = Mutex()
    private val settingsStateFlow = MutableStateFlow<OpenStorageSettings?>(null)

    init {
        repositoryScope.launch {
            settingsStateFlow.value = openStorageSettingsStorage.getOpenStorageSettings()

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
                    } catch (e: SerializationException) {
                        Logger.e(e) { "Failed to process WebSocket event in OpenStorageSettingsRepositoryImpl" }
                    } catch (e: IllegalArgumentException) {
                        Logger.e(e) { "Failed to process WebSocket event in OpenStorageSettingsRepositoryImpl" }
                    } catch (e: IllegalStateException) {
                        Logger.e(e) { "Failed to process WebSocket event in OpenStorageSettingsRepositoryImpl" }
                    }
                }
        }
    }

    public override suspend fun getOpenStorageSettings(): AppResult<OpenStorageSettings> {
        settingsStateFlow.value?.let { return AppResult.Success(it) }

        return updateMutex.withLock {
            val cached = settingsStateFlow.value ?: openStorageSettingsStorage.getOpenStorageSettings()

            if (cached != null) {
                settingsStateFlow.value = cached
                AppResult.Success(cached)
            } else {
                refreshOpenStorageSettingsInternal()
            }
        }
    }

    public override suspend fun refreshOpenStorageSettings(): AppResult<OpenStorageSettings> = updateMutex.withLock {
        refreshOpenStorageSettingsInternal()
    }

    public override suspend fun updateOpenStorageSettings(openStorageSettings: OpenStorageSettings) {
        updateMutex.withLock {
            applySettingsUpdate(openStorageSettings)
        }
    }

    public override fun observeOpenStorageSettings(): Flow<OpenStorageSettings?> = settingsStateFlow.asStateFlow()

    private suspend fun refreshOpenStorageSettingsInternal(): AppResult<OpenStorageSettings> =
        openStorageSettingsApi.getStorageSettings()
            .mapSuccess { response ->
                val settings = response.toOpenStorageSettings()
                applySettingsUpdate(settings)
                settings
            }

    private suspend fun applySettingsUpdate(openStorageSettings: OpenStorageSettings) {
        openStorageSettingsStorage.updateOpenStorageSettings(openStorageSettings)
        settingsStateFlow.value = openStorageSettings
    }
}
