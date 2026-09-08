package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.main

/**
 * Component driving the main management settings landing screen.
 */
interface MainManagementSettingsComponent {
    /** Navigates to edit auth settings. */
    fun onEditAuthSettingsClick()

    /** Navigates to edit global settings. */
    fun onEditGlobalSettingsClick()

    /** Navigates to edit security settings. */
    fun onEditSecuritySettingsClick()
}
