package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.detail

import com.arkivanov.decompose.value.Value

/**
 * Manages the user identifier detail screen lifecycle: loading identifier info and performing actions.
 */
interface IdentifierDetailComponent {
    /** Reactive UI state. */
    val state: Value<IdentifierDetailScreenState>

    /** Shows delete confirmation dialog. */
    fun onDeleteIdentifierRequested()

    /** Dismisses delete confirmation dialog. */
    fun onDismissDeleteIdentifierDialog()

    /** Deletes the current displayed identifier if permitted. */
    fun onDeleteIdentifierClick()

    /** Opens change password dialog. */
    fun onChangePasswordClick()

    /** Confirms password change. */
    fun onConfirmChangePasswordClick(oldPassword: String, newPassword: String)

    /** Dismisses change password dialog. */
    fun onDismissChangePasswordDialog()

    /** Deletes the password credential. */
    fun onDeletePasswordClick()

    /** Retries loading the identifier details on error. */
    fun onRetry()

    /** Navigates back. */
    fun onBackClick()

    /** Navigates to user profile or user detail screen depending on current user. */
    fun onUserClick()
}
