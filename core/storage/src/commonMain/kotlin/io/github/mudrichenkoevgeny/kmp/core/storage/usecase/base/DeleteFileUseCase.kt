package io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.base.StorageRepository

/**
 * Deletes a specific file uploaded by the authenticated user.
 *
 * @param storageRepository Storage repository for file operations.
 */
class DeleteFileUseCase(
    private val storageRepository: StorageRepository
) {
    /**
     * Executes the use case.
     *
     * @param fileId Unique identifier of the target file to delete.
     * @return [AppResult.Success] containing [Unit], or [AppResult.Error] on failure.
     */
    suspend operator fun invoke(fileId: String): AppResult<Unit> {
        return storageRepository.deleteFile(fileId)
    }
}
