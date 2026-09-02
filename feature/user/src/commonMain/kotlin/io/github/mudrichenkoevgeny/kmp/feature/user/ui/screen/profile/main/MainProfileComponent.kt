package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.main

import com.arkivanov.decompose.value.Value

/**
 * Actions and state for the main profile screen.
 */
interface MainProfileComponent {
    /** Reactive state for the UI. */
    val state: Value<MainProfileScreenState>

    /** Opens the login flow. */
    fun onLoginClick()

    /** Shows logout confirmation dialog. */
    fun onLogoutClick()

    /** Executes logout after confirmation. */
    fun onConfirmLogout()

    /** Navigates to TOTP settings. */
    fun onTotpSettingsClick()

    /** Navigates to the session list. */
    fun onSessionsClick()

    /** Navigates to the identifier list. */
    fun onIdentifiersClick()

    /** Shows account deletion confirmation. */
    fun onDeleteAccountClick()

    /** Executes account deletion after confirmation. */
    fun onConfirmDeleteAccount()

    /** Restores a user account currently scheduled for deletion. */
    fun onRestoreAccountClick()

    /** Hides any active confirmation dialogs. */
    fun onDismissDialog()
}
