package io.github.mudrichenkoevgeny.kmp.core.storage.di

import io.github.mudrichenkoevgeny.kmp.core.storage.repository.base.StorageRepository
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

/**
 * Internal use-case wiring for `core/storage`.
 *
 * Assembles open/client and management use cases from [StorageRepositoryModule].
 *
 * @param repositoryModule Repository module providing storage repository instances.
 */
internal class StorageUseCaseModule(
    private val repositoryModule: StorageRepositoryModule
) {

    /** Forces a network refresh of open storage settings. */
    val refreshOpenStorageSettingsUseCase by lazy {
        RefreshOpenStorageSettingsUseCase(repositoryModule.openStorageSettingsRepository)
    }

    /** Retrieves stored or remote open storage settings. */
    val getOpenStorageSettingsUseCase by lazy {
        GetOpenStorageSettingsUseCase(repositoryModule.openStorageSettingsRepository)
    }

    /** Observes open storage settings flow use case. */
    val observeOpenStorageSettingsUseCase by lazy {
        ObserveOpenStorageSettingsUseCase(repositoryModule.openStorageSettingsRepository)
    }

    /** Creates an [UploadFileUseCase] instance bound to the provided [storageRepository]. */
    fun createUploadFileUseCase(storageRepository: StorageRepository): UploadFileUseCase {
        return UploadFileUseCase(storageRepository = storageRepository)
    }

    /** Creates a [GetFilesUseCase] instance bound to the provided [storageRepository]. */
    fun createGetFilesUseCase(storageRepository: StorageRepository): GetFilesUseCase {
        return GetFilesUseCase(storageRepository)
    }

    /** Creates a [GetFileDetailUseCase] instance bound to the provided [storageRepository]. */
    fun createGetFileDetailUseCase(storageRepository: StorageRepository): GetFileDetailUseCase {
        return GetFileDetailUseCase(storageRepository)
    }

    /** Creates a [DeleteFileUseCase] instance bound to the provided [storageRepository]. */
    fun createDeleteFileUseCase(storageRepository: StorageRepository): DeleteFileUseCase {
        return DeleteFileUseCase(storageRepository)
    }

    /** Creates a [DeleteFilesBatchUseCase] instance bound to the provided [storageRepository]. */
    fun createDeleteFilesBatchUseCase(storageRepository: StorageRepository): DeleteFilesBatchUseCase {
        return DeleteFilesBatchUseCase(storageRepository)
    }

    /** Administrative file listing use case. */
    val managementGetFilesUseCase by lazy {
        ManagementGetFilesUseCase(repositoryModule.managementStorageRepository)
    }

    /** Administrative file detail use case. */
    val managementGetFileDetailUseCase by lazy {
        ManagementGetFileDetailUseCase(repositoryModule.managementStorageRepository)
    }

    /** Administrative single file deletion use case. */
    val managementDeleteFileUseCase by lazy {
        ManagementDeleteFileUseCase(repositoryModule.managementStorageRepository)
    }

    /** Administrative batch file deletion use case. */
    val managementDeleteFilesBatchUseCase by lazy {
        ManagementDeleteFilesBatchUseCase(repositoryModule.managementStorageRepository)
    }

    /** Retrieves management storage settings use case. */
    val getManagementStorageSettingsUseCase by lazy {
        GetManagementStorageSettingsUseCase(repositoryModule.managementStorageSettingsRepository)
    }

    /** Refreshes management storage settings use case. */
    val refreshManagementStorageSettingsUseCase by lazy {
        RefreshManagementStorageSettingsUseCase(repositoryModule.managementStorageSettingsRepository)
    }

    /** Updates management storage settings use case. */
    val updateManagementStorageSettingsUseCase by lazy {
        UpdateManagementStorageSettingsUseCase(repositoryModule.managementStorageSettingsRepository)
    }

    /** Resets management storage settings use case. */
    val resetRemoteManagementStorageSettingsUseCase by lazy {
        ResetRemoteManagementStorageSettingsUseCase(repositoryModule.managementStorageSettingsRepository)
    }

    /** Observes management storage settings flow use case. */
    val observeManagementStorageSettingsUseCase by lazy {
        ObserveManagementStorageSettingsUseCase(repositoryModule.managementStorageSettingsRepository)
    }
}
