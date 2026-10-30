package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.datasource

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFileSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.file.DeleteFilesBatchRequest

/**
 * Data source abstraction providing file fetch and deletion capabilities for storage file list UI screens.
 */
interface StorageFileListDataSource {

    /** Whether single file deletion is available in this data source contour. */
    val isSingleDeleteAvailable: Boolean

    /** Whether batch file deletion is available in this data source contour. */
    val isBatchDeleteAvailable: Boolean

    /**
     * Fetches a paginated list of storage files according to [query].
     *
     * @param query Filtering, sorting, and pagination parameters.
     * @return [AppResult] containing a [PagedResult] of [StorageFileSummary].
     */
    suspend fun getFiles(query: StorageFileListQuery): AppResult<PagedResult<StorageFileSummary>>

    /**
     * Deletes a single storage file by ID.
     *
     * @param fileId Target file ID to delete.
     * @return [AppResult] indicating success or failure error.
     */
    suspend fun deleteFile(fileId: String): AppResult<Unit>

    /**
     * Deletes multiple storage files in batch.
     *
     * @param request Batch deletion request containing list of file IDs.
     * @return [AppResult] indicating success or failure error.
     */
    suspend fun deleteFilesBatch(request: DeleteFilesBatchRequest): AppResult<Unit>
}
