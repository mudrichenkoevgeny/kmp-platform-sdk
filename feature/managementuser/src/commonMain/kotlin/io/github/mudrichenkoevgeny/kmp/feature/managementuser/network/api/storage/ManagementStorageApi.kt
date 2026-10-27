package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.storage

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.category.StorageCategory
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFilePrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFileSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.listing.StorageSortValues
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.status.StorageFileStatus
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.visibility.StorageFileVisibility
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.file.DeleteFilesBatchRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId

/**
 * Management Storage API contract for staff administrative file operations.
 */
interface ManagementStorageApi {
    /**
     * Fetches a paginated list of files uploaded across the platform.
     *
     * @return [AppResult.Success] containing a [PagedResult] of [StorageFileSummary] or [AppResult.Error].
     */
    suspend fun getFiles(
        pageNumber: Int? = null,
        pageSize: Int? = null,
        sortBy: StorageSortValues.StorageFileSortBy? = null,
        sortOrder: SortOrder? = null,
        ownerUserIds: List<UserId>? = null,
        fileIds: List<String>? = null,
        fileName: String? = null,
        mimeTypes: List<String>? = null,
        categories: List<StorageCategory>? = null,
        visibilities: List<StorageFileVisibility>? = null,
        statuses: List<StorageFileStatus>? = null,
        targetScopes: List<String>? = null
    ): AppResult<PagedResult<StorageFileSummary>>

    /**
     * Retrieves detailed private view of any specific file in the platform.
     *
     * @param fileId Unique identifier of the target file.
     * @return [AppResult.Success] containing [StorageFilePrivate] or [AppResult.Error].
     */
    suspend fun getFileDetail(fileId: String): AppResult<StorageFilePrivate>

    /**
     * Deletes any specific file from object storage across the platform.
     *
     * @param fileId Unique identifier of the target file to delete.
     * @return [AppResult.Success] containing [Unit] or [AppResult.Error].
     */
    suspend fun deleteFile(fileId: String): AppResult<Unit>

    /**
     * Deletes multiple files across the platform in a single batch operation using a request DTO.
     *
     * @param request Batch deletion request parameters containing target file identifiers.
     * @return [AppResult.Success] containing [Unit] or [AppResult.Error].
     */
    suspend fun deleteFilesBatch(request: DeleteFilesBatchRequest): AppResult<Unit>
}
