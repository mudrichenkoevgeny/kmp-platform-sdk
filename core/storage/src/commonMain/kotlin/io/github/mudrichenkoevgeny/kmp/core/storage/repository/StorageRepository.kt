package io.github.mudrichenkoevgeny.kmp.core.storage.repository

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.category.StorageCategory
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFilePrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFileSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.listing.StorageSortValues
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.presigned.PresignedUrl
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.status.StorageFileStatus
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.visibility.StorageFileVisibility
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.file.DeleteFilesBatchRequest
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.presigned.PresignedUrlRequest
import kotlinx.coroutines.flow.Flow

/**
 * Common repository contract for file storage operations.
 */
interface StorageRepository {
    /**
     * Fetches a paginated list of storage files based on parameters.
     */
    suspend fun getFiles(
        pageNumber: Int? = null,
        pageSize: Int? = null,
        sortBy: StorageSortValues.StorageFileSortBy? = null,
        sortOrder: SortOrder? = null,
        fileIds: List<String>? = null,
        fileName: String? = null,
        mimeTypes: List<String>? = null,
        categories: List<StorageCategory>? = null,
        visibilities: List<StorageFileVisibility>? = null,
        statuses: List<StorageFileStatus>? = null,
        targetScopes: List<String>? = null
    ): AppResult<PagedResult<StorageFileSummary>>

    /**
     * Retrieves detailed private view of a specific file.
     *
     * @param fileId Unique identifier of the target file.
     * @return [AppResult.Success] containing [StorageFilePrivate] or [AppResult.Error].
     */
    suspend fun getFileDetail(fileId: String): AppResult<StorageFilePrivate>

    /**
     * Requests a presigned URL to upload a file directly to S3.
     */
    suspend fun getPresignedUploadUrl(request: PresignedUrlRequest): AppResult<PresignedUrl>

    /**
     * Confirms that a file has been uploaded to S3.
     */
    suspend fun confirmUpload(fileId: String): AppResult<StorageFilePrivate>

    /**
     * Deletes a specific file.
     *
     * @param fileId Unique identifier of the file to delete.
     * @return [AppResult.Success] containing [Unit] or [AppResult.Error].
     */
    suspend fun deleteFile(fileId: String): AppResult<Unit>

    /**
     * Deletes multiple files in a single batch operation.
     *
     * @param request Batch deletion request object containing target file identifiers.
     * @return [AppResult.Success] containing [Unit] or [AppResult.Error].
     */
    suspend fun deleteFilesBatch(request: DeleteFilesBatchRequest): AppResult<Unit>

    /**
     * Observes real-time storage file updates (e.g. from WebSocket push events).
     */
    fun observeFileUpdates(): Flow<StorageFilePrivate>
}
