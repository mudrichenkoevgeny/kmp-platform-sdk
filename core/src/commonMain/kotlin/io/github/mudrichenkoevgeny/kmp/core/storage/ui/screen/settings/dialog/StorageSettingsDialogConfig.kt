package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.settings.dialog

import kotlinx.serialization.Serializable

/**
 * Serializable configurations driving the Decompose child slot for storage settings confirmation dialogs.
 */
@Serializable
public sealed class StorageSettingsDialogConfig {
    /** Modal dialog asking confirmation before resetting remote storage settings to default. */
    @Serializable
    public data object ConfirmReset : StorageSettingsDialogConfig()
}
