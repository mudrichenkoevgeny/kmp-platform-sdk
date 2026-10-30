package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.datasource

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base.DeleteFileUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base.DeleteFilesBatchUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base.GetFilesUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFileSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.navigation.screen.open.list.SelfStorageFileListParams
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.file.DeleteFilesBatchRequest

/**
 * [StorageFileListDataSource] implementation for client / self-service storage file list screen.
 *
 * @param getFilesUseCase Use case fetching paged self-service files.
 * @param deleteFileUseCase Use case deleting a single file.
 * @param deleteFilesBatchUseCase Use case deleting files in batch.
 */
class SelfStorageFileListDataSource(
    private val getFilesUseCase: GetFilesUseCase,
    private val deleteFileUseCase: DeleteFileUseCase,
    private val deleteFilesBatchUseCase: DeleteFilesBatchUseCase
) : StorageFileListDataSource {

    override val isSingleDeleteAvailable: Boolean = true
    override val isBatchDeleteAvailable: Boolean = true

    override suspend fun getFiles(query: StorageFileListQuery): AppResult<PagedResult<StorageFileSummary>> {
        val params = query.toSelfParams()
        return getFilesUseCase(params)
    }

    override suspend fun deleteFile(fileId: String): AppResult<Unit> {
        return deleteFileUseCase(fileId)
    }

    override suspend fun deleteFilesBatch(request: DeleteFilesBatchRequest): AppResult<Unit> {
        return deleteFilesBatchUseCase(request)
    }

    private fun StorageFileListQuery.toSelfParams(): SelfStorageFileListParams {
        return SelfStorageFileListParams(
            pageNumber = pageNumber,
            sortBy = sortBy,
            sortOrder = sortOrder,
            categories = categoryChoice?.let { listOf(it) } ?: emptyList(),
            visibilities = visibilityChoice?.let { listOf(it) } ?: emptyList(),
            statuses = statusChoices,
            mimeTypes = mimeTypeText?.let { listOf(it) } ?: emptyList(),
            fileName = fileNameText
        )
    }
}
