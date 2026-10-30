package io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.management.ManagementStorageRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFileSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.navigation.screen.management.list.ManagementStorageFileListParams

/**
 * Fetches a paginated list of all files across the platform administratively.
 *
 * @param managementStorageRepository Management storage repository for administrative file operations.
 */
class ManagementGetFilesUseCase(
    private val managementStorageRepository: ManagementStorageRepository
) {
    /**
     * Executes the use case.
     *
     * @param params Optional query, filter, and pagination parameters DTO.
     * @return [AppResult.Success] containing a [PagedResult] of [StorageFileSummary], or [AppResult.Error] on failure.
     */
    suspend operator fun invoke(
        params: ManagementStorageFileListParams? = null
    ): AppResult<PagedResult<StorageFileSummary>> {
        return managementStorageRepository.getFiles(params)
    }
}
