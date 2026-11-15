package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.main

/**
 * Component driving the main management landing screen.
 */
public interface MainManagementComponent {
    /** Navigates to global user list screen. */
    public fun onGlobalUserListClick()

    /** Navigates to all platform sessions screen. */
    public fun onGlobalSessionListClick()

    /** Navigates to all platform identifiers screen. */
    public fun onGlobalIdentifierListClick()

    /** Navigates to audit logs screen. */
    public fun onAuditEventListClick()

    /** Navigates to system settings hub. */
    public fun onSettingsClick()

    /** Navigates to all platform storage files screen. */
    public fun onGlobalStorageFileListClick()
}
