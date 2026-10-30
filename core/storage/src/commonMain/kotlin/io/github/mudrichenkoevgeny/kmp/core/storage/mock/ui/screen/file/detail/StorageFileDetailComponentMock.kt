package io.github.mudrichenkoevgeny.kmp.core.storage.mock.ui.screen.file.detail

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail.StorageFileDetailComponent
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail.StorageFileDetailScreenState
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail.dialog.StorageFileDetailDialogChild
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail.dialog.StorageFileDetailDialogConfig
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.domain.model.file.createTestStorageFilePrivate

/**
 * Mock implementation of [StorageFileDetailComponent] for testing and Compose previews.
 */
@InternalApi
class StorageFileDetailComponentMock(
    initialState: StorageFileDetailScreenState = StorageFileDetailScreenState.Content(
        file = createTestStorageFilePrivate()
    ),
    override val isDeleteAvailable: Boolean = true,
    initialDialogSlot: ChildSlot<StorageFileDetailDialogConfig, StorageFileDetailDialogChild>? = null
) : StorageFileDetailComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<StorageFileDetailScreenState> = _state

    override val dialogSlotNavigation = SlotNavigation<StorageFileDetailDialogConfig>()
    override val dialogSlot: Value<ChildSlot<StorageFileDetailDialogConfig, StorageFileDetailDialogChild>> =
        MutableValue(initialDialogSlot ?: ChildSlot())

    var retryCalls = 0
    var deleteCalls = 0
    var confirmDeleteCalls = 0
    var dismissDeleteDialogCalls = 0
    var userClickCalls = 0
    var backCalls = 0

    override fun onRetry() {
        retryCalls++
    }

    override fun onDeleteClick() {
        deleteCalls++
    }

    override fun onDismissDeleteDialog() {
        dismissDeleteDialogCalls++
    }

    override fun onConfirmDelete() {
        confirmDeleteCalls++
    }

    override fun onUserClick() {
        userClickCalls++
    }

    override fun onBackClick() {
        backCalls++
    }

    fun emitState(newState: StorageFileDetailScreenState) {
        _state.value = newState
    }
}
