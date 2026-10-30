package io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload

import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.category.StorageCategory
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.upload.UploadQueueItemId
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.visibility.StorageFileVisibility

/**
 * Presentation logic controller (Decompose component) for the file upload area.
 */
interface StorageFileUploadComponent {
    /** Observable state of the upload queue. */
    val state: Value<StorageFileUploadState>

    /** Retrieves the selected file binary payload for a queued item ID. */
    fun getFilePayload(id: UploadQueueItemId): SelectedFilePayload?

    /** Adds new files to the upload queue and performs initial validation. */
    fun onFilesSelected(files: List<SelectedFilePayload>)

    /** Updates the category for a specific item in the upload queue. */
    fun onItemCategoryChanged(id: UploadQueueItemId, category: StorageCategory)

    /** Updates the visibility for a specific item in the upload queue. */
    fun onItemVisibilityChanged(id: UploadQueueItemId, visibility: StorageFileVisibility)

    /** Starts the batch upload process for all pending files. */
    fun onUploadClicked()

    /** Removes a specific item from the queue, if not currently uploading. */
    fun onRemoveItemClicked(id: UploadQueueItemId)
}
