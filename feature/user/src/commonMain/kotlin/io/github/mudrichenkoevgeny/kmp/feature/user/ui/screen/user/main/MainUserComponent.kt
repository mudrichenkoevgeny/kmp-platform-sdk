package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.main

import com.arkivanov.decompose.value.Value

/**
 * Interface for the main user screen component displaying summary and actions.
 */
interface MainUserComponent {
    /** Reactive state for the screen. */
    val state: Value<MainUserScreenState>

    /** Retries data fetch on failure. */
    fun onRetry()

    /** Action to open TOTP security settings. */
    fun onTotpMainClick()

    /** Action to open sessions management. */
    fun onSessionsClick()

    /** Action to open credentials/identifiers management. */
    fun onIdentifiersClick()

    /** Action to open personal storage files list. */
    fun onFilesListClick()

    /** Action to schedule user deletion. */
    fun onDeleteUserClick()

    /** Confirms scheduled user deletion. */
    fun onConfirmDeleteUserClick()

    /** Dismisses scheduled deletion confirmation dialog. */
    fun onDismissDeleteUserDialog()

    /** Triggers user logout confirmation dialog. */
    fun onLogoutClick()

    /** Confirms user logout. */
    fun onConfirmLogoutClick()

    /** Dismisses logout confirmation dialog. */
    fun onDismissLogoutDialog()

    /** Action to show login flow when unauthorized. */
    fun onLoginClick()
}
