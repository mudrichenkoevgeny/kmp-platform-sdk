package io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.base.StorageRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFilePrivate

/**
 * Retrieves detailed private view of a specific file uploaded by the authenticated user.
 *
 * @param storageRepository Storage repository for file operations.
 */
class GetFileDetailUseCase(
    private val storageRepository: StorageRepository
) {
    /**
     * Executes the use case.
     *
     * @param fileId Unique identifier of the target file.
     * @return [AppResult.Success] containing [StorageFilePrivate], or [AppResult.Error] on failure.
     */
    suspend operator fun invoke(fileId: String): AppResult<StorageFilePrivate> {
        return storageRepository.getFileDetail(fileId)
    }
}
