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
public class StorageFileDetailComponentMock(
    initialState: StorageFileDetailScreenState = StorageFileDetailScreenState.Content(
        file = createTestStorageFilePrivate()
    ),
    public override val isDeleteAvailable: Boolean = true,
    initialDialogSlot: ChildSlot<StorageFileDetailDialogConfig, StorageFileDetailDialogChild>? = null
) : StorageFileDetailComponent {

    private val _state = MutableValue(initialState)
    public override val state: Value<StorageFileDetailScreenState> = _state

    public override val dialogSlotNavigation: SlotNavigation<StorageFileDetailDialogConfig> =
        SlotNavigation<StorageFileDetailDialogConfig>()
    public override val dialogSlot: Value<ChildSlot<StorageFileDetailDialogConfig, StorageFileDetailDialogChild>> =
        MutableValue(initialDialogSlot ?: ChildSlot())

    public var retryCalls: Int = 0
    public var deleteCalls: Int = 0
    public var confirmDeleteCalls: Int = 0
    public var dismissDeleteDialogCalls: Int = 0
    public var userClickCalls: Int = 0
    public var backCalls: Int = 0

    public override fun onRetry() {
        retryCalls++
    }

    public override fun onDeleteClick() {
        deleteCalls++
    }

    public override fun onDismissDeleteDialog() {
        dismissDeleteDialogCalls++
    }

    public override fun onConfirmDelete() {
        confirmDeleteCalls++
    }

    public override fun onUserClick() {
        userClickCalls++
    }

    public override fun onBackClick() {
        backCalls++
    }

    /**
     * Emits a new [newState] to [state] for testing purposes.
     */
    public fun emitState(newState: StorageFileDetailScreenState) {
        _state.value = newState
    }
}
