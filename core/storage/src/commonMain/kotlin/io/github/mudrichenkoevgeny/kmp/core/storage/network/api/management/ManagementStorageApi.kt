package io.github.mudrichenkoevgeny.kmp.core.storage.network.api.management

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFilePrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFileSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.navigation.screen.management.list.ManagementStorageFileListParams
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.file.DeleteFilesBatchRequest

/**
 * Management Storage API contract for staff administrative file operations.
 */
interface ManagementStorageApi {

    /**
     * Fetches a paginated list of files uploaded across the platform.
     *
     * @param params Optional query parameters DTO matching the administrative listing search schema.
     * @return [AppResult.Success] containing a [PagedResult] of [StorageFileSummary] or [AppResult.Error].
     */
    suspend fun getFiles(params: ManagementStorageFileListParams? = null): AppResult<PagedResult<StorageFileSummary>>

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
