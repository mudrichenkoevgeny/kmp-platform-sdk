package io.github.mudrichenkoevgeny.kmp.core.storage.mock.ui.component.file.upload

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.SelectedFilePayload
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.StorageFileUploadComponent
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.StorageFileUploadState
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.category.StorageCategory
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.upload.UploadQueueItemId
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.visibility.StorageFileVisibility
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.ui.component.fileupload.StorageFileUploadUiConfig

/**
 * Mock implementation of [StorageFileUploadComponent] for testing and Compose previews.
 */
@InternalApi
class StorageFileUploadComponentMock(
    initialState: StorageFileUploadState = StorageFileUploadState(config = StorageFileUploadUiConfig())
) : StorageFileUploadComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<StorageFileUploadState> = _state

    var lastSelectedFiles: List<SelectedFilePayload>? = null
    var uploadCallCount = 0
    var lastRemovedItemId: UploadQueueItemId? = null

    override fun getFilePayload(id: UploadQueueItemId): SelectedFilePayload? = null

    override fun onFilesSelected(files: List<SelectedFilePayload>) {
        lastSelectedFiles = files
    }

    override fun onItemCategoryChanged(id: UploadQueueItemId, category: StorageCategory) {}

    override fun onItemVisibilityChanged(id: UploadQueueItemId, visibility: StorageFileVisibility) {}

    override fun onUploadClicked() {
        uploadCallCount++
    }

    override fun onRemoveItemClicked(id: UploadQueueItemId) {
        lastRemovedItemId = id
    }

    fun emitState(newState: StorageFileUploadState) {
        _state.value = newState
    }
}
