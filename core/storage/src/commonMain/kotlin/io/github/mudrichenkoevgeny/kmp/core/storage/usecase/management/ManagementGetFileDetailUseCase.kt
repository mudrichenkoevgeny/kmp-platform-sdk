package io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.management.ManagementStorageRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFilePrivate

/**
 * Retrieves detailed private view of any specific file in the platform administratively.
 *
 * @param managementStorageRepository Management storage repository for administrative file operations.
 */
class ManagementGetFileDetailUseCase(
    private val managementStorageRepository: ManagementStorageRepository
) {
    /**
     * Executes the use case.
     *
     * @param fileId Unique identifier of the target file.
     * @return [AppResult.Success] containing [StorageFilePrivate], or [AppResult.Error] on failure.
     */
    suspend operator fun invoke(fileId: String): AppResult<StorageFilePrivate> {
        return managementStorageRepository.getFileDetail(fileId)
    }
}
