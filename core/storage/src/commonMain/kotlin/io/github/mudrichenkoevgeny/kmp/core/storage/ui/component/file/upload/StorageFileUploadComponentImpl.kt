package io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnDestroy
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.ErrorId
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.error.model.StorageError
import io.github.mudrichenkoevgeny.kmp.core.storage.error.parser.StorageErrorParser
import io.github.mudrichenkoevgeny.kmp.core.storage.mapper.StorageCategoryMapper
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base.UploadFileUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.common.error.model.ApiErrorResponse
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.category.StorageCategory
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFilePrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.upload.UploadQueueItem
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.upload.UploadQueueItemId
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.upload.UploadQueueItemStatus
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.upload.toUploadQueueItemIdOrThrow
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.visibility.StorageFileVisibility
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.validation.StorageUploadValidator
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.presigned.PresignedUrlRequest
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.ui.component.fileupload.StorageFileUploadUiConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Default implementation of [StorageFileUploadComponent].
 *
 * @param componentContext Decompose component context.
 * @param config Configuration options and constraints for the upload queue.
 * @param uploadFileUseCase Use case for executing direct file uploads.
 * @param onUploaded Callback invoked when all queued files are successfully uploaded.
 */
@OptIn(InternalApi::class)
class StorageFileUploadComponentImpl(
    componentContext: ComponentContext,
    private val config: StorageFileUploadUiConfig,
    private val uploadFileUseCase: UploadFileUseCase,
    private val onUploaded: (List<StorageFilePrivate>) -> Unit
) : StorageFileUploadComponent, ComponentContext by componentContext {

    private val _state = MutableValue(StorageFileUploadState(config = config))
    override val state: Value<StorageFileUploadState> = _state

    private val filePayloads = mutableMapOf<UploadQueueItemId, SelectedFilePayload>()
    private val componentScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    init {
        lifecycle.doOnDestroy {
            componentScope.cancel()
        }
    }

    override fun onFilesSelected(files: List<SelectedFilePayload>) {
        val currentItems = _state.value.queueItems
        val newQueueItems = mutableListOf<UploadQueueItem>()

        files.forEach { payload ->
            val existsInCurrent = currentItems.any {
                it.fileName == payload.fileName &&
                    it.fileSizeBytes == payload.fileSizeBytes
            }
            val existsInNew = newQueueItems.any {
                it.fileName == payload.fileName &&
                    it.fileSizeBytes == payload.fileSizeBytes
            }
            if (existsInCurrent || existsInNew) {
                return@forEach
            }

            val inferredCategory = StorageCategoryMapper.inferFromMimeType(payload.mimeType)
            var status = UploadQueueItemStatus.IDLE
            var appError: StorageError? = null

            val currentTotal = currentItems.size + newQueueItems.size + 1
            val maxFiles = config.maxFiles
            if (maxFiles != null && !StorageUploadValidator.isBatchSizeValid(maxFiles, currentTotal)) {
                status = UploadQueueItemStatus.FAILED
                appError = StorageError.MaxBatchUploadCountExceeded(
                    maxBatchUploadCount = maxFiles
                )
            } else if (!StorageUploadValidator.isFileSizeValid(
                    config.maxFileSizeBytes,
                    payload.fileSizeBytes
                )) {
                status = UploadQueueItemStatus.FAILED
                appError = StorageError.FileSizeExceedsLimit(
                    maxFileSizeBytes = config.maxFileSizeBytes,
                    fileSizeBytes = payload.fileSizeBytes,
                    category = inferredCategory.value,
                    fileName = payload.fileName
                )
            } else if (config.acceptedFileTypes.isNotEmpty() &&
                !StorageUploadValidator.isFileTypeAccepted(
                    config.acceptedFileTypes,
                    payload.fileName,
                    payload.mimeType
                )) {
                status = UploadQueueItemStatus.FAILED
                appError = StorageError.UnsupportedMimeType(
                    mimeType = payload.mimeType,
                    category = inferredCategory.value,
                    fileName = payload.fileName
                )
            }

            val apiError = if (appError != null) {
                ApiErrorResponse(
                    id = ErrorId.generate().asHexDashString(),
                    code = appError.code,
                    message = StorageErrorParser.parse(appError) ?: "",
                    args = emptyMap()
                )
            } else {
                null
            }

            val queueItemId = ErrorId.generate().asHexDashString().toUploadQueueItemIdOrThrow()
            filePayloads[queueItemId] = payload
            newQueueItems.add(
                UploadQueueItem(
                    id = queueItemId,
                    category = config.category ?: inferredCategory,
                    visibility = config.visibility ?: StorageFileVisibility.PRIVATE,
                    fileName = payload.fileName,
                    fileSizeBytes = payload.fileSizeBytes,
                    mimeType = payload.mimeType,
                    progressPercent = 0,
                    status = status,
                    error = apiError
                )
            )
        }

        if (newQueueItems.isNotEmpty()) {
            _state.value = _state.value.copy(
                queueItems = currentItems + newQueueItems
            )
        }
    }

    override fun onUploadClicked() {
        if (_state.value.isUploading) {
            return
        }
        val currentItems = _state.value.queueItems
        val pending = currentItems.filter {
            it.status == UploadQueueItemStatus.IDLE ||
                it.status == UploadQueueItemStatus.FAILED
        }
        if (pending.isEmpty()) {
            return
        }

        _state.value = _state.value.copy(isUploading = true)

        componentScope.launch {
            pending.forEach { item ->
                val payload = filePayloads[item.id] ?: return@forEach
                updateItemState(item.id) {
                    it.copy(
                        status = UploadQueueItemStatus.UPLOADING,
                        error = null,
                        progressPercent = 0
                    )
                }

                val request = PresignedUrlRequest(
                    fileName = payload.fileName,
                    fileSizeBytes = payload.fileSizeBytes,
                    mimeType = payload.mimeType,
                    category = item.category.value,
                    visibility = item.visibility.value,
                    targetScope = config.targetScope ?: ""
                )

                val result = uploadFileUseCase(
                    request = request,
                    openReadChannel = payload.openReadChannel,
                    onProgress = { percent ->
                        updateItemState(item.id) {
                            it.copy(progressPercent = percent)
                        }
                    }
                )

                when (result) {
                    is AppResult.Success -> {
                        updateItemState(item.id) {
                            it.copy(
                                status = UploadQueueItemStatus.COMPLETED,
                                progressPercent = 100,
                                uploadedFile = result.data
                            )
                        }
                    }
                    is AppResult.Error -> {
                        val appError = result.error
                        val apiError = ApiErrorResponse(
                            id = ErrorId.generate().asHexDashString(),
                            code = appError.code,
                            message = StorageErrorParser.parse(appError) ?: "",
                            args = emptyMap()
                        )
                        updateItemState(item.id) {
                            it.copy(
                                status = UploadQueueItemStatus.FAILED,
                                error = apiError
                            )
                        }
                    }
                }
            }

            _state.value = _state.value.copy(isUploading = false)
            val finalItems = _state.value.queueItems
            if (finalItems.all { it.status == UploadQueueItemStatus.COMPLETED }) {
                val completedFiles = finalItems.mapNotNull { it.uploadedFile }
                onUploaded(completedFiles)
            }
        }
    }

    override fun getFilePayload(id: UploadQueueItemId): SelectedFilePayload? = filePayloads[id]

    override fun onItemCategoryChanged(id: UploadQueueItemId, category: StorageCategory) {
        if (_state.value.isUploading) return
        updateItemState(id) { item ->
            if (item.status != UploadQueueItemStatus.UPLOADING) {
                item.copy(category = category)
            } else {
                item
            }
        }
    }

    override fun onItemVisibilityChanged(id: UploadQueueItemId, visibility: StorageFileVisibility) {
        if (_state.value.isUploading) return
        updateItemState(id) { item ->
            if (item.status != UploadQueueItemStatus.UPLOADING) {
                item.copy(visibility = visibility)
            } else {
                item
            }
        }
    }

    override fun onRemoveItemClicked(id: UploadQueueItemId) {
        if (!_state.value.isUploading) {
            val currentItems = _state.value.queueItems
            val item = currentItems.find { it.id == id }
            val isNotUploading = item != null &&
                item.status != UploadQueueItemStatus.UPLOADING
            if (isNotUploading) {
                _state.value = _state.value.copy(
                    queueItems = currentItems.filter { it.id != id }
                )
                filePayloads.remove(id)
            }
        }
    }

    private fun updateItemState(
        id: UploadQueueItemId,
        update: (UploadQueueItem) -> UploadQueueItem
    ) {
        val currentItems = _state.value.queueItems
        val index = currentItems.indexOfFirst { it.id == id }
        if (index != -1) {
            val newItems = currentItems.toMutableList()
            newItems[index] = update(newItems[index])
            _state.value = _state.value.copy(queueItems = newItems)
        }
    }
}
