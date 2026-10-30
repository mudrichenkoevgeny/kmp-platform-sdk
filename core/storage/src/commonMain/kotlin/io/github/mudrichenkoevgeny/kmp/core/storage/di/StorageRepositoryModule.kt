package io.github.mudrichenkoevgeny.kmp.core.storage.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.base.StorageRepository
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.management.ManagementStorageRepository
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.management.ManagementStorageRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.management.ManagementStorageSettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.management.ManagementStorageSettingsRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.management.SelfManagementStorageRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.open.OpenStorageRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.open.OpenStorageSettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.open.OpenStorageSettingsRepositoryImpl
import kotlinx.coroutines.CoroutineScope

/**
 * Internal repository wiring for `core/storage`.
 *
 * Coordinates network APIs, local storage, and WebSocket services into concrete repository implementations.
 *
 * @param networkModule Internal network module providing Ktor API clients.
 * @param storageModule Internal storage module providing encrypted storage instances.
 * @param webSocketService WebSocket service for reactive updates.
 * @param repositoryScope Coroutine scope for repository background jobs.
 */
internal class StorageRepositoryModule(
    private val networkModule: StorageNetworkModule,
    private val storageModule: StorageStorageModule,
    private val webSocketService: WebSocketService,
    private val repositoryScope: CoroutineScope
) {

    /** Default [StorageRepository] implementation for open/client storage file operations. */
    val openStorageRepository: StorageRepository by lazy {
        OpenStorageRepositoryImpl(
            openStorageApi = networkModule.openStorageApi,
            directFileUploader = networkModule.directFileUploader,
            webSocketService = webSocketService,
            repositoryScope = repositoryScope
        )
    }

    /** Default [StorageRepository] implementation for staff self-service storage operations. */
    val selfManagementStorageRepository: StorageRepository by lazy {
        SelfManagementStorageRepositoryImpl(
            selfManagementStorageApi = networkModule.selfManagementStorageApi,
            directFileUploader = networkModule.directFileUploader,
            webSocketService = webSocketService,
            repositoryScope = repositoryScope
        )
    }

    /** Default [ManagementStorageRepository] implementation for administrative storage operations. */
    val managementStorageRepository: ManagementStorageRepository by lazy {
        ManagementStorageRepositoryImpl(
            managementStorageApi = networkModule.managementStorageApi
        )
    }

    /** Default [OpenStorageSettingsRepository] implementation for open storage settings. */
    val openStorageSettingsRepository: OpenStorageSettingsRepository by lazy {
        OpenStorageSettingsRepositoryImpl(
            openStorageSettingsApi = networkModule.openStorageSettingsApi,
            openStorageSettingsStorage = storageModule.openStorageSettingsStorage,
            webSocketService = webSocketService,
            repositoryScope = repositoryScope
        )
    }

    /** Default [ManagementStorageSettingsRepository] implementation for management storage settings. */
    val managementStorageSettingsRepository: ManagementStorageSettingsRepository by lazy {
        ManagementStorageSettingsRepositoryImpl(
            managementStorageSettingsApi = networkModule.managementStorageSettingsApi,
            managementStorageSettingsStorage = storageModule.managementStorageSettingsStorage,
            webSocketService = webSocketService,
            repositoryScope = repositoryScope
        )
    }
}
