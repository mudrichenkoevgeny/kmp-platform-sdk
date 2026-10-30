package io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload

import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.upload.UploadQueueItem
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.upload.UploadQueueItemStatus
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.ui.component.fileupload.StorageFileUploadUiConfig

/**
 * State of the storage file upload component.
 *
 * @property config Configuration constraints for the upload queue.
 * @property queueItems Current list of items in the upload queue.
 * @property isUploading Whether an upload operation is actively running.
 */
data class StorageFileUploadState(
    val config: StorageFileUploadUiConfig,
    val queueItems: List<UploadQueueItem> = emptyList(),
    val isUploading: Boolean = false
) {
    /**
     * `true` if there are any idle or failed items ready for upload.
     */
    val hasPending: Boolean
        get() = queueItems.any { uploadQueueItem ->
            uploadQueueItem.status == UploadQueueItemStatus.IDLE || uploadQueueItem.status == UploadQueueItemStatus.FAILED
        }
}
