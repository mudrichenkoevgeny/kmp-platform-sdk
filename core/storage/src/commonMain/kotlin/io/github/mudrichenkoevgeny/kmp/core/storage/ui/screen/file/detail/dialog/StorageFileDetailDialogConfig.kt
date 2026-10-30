package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail.dialog

import kotlinx.serialization.Serializable

/**
 * Serializable configurations driving the Decompose child slot for storage file detail confirmation dialogs.
 */
@Serializable
sealed class StorageFileDetailDialogConfig {
    /** Confirmation dialog configuration for deleting the file. */
    @Serializable
    data class ConfirmDeleteFile(val fileId: String) : StorageFileDetailDialogConfig()
}
