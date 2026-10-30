package io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.base.StorageRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFileSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.navigation.screen.open.list.SelfStorageFileListParams

/**
 * Fetches a paginated list of storage files uploaded by the authenticated user.
 *
 * @param storageRepository Storage repository for file operations.
 */
class GetFilesUseCase(
    private val storageRepository: StorageRepository
) {
    /**
     * Executes the use case.
     *
     * @param params Optional listing query, filter, and pagination parameters DTO.
     * @return [AppResult.Success] containing a [PagedResult] of [StorageFileSummary], or [AppResult.Error] on failure.
     */
    suspend operator fun invoke(
        params: SelfStorageFileListParams? = null
    ): AppResult<PagedResult<StorageFileSummary>> {
        return storageRepository.getFiles(params)
    }
}
