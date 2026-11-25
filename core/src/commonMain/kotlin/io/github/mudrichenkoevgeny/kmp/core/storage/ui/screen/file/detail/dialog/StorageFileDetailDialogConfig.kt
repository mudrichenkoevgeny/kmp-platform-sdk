package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail.dialog

import kotlinx.serialization.Serializable

/**
 * Serializable configurations driving the Decompose child slot for storage file detail confirmation dialogs.
 */
@Serializable
public sealed class StorageFileDetailDialogConfig {
    /** Confirmation dialog configuration for deleting the file. */
    @Serializable
    public data class ConfirmDeleteFile(public val fileId: String) : StorageFileDetailDialogConfig()
}
