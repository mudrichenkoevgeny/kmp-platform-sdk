package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.main

import com.arkivanov.decompose.value.Value

/**
 * Interface for the main user screen component displaying summary and actions.
 */
public interface MainUserComponent {
    /** Reactive state for the screen. */
    public val state: Value<MainUserScreenState>

    /** Retries data fetch on failure. */
    public fun onRetry()

    /** Action to open TOTP security settings. */
    public fun onTotpMainClick()

    /** Action to edit user details. */
    public fun onEditUserClick()

    /** Action to open sessions management. */
    public fun onSessionsClick()

    /** Action to open credentials/identifiers management. */
    public fun onIdentifiersClick()

    /** Action to open personal storage files list. */
    public fun onFilesListClick()

    /** Action to schedule user deletion. */
    public fun onDeleteUserClick()

    /** Confirms scheduled user deletion. */
    public fun onConfirmDeleteUserClick()

    /** Dismisses scheduled deletion confirmation dialog. */
    public fun onDismissDeleteUserDialog()

    /** Triggers user logout confirmation dialog. */
    public fun onLogoutClick()

    /** Confirms user logout. */
    public fun onConfirmLogoutClick()

    /** Dismisses logout confirmation dialog. */
    public fun onDismissLogoutDialog()

    /** Action to show login flow when unauthorized. */
    public fun onLoginClick()
}
