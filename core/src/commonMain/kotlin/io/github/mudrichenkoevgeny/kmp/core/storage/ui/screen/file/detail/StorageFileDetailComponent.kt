package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail.dialog.StorageFileDetailDialogChild
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail.dialog.StorageFileDetailDialogConfig

/**
 * Presentation logic controller (Decompose component) for the storage file detail screen.
 */
public interface StorageFileDetailComponent {

    /** Observable screen state flow. */
    public val state: Value<StorageFileDetailScreenState>

    /** Decompose child slot for confirmation dialogs. */
    public val dialogSlot: Value<ChildSlot<StorageFileDetailDialogConfig, StorageFileDetailDialogChild>>

    /** Slot navigator for dialogs. */
    public val dialogSlotNavigation: SlotNavigation<StorageFileDetailDialogConfig>

    /** Whether the delete action is available for this contour. */
    public val isDeleteAvailable: Boolean

    /** Retries loading file details after failure. */
    public fun onRetry()

    /** Opens delete confirmation dialog. */
    public fun onDeleteClick()

    /** Dismisses delete confirmation dialog. */
    public fun onDismissDeleteDialog()

    /** Confirms and executes file deletion. */
    public fun onConfirmDelete()

    /** Handles click on owner user ID. */
    public fun onUserClick()

    /** Triggers back navigation. */
    public fun onBackClick()
}
