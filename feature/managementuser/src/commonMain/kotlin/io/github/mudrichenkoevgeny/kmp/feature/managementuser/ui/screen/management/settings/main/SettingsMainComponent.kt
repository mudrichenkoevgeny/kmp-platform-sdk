package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.main

/**
 * Component driving the main settings screen in management portal.
 */
interface SettingsMainComponent {
    /** Navigates to edit auth settings. */
    fun onEditAuthSettingsClick()

    /** Navigates to edit global settings. */
    fun onEditGlobalSettingsClick()

    /** Navigates to edit security settings. */
    fun onEditSecuritySettingsClick()

    /** Navigates to edit storage settings. */
    fun onEditStorageSettingsClick()

    /** Navigates back. */
    fun onBackClick()
}
