package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail.dialog.StorageFileDetailDialogChild
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail.dialog.StorageFileDetailDialogConfig

/**
 * Presentation logic controller (Decompose component) for the storage file detail screen.
 */
interface StorageFileDetailComponent {

    /** Observable screen state flow. */
    val state: Value<StorageFileDetailScreenState>

    /** Decompose child slot for confirmation dialogs. */
    val dialogSlot: Value<ChildSlot<StorageFileDetailDialogConfig, StorageFileDetailDialogChild>>

    /** Slot navigator for dialogs. */
    val dialogSlotNavigation: SlotNavigation<StorageFileDetailDialogConfig>

    /** Whether the delete action is available for this contour. */
    val isDeleteAvailable: Boolean

    /** Retries loading file details after failure. */
    fun onRetry()

    /** Opens delete confirmation dialog. */
    fun onDeleteClick()

    /** Dismisses delete confirmation dialog. */
    fun onDismissDeleteDialog()

    /** Confirms and executes file deletion. */
    fun onConfirmDelete()

    /** Handles click on owner user ID. */
    fun onUserClick()

    /** Triggers back navigation. */
    fun onBackClick()
}
