package io.github.mudrichenkoevgeny.kmp.core.storage.repository.management

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.network.api.management.ManagementStorageApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFilePrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFileSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.navigation.screen.management.list.ManagementStorageFileListParams
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.file.DeleteFilesBatchRequest

/**
 * Implementation of [ManagementStorageRepository].
 *
 * @param managementStorageApi Administrative HTTP endpoints for system-wide file storage management.
 */
class ManagementStorageRepositoryImpl(
    private val managementStorageApi: ManagementStorageApi
) : ManagementStorageRepository {

    override suspend fun getFiles(params: ManagementStorageFileListParams?): AppResult<PagedResult<StorageFileSummary>> {
        return managementStorageApi.getFiles(params)
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
