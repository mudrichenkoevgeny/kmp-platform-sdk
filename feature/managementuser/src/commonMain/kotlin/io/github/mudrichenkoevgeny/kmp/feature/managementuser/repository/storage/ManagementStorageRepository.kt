package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.storage

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
 * Management Storage Repository contract for staff administrative file operations.
 */
interface ManagementStorageRepository {
    /**
     * Fetches a paginated list of all files in the platform.
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
     */
    suspend fun getFileDetail(fileId: String): AppResult<StorageFilePrivate>

    /**
     * Deletes a specific file from object storage across the platform.
     */
    suspend fun deleteFile(fileId: String): AppResult<Unit>

    /**
     * Deletes multiple files across the platform in a single batch operation.
     */
    suspend fun deleteFilesBatch(request: DeleteFilesBatchRequest): AppResult<Unit>
}
