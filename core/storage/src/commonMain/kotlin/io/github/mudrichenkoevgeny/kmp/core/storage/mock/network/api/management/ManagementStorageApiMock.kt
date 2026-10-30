package io.github.mudrichenkoevgeny.kmp.core.storage.mock.network.api.management

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.network.api.management.ManagementStorageApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFilePrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFileSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.navigation.screen.management.list.ManagementStorageFileListParams
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.file.DeleteFilesBatchRequest

@InternalApi
class ManagementStorageApiMock : ManagementStorageApi {
    var getFilesResult: AppResult<PagedResult<StorageFileSummary>> = AppResult.Error(CommonError.Unknown())
    var getFileDetailResult: AppResult<StorageFilePrivate> = AppResult.Error(CommonError.Unknown())
    var deleteFileResult: AppResult<Unit> = AppResult.Success(Unit)
    var deleteFilesBatchResult: AppResult<Unit> = AppResult.Success(Unit)

    override suspend fun getFiles(params: ManagementStorageFileListParams?): AppResult<PagedResult<StorageFileSummary>> = getFilesResult
    override suspend fun getFileDetail(fileId: String): AppResult<StorageFilePrivate> = getFileDetailResult
    override suspend fun deleteFile(fileId: String): AppResult<Unit> = deleteFileResult
    override suspend fun deleteFilesBatch(request: DeleteFilesBatchRequest): AppResult<Unit> = deleteFilesBatchResult
}
