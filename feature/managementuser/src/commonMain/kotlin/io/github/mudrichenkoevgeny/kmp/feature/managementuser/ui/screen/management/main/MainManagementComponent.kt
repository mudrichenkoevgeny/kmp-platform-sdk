package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.main

/**
 * Component driving the main management landing screen.
 */
interface MainManagementComponent {
    /** Navigates to edit auth settings. */
    fun onEditAuthSettingsClick()

    /** Navigates to edit global settings. */
    fun onEditGlobalSettingsClick()

    /** Navigates to edit security settings. */
    fun onEditSecuritySettingsClick()

    /** Navigates to global user list screen. */
    fun onGlobalUserListClick()

    /** Navigates to audit logs screen. */
    fun onAuditEventListClick()

    /** Navigates to all platform sessions screen. */
    fun onGlobalSessionListClick()
}