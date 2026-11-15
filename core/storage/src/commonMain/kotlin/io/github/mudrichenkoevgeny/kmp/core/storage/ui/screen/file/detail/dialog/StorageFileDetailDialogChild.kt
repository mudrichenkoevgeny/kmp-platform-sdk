package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail.dialog

/**
 * Child instances created for storage file detail dialog configurations.
 */
public sealed class StorageFileDetailDialogChild {
    /** Confirmation dialog instance for deleting the file. */
    public data class ConfirmDeleteFile(public val fileId: String) : StorageFileDetailDialogChild()
}
