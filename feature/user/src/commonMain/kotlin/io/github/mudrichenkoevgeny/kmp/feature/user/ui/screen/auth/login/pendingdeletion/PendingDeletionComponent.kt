package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.pendingdeletion

import com.arkivanov.decompose.value.Value

/**
 * Component for managing the pending deletion state when logging in with a scheduled-for-deletion account.
 */
interface PendingDeletionComponent {
    /** Reactive UI state. */
    val state: Value<PendingDeletionScreenState>

    /** Restores the user account and proceeds into the app. */
    fun onRestoreAccountClick()

    /** Signs out the current user and returns to the login entry point. */
    fun onSignOutClick()
}
