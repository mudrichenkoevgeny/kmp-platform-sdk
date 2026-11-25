package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.datasource

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.ManagementDeleteFileUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.ManagementDeleteFilesBatchUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.ManagementGetFilesUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.toUserIdOrNull
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFileSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.navigation.screen.management.list.ManagementStorageFileListParams
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.file.DeleteFilesBatchRequest

/**
 * [StorageFileListDataSource] implementation for management storage file list screens (global or staff self-service).
 *
 * @param getFilesUseCase Use case fetching paged management storage files.
 * @param deleteFileUseCase Use case deleting a single file in management context.
 * @param deleteFilesBatchUseCase Use case deleting files in batch in management context.
 */
public class ManagementStorageFileListDataSource(
    private val getFilesUseCase: ManagementGetFilesUseCase,
    private val deleteFileUseCase: ManagementDeleteFileUseCase,
    private val deleteFilesBatchUseCase: ManagementDeleteFilesBatchUseCase
) : StorageFileListDataSource {

    public override val isSingleDeleteAvailable: Boolean = true
    public override val isBatchDeleteAvailable: Boolean = true

    public override suspend fun getFiles(query: StorageFileListQuery): AppResult<PagedResult<StorageFileSummary>> {
        val params = query.toManagementParams()
        return getFilesUseCase(params)
    }

    public override suspend fun deleteFile(fileId: String): AppResult<Unit> = deleteFileUseCase(fileId)

    public override suspend fun deleteFilesBatch(request: DeleteFilesBatchRequest): AppResult<Unit> =
        deleteFilesBatchUseCase(
            request
        )

    private fun StorageFileListQuery.toManagementParams(): ManagementStorageFileListParams {
        val ownerUserIdsList = ownerUserId?.let { listOf(it) }
            ?: ownerUserIdText?.toUserIdOrNull()?.let { listOf(it) }
            ?: emptyList()

        return ManagementStorageFileListParams(
            pageNumber = pageNumber,
            sortBy = sortBy,
            sortOrder = sortOrder,
            fileIds = fileIdText?.let { listOf(it) } ?: emptyList(),
            ownerUserIds = ownerUserIdsList,
            isOwnerUserIdSet = isOwnerUserIdSetChoice,
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
}
