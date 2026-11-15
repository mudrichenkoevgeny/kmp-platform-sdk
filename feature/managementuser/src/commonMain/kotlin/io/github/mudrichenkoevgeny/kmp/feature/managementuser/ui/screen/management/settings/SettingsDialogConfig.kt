package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings

import kotlinx.serialization.Serializable

/**
 * Serializable configurations driving the Decompose child slot for settings confirmation dialogs.
 */
@Serializable
public sealed interface SettingsDialogConfig {
    /** Modal dialog asking confirmation before resetting remote settings to default. */
    @Serializable
    public data object ConfirmReset : SettingsDialogConfig
}

/**
 * Child instances created for settings dialog configurations.
 */
public sealed interface SettingsDialogChild {
    public data object ConfirmReset : SettingsDialogChild
}
