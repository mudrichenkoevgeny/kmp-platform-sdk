package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.dialog

/**
 * Child instances created for settings dialog configurations.
 */
public sealed interface SettingsDialogChild {
    /**
     * Child instance for confirming settings reset.
     */
    public data object ConfirmReset : SettingsDialogChild
}
