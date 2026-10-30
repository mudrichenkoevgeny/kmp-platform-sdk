package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.dialog

import kotlinx.serialization.Serializable

/**
 * Serializable configurations driving the Decompose child slot for storage file list confirmation dialogs.
 */
@Serializable
sealed class StorageFileListDialogConfig {
    /** Confirmation dialog configuration for deleting a single file. */
    @Serializable
    data class ConfirmDeleteFile(val fileId: String) : StorageFileListDialogConfig()

    /** Confirmation dialog configuration for deleting a batch of selected files. */
    @Serializable
    data object ConfirmDeleteBatchFiles : StorageFileListDialogConfig()
}
