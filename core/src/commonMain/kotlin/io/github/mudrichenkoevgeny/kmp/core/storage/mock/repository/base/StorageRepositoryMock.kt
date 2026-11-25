package io.github.mudrichenkoevgeny.kmp.core.storage.mock.repository.base

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.base.StorageRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFilePrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFileSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.navigation.screen.open.list.SelfStorageFileListParams
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.file.DeleteFilesBatchRequest
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.presigned.PresignedUrlRequest
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Mock implementation of [StorageRepository] for testing.
 */
@InternalApi
public class StorageRepositoryMock : StorageRepository {
    public var uploadFileResult: AppResult<StorageFilePrivate> = AppResult.Error(CommonError.Unknown())
    public var getFilesResult: AppResult<PagedResult<StorageFileSummary>> = AppResult.Error(CommonError.Unknown())
    public var getFileDetailResult: AppResult<StorageFilePrivate> = AppResult.Error(CommonError.Unknown())
    public var deleteFileResult: AppResult<Unit> = AppResult.Success(Unit)
    public var deleteFilesBatchResult: AppResult<Unit> = AppResult.Success(Unit)
    private val fileUpdatesFlow = MutableSharedFlow<StorageFilePrivate>()

    public override suspend fun uploadFile(
        request: PresignedUrlRequest,
        openReadChannel: suspend () -> ByteReadChannel,
        onProgress: ((percent: Int) -> Unit)?
    ): AppResult<StorageFilePrivate> {
        onProgress?.invoke(100)
        return uploadFileResult
    }

    public override suspend fun getFiles(
        params: SelfStorageFileListParams?
    ): AppResult<PagedResult<StorageFileSummary>> = getFilesResult

    public override suspend fun getFileDetail(fileId: String): AppResult<StorageFilePrivate> = getFileDetailResult
    public override suspend fun deleteFile(fileId: String): AppResult<Unit> = deleteFileResult
    public override suspend fun deleteFilesBatch(request: DeleteFilesBatchRequest): AppResult<Unit> =
        deleteFilesBatchResult

    public override fun observeFileUpdates(): Flow<StorageFilePrivate> = fileUpdatesFlow.asSharedFlow()
}
