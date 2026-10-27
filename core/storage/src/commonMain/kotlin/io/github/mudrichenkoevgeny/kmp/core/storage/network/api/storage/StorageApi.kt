package io.github.mudrichenkoevgeny.kmp.core.storage.network.api.storage

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

/**
 * Common REST API client interface for file storage operations returning mapped domain models.
 */
interface StorageApi {
    /**
     * Retrieves a presigned URL to upload a file directly to S3 using input parameters.
     *
     * @param request Presigned upload URL request parameters DTO.
     * @return [AppResult.Success] containing [PresignedUrl] or [AppResult.Error].
     */
    suspend fun getPresignedUploadUrl(request: PresignedUrlRequest): AppResult<PresignedUrl>

    /**
     * Confirms that a file has been successfully uploaded to S3.
     *
     * @param fileId Unique identifier of the uploaded file.
     * @return [AppResult.Success] containing [StorageFilePrivate] or [AppResult.Error].
     */
    suspend fun confirmUpload(fileId: String): AppResult<StorageFilePrivate>

    /**
     * Fetches a paginated list of storage files based on query parameters for the current authenticated user.
     *
     * @return [AppResult.Success] containing a [PagedResult] of [StorageFileSummary] or [AppResult.Error].
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
     * @param fileId Unique identifier of the requested file.
     * @return [AppResult.Success] containing [StorageFilePrivate] or [AppResult.Error].
     */
    suspend fun getFileDetail(fileId: String): AppResult<StorageFilePrivate>

    /**
     * Deletes a specific file.
     *
     * @param fileId Unique identifier of the target file to delete.
     * @return [AppResult.Success] containing [Unit] or [AppResult.Error].
     */
    suspend fun deleteFile(fileId: String): AppResult<Unit>

    /**
     * Deletes multiple files in a single batch operation using a request DTO.
     *
     * @param request Batch deletion request parameters containing target file identifiers.
     * @return [AppResult.Success] containing [Unit] or [AppResult.Error].
     */
    suspend fun deleteFilesBatch(request: DeleteFilesBatchRequest): AppResult<Unit>
}
