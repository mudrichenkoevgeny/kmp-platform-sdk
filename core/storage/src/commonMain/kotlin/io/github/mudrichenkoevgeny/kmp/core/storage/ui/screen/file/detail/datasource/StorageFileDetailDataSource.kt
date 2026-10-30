package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail.datasource

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFilePrivate

/**
 * Data source contract for storage file detail data fetching and deletion operations.
 */
interface StorageFileDetailDataSource {
    /** Whether file deletion action is supported by this data source. */
    val isDeleteAvailable: Boolean

    /**
     * Fetches file details by unique identifier.
     *
     * @param fileId Unique identifier of target file.
     * @return [AppResult.Success] containing [StorageFilePrivate], or [AppResult.Error].
     */
    suspend fun getFileDetail(fileId: String): AppResult<StorageFilePrivate>

    /**
     * Deletes file by unique identifier.
     *
     * @param fileId Unique identifier of target file to delete.
     * @return [AppResult.Success] containing [Unit], or [AppResult.Error].
     */
    suspend fun deleteFile(fileId: String): AppResult<Unit>
}
