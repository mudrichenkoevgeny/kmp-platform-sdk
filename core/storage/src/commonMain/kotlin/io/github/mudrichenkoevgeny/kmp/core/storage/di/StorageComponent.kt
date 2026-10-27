package io.github.mudrichenkoevgeny.kmp.core.storage.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Main entry point and dependency wiring for the `core:storage` module.
 *
 * Exposes repositories and use cases for storage settings management.
 *
 * @param httpClient Shared Ktor client for REST operations.
 * @param encryptedSettings Encrypted settings backing store.
 * @param webSocketService WebSocket service for reactive settings updates.
 * @param parentScope Parent coroutine scope for repository jobs.
 */
class StorageComponent(
    httpClient: HttpClient,
    encryptedSettings: EncryptedSettings,
    webSocketService: WebSocketService,
    parentScope: CoroutineScope? = null
) {
    private val componentScope = parentScope
        ?: CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val networkModule = StorageNetworkModule(httpClient)
    private val storageModule = StorageStorageModule(encryptedSettings)
    private val repositoryModule = StorageRepositoryModule(
        openStorageSettingsApi = networkModule.openStorageSettingsApi,
        openStorageSettingsStorage = storageModule.openStorageSettingsStorage,
        webSocketService = webSocketService,
        repositoryScope = componentScope
    )
    private val useCaseModule = StorageUseCaseModule(
        openStorageSettingsRepository = repositoryModule.openStorageSettingsRepository
    )

    /** Repository for open storage settings. */
    val storageSettingsRepository get() = repositoryModule.openStorageSettingsRepository

    /** Use case for refreshing open storage settings. */
    val refreshOpenStorageSettingsUseCase get() = useCaseModule.refreshOpenStorageSettingsUseCase

    /** Use case for getting open storage settings. */
    val getOpenStorageSettingsUseCase get() = useCaseModule.getOpenStorageSettingsUseCase
}
