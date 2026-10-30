package io.github.mudrichenkoevgeny.kmp.core.storage.repository.open

import co.touchlab.kermit.Logger
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.network.api.base.StorageApi
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.base.StorageRepository
import io.github.mudrichenkoevgeny.kmp.core.storage.uploader.DirectFileUploader
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFilePrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFileSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.mapper.file.toStorageFilePrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.navigation.screen.open.list.SelfStorageFileListParams
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.contract.StorageWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.model.file.StorageFilePrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.file.DeleteFilesBatchRequest
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.presigned.PresignedUrlRequest
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * Default implementation of [StorageRepository] for open self-service storage operations.
 *
 * @param openStorageApi API client for open storage operations.
 * @param directFileUploader Uploader for direct S3 binary PUT requests.
 * @param webSocketService WebSocket service for real-time file updates.
 * @param repositoryScope Scope used to collect WebSocket events.
 */
class OpenStorageRepositoryImpl(
    private val openStorageApi: StorageApi,
    private val directFileUploader: DirectFileUploader,
    private val webSocketService: WebSocketService,
    repositoryScope: CoroutineScope
) : StorageRepository {

    private val _fileUpdates = MutableSharedFlow<StorageFilePrivate>()

    init {
        repositoryScope.launch {
            webSocketService.observeEvents()
                .filter { it.type == StorageWebSocketEventTypes.STORAGE_FILE_UPDATED }
                .collect { frame ->
                    try {
                        val payload = frame.payload?.let {
                            FoundationJson.decodeFromJsonElement<StorageFilePrivatePayload>(it)
                        }
                        if (payload != null) {
                            _fileUpdates.emit(payload.toStorageFilePrivate())
                        } else {
                            Logger.w { "Received STORAGE_FILE_UPDATED with invalid payload" }
                        }
                    } catch (e: Exception) {
                        Logger.e(e) { "Failed to process WebSocket event in OpenStorageRepositoryImpl" }
                    }
                }
        }
    }

    override suspend fun getFiles(params: SelfStorageFileListParams?): AppResult<PagedResult<StorageFileSummary>> {
        return openStorageApi.getFiles(params)
    }

    override suspend fun getFileDetail(fileId: String): AppResult<StorageFilePrivate> {
        return openStorageApi.getFileDetail(fileId)
    }

    override suspend fun uploadFile(
        request: PresignedUrlRequest,
        openReadChannel: suspend () -> ByteReadChannel,
        onProgress: ((percent: Int) -> Unit)?
    ): AppResult<StorageFilePrivate> {
        val presignedResult = openStorageApi.getPresignedUploadUrl(request)
        if (presignedResult is AppResult.Error) {
            return presignedResult
        }
        val presigned = (presignedResult as AppResult.Success).data

        val uploadResult = directFileUploader.uploadFile(
            uploadUrl = presigned.uploadUrl,
            contentLength = request.fileSizeBytes,
            openReadChannel = openReadChannel,
            mimeType = request.mimeType,
            onProgress = onProgress
        )
        if (uploadResult is AppResult.Error) {
            return AppResult.Error(uploadResult.error)
        }

        return openStorageApi.confirmUpload(presigned.fileId.asHexDashString())
    }

    override suspend fun deleteFile(fileId: String): AppResult<Unit> {
        return openStorageApi.deleteFile(fileId)
    }

    override suspend fun deleteFilesBatch(request: DeleteFilesBatchRequest): AppResult<Unit> {
        return openStorageApi.deleteFilesBatch(request)
    }

    override fun observeFileUpdates(): Flow<StorageFilePrivate> = _fileUpdates.asSharedFlow()
}
