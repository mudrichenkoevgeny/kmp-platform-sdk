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

@InternalApi
class StorageRepositoryMock : StorageRepository {
    var uploadFileResult: AppResult<StorageFilePrivate> = AppResult.Error(CommonError.Unknown())
    var getFilesResult: AppResult<PagedResult<StorageFileSummary>> = AppResult.Error(CommonError.Unknown())
    var getFileDetailResult: AppResult<StorageFilePrivate> = AppResult.Error(CommonError.Unknown())
    var deleteFileResult: AppResult<Unit> = AppResult.Success(Unit)
    var deleteFilesBatchResult: AppResult<Unit> = AppResult.Success(Unit)
    private val _fileUpdates = MutableSharedFlow<StorageFilePrivate>()

    override suspend fun uploadFile(
        request: PresignedUrlRequest,
        openReadChannel: suspend () -> ByteReadChannel,
        onProgress: ((percent: Int) -> Unit)?
    ): AppResult<StorageFilePrivate> {
        onProgress?.invoke(100)
        return uploadFileResult
    }

    override suspend fun getFiles(params: SelfStorageFileListParams?): AppResult<PagedResult<StorageFileSummary>> = getFilesResult
    override suspend fun getFileDetail(fileId: String): AppResult<StorageFilePrivate> = getFileDetailResult
    override suspend fun deleteFile(fileId: String): AppResult<Unit> = deleteFileResult
    override suspend fun deleteFilesBatch(request: DeleteFilesBatchRequest): AppResult<Unit> = deleteFilesBatchResult
    override fun observeFileUpdates(): Flow<StorageFilePrivate> = _fileUpdates.asSharedFlow()
}
