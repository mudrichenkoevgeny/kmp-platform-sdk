package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.storage

import co.touchlab.kermit.Logger
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.network.api.storage.StorageApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.category.StorageCategory
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFilePrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFileSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.listing.StorageSortValues
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.presigned.PresignedUrl
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.status.StorageFileStatus
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.visibility.StorageFileVisibility
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.mapper.file.toStorageFilePrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.contract.StorageWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.model.file.StorageFilePrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.file.DeleteFilesBatchRequest
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.presigned.PresignedUrlRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * Implementation of [SelfManagementStorageRepository] for staff self-service storage operations.
 *
 * @param selfManagementStorageApi API client for staff self-service storage operations.
 * @param webSocketService WebSocket service for real-time file updates.
 * @param repositoryScope Scope used to collect WebSocket events.
 */
class SelfManagementStorageRepositoryImpl(
    private val selfManagementStorageApi: StorageApi,
    private val webSocketService: WebSocketService,
    repositoryScope: CoroutineScope
) : SelfManagementStorageRepository {

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
                        Logger.e(e) { "Failed to process WebSocket event in SelfManagementStorageRepositoryImpl" }
                    }
                }
        }
    }

    override suspend fun getFiles(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: StorageSortValues.StorageFileSortBy?,
        sortOrder: SortOrder?,
        fileIds: List<String>?,
        fileName: String?,
        mimeTypes: List<String>?,
        categories: List<StorageCategory>?,
        visibilities: List<StorageFileVisibility>?,
        statuses: List<StorageFileStatus>?,
        targetScopes: List<String>?
    ): AppResult<PagedResult<StorageFileSummary>> {
        return selfManagementStorageApi.getFiles(
            pageNumber = pageNumber,
            pageSize = pageSize,
            sortBy = sortBy,
            sortOrder = sortOrder,
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
        return selfManagementStorageApi.getFileDetail(fileId)
    }

    override suspend fun getPresignedUploadUrl(request: PresignedUrlRequest): AppResult<PresignedUrl> {
        return selfManagementStorageApi.getPresignedUploadUrl(request)
    }

    override suspend fun confirmUpload(fileId: String): AppResult<StorageFilePrivate> {
        return selfManagementStorageApi.confirmUpload(fileId)
    }

    override suspend fun deleteFile(fileId: String): AppResult<Unit> {
        return selfManagementStorageApi.deleteFile(fileId)
    }

    override suspend fun deleteFilesBatch(request: DeleteFilesBatchRequest): AppResult<Unit> {
        return selfManagementStorageApi.deleteFilesBatch(request)
    }

    override fun observeFileUpdates(): Flow<StorageFilePrivate> = _fileUpdates.asSharedFlow()
}
