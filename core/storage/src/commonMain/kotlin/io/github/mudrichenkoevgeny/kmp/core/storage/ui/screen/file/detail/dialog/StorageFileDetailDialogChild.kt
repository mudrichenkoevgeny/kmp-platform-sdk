package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail.dialog

/**
 * Child instances created for storage file detail dialog configurations.
 */
sealed class StorageFileDetailDialogChild {
    /** Confirmation dialog instance for deleting the file. */
    data class ConfirmDeleteFile(val fileId: String) : StorageFileDetailDialogChild()
}
