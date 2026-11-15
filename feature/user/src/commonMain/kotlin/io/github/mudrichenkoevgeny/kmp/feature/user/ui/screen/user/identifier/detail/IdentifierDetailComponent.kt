package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.detail

import com.arkivanov.decompose.value.Value

/**
 * Manages the user identifier detail screen lifecycle: loading identifier info and performing actions.
 */
public interface IdentifierDetailComponent {
    /** Reactive UI state. */
    public val state: Value<IdentifierDetailScreenState>

    /** Shows delete confirmation dialog. */
    public fun onDeleteIdentifierRequested()

    /** Dismisses delete confirmation dialog. */
    public fun onDismissDeleteIdentifierDialog()

    /** Deletes the current displayed identifier if permitted. */
    public fun onDeleteIdentifierClick()

    /** Opens change password dialog. */
    public fun onChangePasswordClick()

    /** Confirms password change. */
    public fun onConfirmChangePasswordClick(
        oldPassword: String,
        newPassword: String
    )

    /** Dismisses change password dialog. */
    public fun onDismissChangePasswordDialog()

    /** Deletes the password credential. */
    public fun onDeletePasswordClick()

    /** Retries loading the identifier details on error. */
    public fun onRetry()

    /** Navigates back. */
    public fun onBackClick()

    /** Navigates to user detail screen. */
    public fun onUserClick()
}
