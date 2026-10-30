package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.dialog

/**
 * Child instances created for storage file list dialog configurations.
 */
sealed class StorageFileListDialogChild {
    /** Confirmation dialog instance for deleting a single file. */
    data class ConfirmDeleteFile(val fileId: String) : StorageFileListDialogChild()

    /** Confirmation dialog instance for deleting a batch of selected files. */
    data object ConfirmDeleteBatchFiles : StorageFileListDialogChild()
}
