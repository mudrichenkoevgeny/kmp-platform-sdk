package io.github.mudrichenkoevgeny.kmp.core.storage.mock.network.api.base

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.network.api.base.StorageApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFilePrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFileSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.presigned.PresignedUrl
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.navigation.screen.open.list.SelfStorageFileListParams
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.file.DeleteFilesBatchRequest
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.presigned.PresignedUrlRequest

/**
 * Mock implementation of [StorageApi] for testing.
 */
@InternalApi
public class StorageApiMock : StorageApi {
    public var presignedUrlResult: AppResult<PresignedUrl> = AppResult.Error(CommonError.Unknown())
    public var confirmUploadResult: AppResult<StorageFilePrivate> = AppResult.Error(CommonError.Unknown())
    public var getFilesResult: AppResult<PagedResult<StorageFileSummary>> = AppResult.Error(CommonError.Unknown())
    public var getFileDetailResult: AppResult<StorageFilePrivate> = AppResult.Error(CommonError.Unknown())
    public var deleteFileResult: AppResult<Unit> = AppResult.Success(Unit)
    public var deleteFilesBatchResult: AppResult<Unit> = AppResult.Success(Unit)

    public override suspend fun getPresignedUploadUrl(request: PresignedUrlRequest): AppResult<PresignedUrl> =
        presignedUrlResult

    public override suspend fun confirmUpload(fileId: String): AppResult<StorageFilePrivate> = confirmUploadResult
    public override suspend fun getFiles(
        params: SelfStorageFileListParams?
    ): AppResult<PagedResult<StorageFileSummary>> = getFilesResult

    public override suspend fun getFileDetail(fileId: String): AppResult<StorageFilePrivate> = getFileDetailResult
    public override suspend fun deleteFile(fileId: String): AppResult<Unit> = deleteFileResult
    public override suspend fun deleteFilesBatch(request: DeleteFilesBatchRequest): AppResult<Unit> =
        deleteFilesBatchResult
}
