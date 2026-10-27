package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.storage

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.storage.ManagementStorageApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.category.StorageCategory
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFilePrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFileSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.listing.StorageSortValues
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.status.StorageFileStatus
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.visibility.StorageFileVisibility
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.file.DeleteFilesBatchRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId

/**
 * Implementation of [ManagementStorageRepository].
 *
 * @param managementStorageApi Administrative HTTP endpoints for system-wide file storage management.
 */
class ManagementStorageRepositoryImpl(
    private val managementStorageApi: ManagementStorageApi
) : ManagementStorageRepository {

    override suspend fun getFiles(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: StorageSortValues.StorageFileSortBy?,
        sortOrder: SortOrder?,
        ownerUserIds: List<UserId>?,
        fileIds: List<String>?,
        fileName: String?,
        mimeTypes: List<String>?,
        categories: List<StorageCategory>?,
        visibilities: List<StorageFileVisibility>?,
        statuses: List<StorageFileStatus>?,
        targetScopes: List<String>?
    ): AppResult<PagedResult<StorageFileSummary>> {
        return managementStorageApi.getFiles(
            pageNumber = pageNumber,
            pageSize = pageSize,
            sortBy = sortBy,
            sortOrder = sortOrder,
            ownerUserIds = ownerUserIds,
            fileIds = fileIds,
            fileName = fileName,
            mimeTypes = mimeTypes,
            categories = categories,
            visibilities = visibilities,
            statuses = statuses,
            targetScopes = targetScopes
        )
    }

    override suspend fun getFileDetail(fileId: String): AppResult<StorageFilePrivate> {
        return managementStorageApi.getFileDetail(fileId)
    }

    override suspend fun deleteFile(fileId: String): AppResult<Unit> {
        return managementStorageApi.deleteFile(fileId)
    }

    override suspend fun deleteFilesBatch(request: DeleteFilesBatchRequest): AppResult<Unit> {
        return managementStorageApi.deleteFilesBatch(request)
    }
}
