package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.resetpassword

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.value.Value

/**
 * Password reset by email: send reset code, then confirm with code and new password.
 */
interface ResetEmailPasswordComponent {
    /**
     * Decompose navigation stack driving the wizard steps.
     */
    val stack: Value<ChildStack<ResetPasswordStepConfig, Child>>

    /**
     * Stack navigator for the password reset wizard.
     */
    val stackNavigation: StackNavigation<ResetPasswordStepConfig>

    /**
     * Current UI state for [ResetEmailPasswordScreen].
     */
    val state: Value<ResetEmailPasswordScreenState>

    /**
     * Child instances created for each step configuration.
     */
    sealed interface Child {
        /** Email input step instance. */
        data class EmailInput(val initialEmail: String) : Child

        /** Code & new password confirmation step instance. */
        data class ConfirmInput(val email: String) : Child
    }

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
