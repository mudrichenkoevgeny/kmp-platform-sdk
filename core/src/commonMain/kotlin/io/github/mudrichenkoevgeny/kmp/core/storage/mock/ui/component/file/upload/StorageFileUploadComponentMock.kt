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
public class StorageFileUploadComponentMock(
    initialState: StorageFileUploadState = StorageFileUploadState(config = StorageFileUploadUiConfig())
) : StorageFileUploadComponent {

    private val _state = MutableValue(initialState)
    public override val state: Value<StorageFileUploadState> = _state

    public var lastSelectedFiles: List<SelectedFilePayload>? = null
    public var uploadCallCount: Int = 0
    public var lastRemovedItemId: UploadQueueItemId? = null

    public override fun getFilePayload(id: UploadQueueItemId): SelectedFilePayload? = null

    public override fun onFilesSelected(files: List<SelectedFilePayload>) {
        lastSelectedFiles = files
    }

    public override fun onItemCategoryChanged(
        id: UploadQueueItemId,
        category: StorageCategory
    ): Unit = Unit

    public override fun onItemVisibilityChanged(
        id: UploadQueueItemId,
        visibility: StorageFileVisibility
    ): Unit = Unit

    public override fun onUploadClicked() {
        uploadCallCount++
    }

    public override fun onRemoveItemClicked(id: UploadQueueItemId) {
        lastRemovedItemId = id
    }

    /**
     * Emits a new [newState] to [state] for testing purposes.
     */
    public fun emitState(newState: StorageFileUploadState) {
        _state.value = newState
    }
}
