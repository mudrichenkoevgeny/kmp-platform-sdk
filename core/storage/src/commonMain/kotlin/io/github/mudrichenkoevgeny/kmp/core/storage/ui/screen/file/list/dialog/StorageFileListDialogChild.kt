package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.dialog

/**
 * Child instances created for storage file list dialog configurations.
 */
public sealed class StorageFileListDialogChild {
    /** Confirmation dialog instance for deleting a single file. */
    public data class ConfirmDeleteFile(public val fileId: String) : StorageFileListDialogChild()

    /** Confirmation dialog instance for deleting a batch of selected files. */
    public data object ConfirmDeleteBatchFiles : StorageFileListDialogChild()
}
