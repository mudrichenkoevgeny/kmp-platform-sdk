package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.resetpassword

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.value.Value

/**
 * Password reset by email: send reset code, then confirm with code and new password.
 */
public interface ResetEmailPasswordComponent {
    /**
     * Decompose navigation stack driving the wizard steps.
     */
    public val stack: Value<ChildStack<ResetPasswordStepConfig, Child>>

    /**
     * Stack navigator for the password reset wizard.
     */
    public val stackNavigation: StackNavigation<ResetPasswordStepConfig>

    /**
     * Current UI state for [ResetEmailPasswordScreen].
     */
    public val state: Value<ResetEmailPasswordScreenState>

    /**
     * Child instances created for each step configuration.
     */
    public sealed interface Child {
        /** Email input step instance. */
        public data class EmailInput(val initialEmail: String) : Child

        /** Code & new password confirmation step instance. */
        public data class ConfirmInput(val email: String) : Child
    }

    /**
     * Updates email; may advance to reset input if a cooldown still applies.
     *
     * @param email new email text.
     */
    public fun onEmailChanged(email: String)

    /**
     * Updates confirmation code.
     *
     * @param code new code text.
     */
    public fun onCodeChanged(code: String)

    /**
     * Updates new password and asynchronously revalidates minimum length.
     *
     * @param password new password text.
     */
    public fun onPasswordChanged(password: String)

    /**
     * Sends or re-sends the reset code (from email step or resend from reset step).
     */
    public fun onSendCodeClick()

    /** Clears the reset step and returns to email input, keeping the typed email when possible. */
    public fun onResetEmailClick()

    /** Submits email, code, and new password to complete the reset. */
    public fun onConfirmResetClick()

    /**
     * From reset input, returns to email step; from email step, pops the parent stack.
     */
    public fun onBackClick()
}
