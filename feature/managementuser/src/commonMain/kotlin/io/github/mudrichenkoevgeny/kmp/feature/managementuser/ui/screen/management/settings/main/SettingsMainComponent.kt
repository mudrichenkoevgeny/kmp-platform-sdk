package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.main

/**
 * Component driving the main settings screen in management portal.
 */
public interface SettingsMainComponent {
    /** Navigates to edit auth settings. */
    public fun onEditAuthSettingsClick()

    /** Navigates to edit global settings. */
    public fun onEditGlobalSettingsClick()

    /** Navigates to edit security settings. */
    public fun onEditSecuritySettingsClick()

    /** Navigates to edit storage settings. */
    public fun onEditStorageSettingsClick()

    /** Navigates back. */
    public fun onBackClick()
}
