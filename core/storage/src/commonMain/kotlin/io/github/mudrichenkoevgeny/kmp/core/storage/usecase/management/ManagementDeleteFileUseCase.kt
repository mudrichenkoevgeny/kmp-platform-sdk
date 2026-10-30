package io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.management.ManagementStorageRepository

/**
 * Deletes a specific file from object storage across the platform administratively.
 *
 * @param managementStorageRepository Management storage repository for administrative file operations.
 */
class ManagementDeleteFileUseCase(
    private val managementStorageRepository: ManagementStorageRepository
) {
    /**
     * Executes the use case.
     *
     * @param fileId Unique identifier of the target file to delete.
     * @return [AppResult.Success] containing [Unit], or [AppResult.Error] on failure.
     */
    suspend operator fun invoke(fileId: String): AppResult<Unit> {
        return managementStorageRepository.deleteFile(fileId)
    }
}
