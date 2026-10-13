package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings

import kotlinx.serialization.Serializable

/**
 * Serializable configurations driving the Decompose child slot for settings confirmation dialogs.
 */
@Serializable
sealed interface SettingsDialogConfig {
    /** Modal dialog asking confirmation before resetting remote settings to default. */
    @Serializable
    data object ConfirmReset : SettingsDialogConfig
}

/**
 * Child instances created for settings dialog configurations.
 */
sealed interface SettingsDialogChild {
    data object ConfirmReset : SettingsDialogChild
}
