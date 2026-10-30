package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.settings.dialog

/**
 * Child instances created for storage settings dialog configurations.
 */
sealed class StorageSettingsDialogChild {
    /** Confirmation dialog instance for resetting storage settings to defaults. */
    data object ConfirmReset : StorageSettingsDialogChild()
}
