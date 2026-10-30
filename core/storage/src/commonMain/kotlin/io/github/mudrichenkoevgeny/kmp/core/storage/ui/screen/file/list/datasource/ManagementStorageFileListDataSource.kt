package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.datasource

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.TextListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.ManagementDeleteFileUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.ManagementDeleteFilesBatchUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.ManagementGetFilesUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFileSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.listing.StorageFilterValues.StorageFileFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.navigation.screen.management.list.ManagementStorageFileListParams
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.file.DeleteFilesBatchRequest

/**
 * [StorageFileListDataSource] implementation for management storage file list screens (global or staff self-service).
 *
 * @param getFilesUseCase Use case fetching paged management storage files.
 * @param deleteFileUseCase Use case deleting a single file in management context.
 * @param deleteFilesBatchUseCase Use case deleting files in batch in management context.
 */
class ManagementStorageFileListDataSource(
    private val getFilesUseCase: ManagementGetFilesUseCase,
    private val deleteFileUseCase: ManagementDeleteFileUseCase,
    private val deleteFilesBatchUseCase: ManagementDeleteFilesBatchUseCase
) : StorageFileListDataSource {

    override val isSingleDeleteAvailable: Boolean = true
    override val isBatchDeleteAvailable: Boolean = true

    override suspend fun getFiles(query: StorageFileListQuery): AppResult<PagedResult<StorageFileSummary>> {
        val params = query.toManagementParams()
        return getFilesUseCase(params)
    }

    override suspend fun deleteFile(fileId: String): AppResult<Unit> {
        return deleteFileUseCase(fileId)
    }

    override suspend fun deleteFilesBatch(request: DeleteFilesBatchRequest): AppResult<Unit> {
        return deleteFilesBatchUseCase(request)
    }

    private fun StorageFileListQuery.toManagementParams(): ManagementStorageFileListParams {
        val ownerUserIdText = (filterStates[StorageFileFilterValues.OWNER_USER_ID] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

        val ownerUserIdsList = ownerUserId?.let { listOf(it) }
            ?: ownerUserIdText?.let { listOf(it) }
            ?: emptyList()

        return ManagementStorageFileListParams(
            pageNumber = pageNumber,
            sortBy = sortBy,
            sortOrder = sortOrder,
            ownerUserIds = ownerUserIdsList,
            categories = categoryChoice?.let { listOf(it) } ?: emptyList(),
            visibilities = visibilityChoice?.let { listOf(it) } ?: emptyList(),
            statuses = statusChoices,
            mimeTypes = mimeTypeText?.let { listOf(it) } ?: emptyList(),
            fileName = fileNameText
        )
    }
}
