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
public class ManagementStorageRepositoryImpl(
    private val managementStorageApi: ManagementStorageApi
) : ManagementStorageRepository {

    public override suspend fun getFiles(
        params: ManagementStorageFileListParams?
    ): AppResult<PagedResult<StorageFileSummary>> =
        managementStorageApi.getFiles(
            params
        )

    public override suspend fun getFileDetail(fileId: String): AppResult<StorageFilePrivate> =
        managementStorageApi.getFileDetail(
            fileId
        )

    public override suspend fun deleteFile(fileId: String): AppResult<Unit> = managementStorageApi.deleteFile(fileId)

    public override suspend fun deleteFilesBatch(request: DeleteFilesBatchRequest): AppResult<Unit> =
        managementStorageApi.deleteFilesBatch(
            request
        )
}
