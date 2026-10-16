package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.main

/**
 * Component driving the main management landing screen.
 */
interface MainManagementComponent {
    /** Navigates to global user list screen. */
    fun onGlobalUserListClick()

    /** Navigates to all platform sessions screen. */
    fun onGlobalSessionListClick()

    /** Navigates to all platform identifiers screen. */
    fun onGlobalIdentifierListClick()

    /** Navigates to audit logs screen. */
    fun onAuditEventListClick()

    /** Navigates to system settings hub. */
    fun onSettingsClick()
}
