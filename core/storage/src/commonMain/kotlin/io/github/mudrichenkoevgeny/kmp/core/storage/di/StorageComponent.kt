package io.github.mudrichenkoevgeny.kmp.core.storage.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.base.StorageRepository
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.management.ManagementStorageRepository
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.management.ManagementStorageSettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.open.OpenStorageSettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.storage.uploader.DirectFileUploader
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base.DeleteFileUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base.DeleteFilesBatchUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base.GetFileDetailUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base.GetFilesUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base.UploadFileUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.GetManagementStorageSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.ManagementDeleteFileUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.ManagementDeleteFilesBatchUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.ManagementGetFileDetailUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.ManagementGetFilesUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.ObserveManagementStorageSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.RefreshManagementStorageSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.ResetRemoteManagementStorageSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.UpdateManagementStorageSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.open.ObserveOpenStorageSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.open.GetOpenStorageSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.open.RefreshOpenStorageSettingsUseCase
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Main entry point and dependency wiring for the `core:storage` module.
 *
 * Encapsulates network clients, local encrypted storage, and WebSocket message handling internally,
 * exposing only high-level repositories and use cases.
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
    private val componentScope = parentScope ?: CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val networkModule = StorageNetworkModule(httpClient)
    private val storageModule = StorageStorageModule(encryptedSettings)
    private val repositoryModule = StorageRepositoryModule(
        networkModule = networkModule,
        storageModule = storageModule,
        webSocketService = webSocketService,
        repositoryScope = componentScope
    )
    private val useCaseModule = StorageUseCaseModule(
        repositoryModule = repositoryModule
    )

    // ==========================================
    // 1. Shared Infrastructure
    // ==========================================
    /** Direct S3 file uploader instance for client/management uploads. */
    val directFileUploader: DirectFileUploader
        get() = networkModule.directFileUploader

    /** WebSocket message handler for storage push events. */
    val storageWebSocketMessageHandler: WebSocketMessageHandler
        get() = networkModule.storageWebSocketMessageHandler

    // ==========================================
    // 2. Open / Client Storage (Files & Settings)
    // ==========================================
    /** Repository for open/client storage file operations. */
    val openStorageRepository: StorageRepository
        get() = repositoryModule.openStorageRepository

    /** Repository for staff self-service storage operations. */
    val selfManagementStorageRepository: StorageRepository
        get() = repositoryModule.selfManagementStorageRepository

    /** Repository for open storage settings. */
    val openStorageSettingsRepository: OpenStorageSettingsRepository
        get() = repositoryModule.openStorageSettingsRepository

    /** Use case for refreshing open storage settings. */
    val refreshOpenStorageSettingsUseCase: RefreshOpenStorageSettingsUseCase
        get() = useCaseModule.refreshOpenStorageSettingsUseCase

    /** Use case for getting open storage settings. */
    val getOpenStorageSettingsUseCase: GetOpenStorageSettingsUseCase
        get() = useCaseModule.getOpenStorageSettingsUseCase

    /** Observes open storage settings flow use case. */
    val observeOpenStorageSettingsUseCase: ObserveOpenStorageSettingsUseCase
        get() = useCaseModule.observeOpenStorageSettingsUseCase

    /** Default upload file use case bound to open storage repository. */
    val uploadFileUseCase: UploadFileUseCase
        get() = useCaseModule.createUploadFileUseCase(repositoryModule.openStorageRepository)

    /** Factory method for creating an [UploadFileUseCase] bound to a given repository. */
    fun createUploadFileUseCase(storageRepository: StorageRepository): UploadFileUseCase =
        useCaseModule.createUploadFileUseCase(storageRepository)

    /** Factory method for creating a [GetFilesUseCase] bound to a given repository. */
    fun createGetFilesUseCase(storageRepository: StorageRepository): GetFilesUseCase =
        useCaseModule.createGetFilesUseCase(storageRepository)

    /** Factory method for creating a [GetFileDetailUseCase] bound to a given repository. */
    fun createGetFileDetailUseCase(storageRepository: StorageRepository): GetFileDetailUseCase =
        useCaseModule.createGetFileDetailUseCase(storageRepository)

    /** Factory method for creating a [DeleteFileUseCase] bound to a given repository. */
    fun createDeleteFileUseCase(storageRepository: StorageRepository): DeleteFileUseCase =
        useCaseModule.createDeleteFileUseCase(storageRepository)

    /** Factory method for creating a [DeleteFilesBatchUseCase] bound to a given repository. */
    fun createDeleteFilesBatchUseCase(storageRepository: StorageRepository): DeleteFilesBatchUseCase =
        useCaseModule.createDeleteFilesBatchUseCase(storageRepository)

    // ==========================================
    // 3. Management Storage (Files)
    // ==========================================
    /** Repository for management administrative storage operations. */
    val managementStorageRepository: ManagementStorageRepository
        get() = repositoryModule.managementStorageRepository

    /** Administrative file listing use case. */
    val managementGetFilesUseCase: ManagementGetFilesUseCase
        get() = useCaseModule.managementGetFilesUseCase

    /** Administrative file detail use case. */
    val managementGetFileDetailUseCase: ManagementGetFileDetailUseCase
        get() = useCaseModule.managementGetFileDetailUseCase

    /** Administrative single file deletion use case. */
    val managementDeleteFileUseCase: ManagementDeleteFileUseCase
        get() = useCaseModule.managementDeleteFileUseCase

    /** Administrative batch file deletion use case. */
    val managementDeleteFilesBatchUseCase: ManagementDeleteFilesBatchUseCase
        get() = useCaseModule.managementDeleteFilesBatchUseCase

    // ==========================================
    // 4. Management Storage Settings
    // ==========================================
    /** Repository for management storage settings. */
    val managementStorageSettingsRepository: ManagementStorageSettingsRepository
        get() = repositoryModule.managementStorageSettingsRepository

    /** Retrieves management storage settings use case. */
    val getManagementStorageSettingsUseCase: GetManagementStorageSettingsUseCase
        get() = useCaseModule.getManagementStorageSettingsUseCase

    /** Refreshes management storage settings use case. */
    val refreshManagementStorageSettingsUseCase: RefreshManagementStorageSettingsUseCase
        get() = useCaseModule.refreshManagementStorageSettingsUseCase

    /** Updates management storage settings use case. */
    val updateManagementStorageSettingsUseCase: UpdateManagementStorageSettingsUseCase
        get() = useCaseModule.updateManagementStorageSettingsUseCase

    /** Resets management storage settings use case. */
    val resetRemoteManagementStorageSettingsUseCase: ResetRemoteManagementStorageSettingsUseCase
        get() = useCaseModule.resetRemoteManagementStorageSettingsUseCase

    /** Observes management storage settings flow use case. */
    val observeManagementStorageSettingsUseCase: ObserveManagementStorageSettingsUseCase
        get() = useCaseModule.observeManagementStorageSettingsUseCase

    /** Bundles all management storage file use cases for administrative operations. */
    val managementStorageDependencies: ManagementStorageDependencies
        get() = ManagementStorageDependencies(
            getFilesUseCase = managementGetFilesUseCase,
            getFileDetailUseCase = managementGetFileDetailUseCase,
            uploadFileUseCase = createUploadFileUseCase(repositoryModule.selfManagementStorageRepository),
            deleteFileUseCase = managementDeleteFileUseCase,
            deleteFilesBatchUseCase = managementDeleteFilesBatchUseCase
        )

    fun createStorageDependencies(storageRepository: StorageRepository): OpenStorageDependencies =
        OpenStorageDependencies(
            getFilesUseCase = createGetFilesUseCase(storageRepository),
            getFileDetailUseCase = createGetFileDetailUseCase(storageRepository),
            uploadFileUseCase = createUploadFileUseCase(storageRepository),
            deleteFileUseCase = createDeleteFileUseCase(storageRepository),
            deleteFilesBatchUseCase = createDeleteFilesBatchUseCase(storageRepository)
        )
}
