package io.github.mudrichenkoevgeny.kmp.core.storage.repository.base

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFilePrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFileSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.navigation.screen.open.list.SelfStorageFileListParams
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.file.DeleteFilesBatchRequest
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.presigned.PresignedUrlRequest
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.flow.Flow

/**
 * Common repository contract for self-service file storage operations.
 */
interface StorageRepository {
    /**
     * Fetches a paginated list of storage files based on parameters.
     *
     * @param params Optional query parameters DTO for filtering and pagination.
     * @return [AppResult.Success] containing a [PagedResult] of [StorageFileSummary] or [AppResult.Error].
     */
    suspend fun getFiles(params: SelfStorageFileListParams? = null): AppResult<PagedResult<StorageFileSummary>>

    /**
     * Retrieves detailed private view of a specific file.
     *
     * @param fileId Unique identifier of the target file.
     * @return [AppResult.Success] containing [StorageFilePrivate] or [AppResult.Error].
     */
    suspend fun getFileDetail(fileId: String): AppResult<StorageFilePrivate>

    /**
     * Performs direct S3 upload flow and confirms file creation.
     *
     * @param request Presigned upload URL request parameters DTO.
     * @param openReadChannel Suspending function opening a [ByteReadChannel] stream to read payload bytes.
     * @param onProgress Optional callback tracking upload completion percentage (0 to 100).
     * @return [AppResult.Success] containing confirmed [StorageFilePrivate] or [AppResult.Error].
     */
    suspend fun uploadFile(
        request: PresignedUrlRequest,
        openReadChannel: suspend () -> ByteReadChannel,
        onProgress: ((percent: Int) -> Unit)? = null
    ): AppResult<StorageFilePrivate>

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
