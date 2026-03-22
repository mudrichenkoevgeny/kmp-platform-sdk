package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.password

import com.arkivanov.decompose.value.Value

/**
 * Password reset by email: send reset code, then confirm with code and new password.
 */
interface ResetEmailPasswordComponent {
    /**
     * Current UI state for [ResetEmailPasswordScreen].
     *
     * @return reactive [ResetEmailPasswordScreenState].
     */
    val state: Value<ResetEmailPasswordScreenState>

    /**
     * Updates email; may advance to reset input if a cooldown still applies.
     *
     * @param email new email text.
     */
    fun onEmailChanged(email: String)

    /**
     * Updates confirmation code.
     *
     * @param code new code text.
     */
    fun onCodeChanged(code: String)

    /**
     * Updates new password and asynchronously revalidates minimum length.
     *
     * @param password new password text.
     */
    fun onPasswordChanged(password: String)

    /**
     * Sends or re-sends the reset code (from email step or resend from reset step).
     */
    fun onSendCodeClick()

    /** Clears the reset step and returns to email input, keeping the typed email when possible. */
    fun onResetEmailClick()

    /** Submits email, code, and new password to complete the reset. */
    fun onConfirmResetClick()

    /**
     * From reset input, returns to email step; from email step, pops the parent stack.
     */
    fun onBackClick()
}