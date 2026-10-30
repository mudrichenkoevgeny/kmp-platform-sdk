package io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.management.ManagementStorageRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.file.DeleteFilesBatchRequest

/**
 * Deletes multiple files across the platform in a single batch operation administratively.
 *
 * @param managementStorageRepository Management storage repository for administrative file operations.
 */
class ManagementDeleteFilesBatchUseCase(
    private val managementStorageRepository: ManagementStorageRepository
) {
    /**
     * Executes the use case.
     *
     * @param request Batch deletion request containing array of file identifiers.
     * @return [AppResult.Success] containing [Unit], or [AppResult.Error] on failure.
     */
    suspend operator fun invoke(request: DeleteFilesBatchRequest): AppResult<Unit> {
        return managementStorageRepository.deleteFilesBatch(request)
    }
}
