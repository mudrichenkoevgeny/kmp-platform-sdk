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
public class SelfStorageFileListDataSource(
    private val getFilesUseCase: GetFilesUseCase,
    private val deleteFileUseCase: DeleteFileUseCase,
    private val deleteFilesBatchUseCase: DeleteFilesBatchUseCase
) : StorageFileListDataSource {

    public override val isSingleDeleteAvailable: Boolean = true
    public override val isBatchDeleteAvailable: Boolean = true

    public override suspend fun getFiles(query: StorageFileListQuery): AppResult<PagedResult<StorageFileSummary>> {
        val params = query.toSelfParams()
        return getFilesUseCase(params)
    }

    public override suspend fun deleteFile(fileId: String): AppResult<Unit> = deleteFileUseCase(fileId)

    public override suspend fun deleteFilesBatch(request: DeleteFilesBatchRequest): AppResult<Unit> =
        deleteFilesBatchUseCase(
            request
        )

    private fun StorageFileListQuery.toSelfParams(): SelfStorageFileListParams = SelfStorageFileListParams(
        pageNumber = pageNumber,
        sortBy = sortBy,
        sortOrder = sortOrder,
        fileIds = fileIdText?.let { listOf(it) } ?: emptyList(),
        fileName = fileNameText,
        fileSizeBytesFrom = fileSizeBytesFromNumber,
        fileSizeBytesTo = fileSizeBytesToNumber,
        mimeTypes = mimeTypeText?.let { listOf(it) } ?: emptyList(),
        categories = categoryChoice?.let { listOf(it) } ?: emptyList(),
        excludeCategories = excludeCategoriesChoices,
        visibilities = visibilityChoice?.let { listOf(it) } ?: emptyList(),
        excludeVisibilities = excludeVisibilitiesChoices,
        statuses = statusChoices,
        excludeStatuses = excludeStatusesChoices,
        targetScopes = targetScopeText?.let { listOf(it) } ?: emptyList(),
        isTargetScopeSet = isTargetScopeSetChoice,
        createdAtFrom = createdAtFromText,
        createdAtTo = createdAtToText,
        updatedAtFrom = updatedAtFromText,
        updatedAtTo = updatedAtToText,
        isUpdatedAtSet = isUpdatedAtSetChoice,
        searchQuery = searchQueryText
    )
}
