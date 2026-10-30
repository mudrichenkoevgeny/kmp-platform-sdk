package io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.base.StorageRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.file.DeleteFilesBatchRequest

/**
 * Deletes multiple files uploaded by the authenticated user in a single batch operation.
 *
 * @param storageRepository Storage repository for file operations.
 */
class DeleteFilesBatchUseCase(
    private val storageRepository: StorageRepository
) {
    /**
     * Executes the use case.
     *
     * @param request Batch deletion request containing array of file identifiers.
     * @return [AppResult.Success] containing [Unit], or [AppResult.Error] on failure.
     */
    suspend operator fun invoke(request: DeleteFilesBatchRequest): AppResult<Unit> {
        return storageRepository.deleteFilesBatch(request)
    }
}
