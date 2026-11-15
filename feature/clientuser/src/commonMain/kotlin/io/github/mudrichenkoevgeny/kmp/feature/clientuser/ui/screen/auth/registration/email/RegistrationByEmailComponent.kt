package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.value.Value

/**
 * Registration by email flow: request code, enter code and password, handles timers and submission.
 */
public interface RegistrationByEmailComponent {
    /**
     * Decompose navigation stack driving the registration wizard steps.
     */
    public val stack: Value<ChildStack<RegistrationByEmailStepConfig, Child>>

    /**
     * Stack navigator for the registration wizard.
     */
    public val stackNavigation: StackNavigation<RegistrationByEmailStepConfig>

    /**
     * Current UI state for [RegistrationByEmailScreen].
     */
    public val state: Value<RegistrationByEmailScreenState>

    /**
     * Child instances corresponding to each wizard step.
     */
    public sealed interface Child {
        public data class EmailInput(public val initialEmail: String) : Child
        public data class ConfirmInput(public val email: String) : Child
    }

    /**
     * Updates the email field; may auto-advance to password/code step if a cooldown is active.
     *
     * @param email new email text.
     */
    public fun onEmailChanged(email: String)

    /** Requests a registration confirmation code for the current email. */
    public fun onSendCodeClick()

    /**
     * Updates the confirmation code field.
     *
     * @param code new code text.
     */
    public fun onCodeChanged(code: String)

    /**
     * Updates the password field; triggers real-time password policy validation.
     *
     * @param password new password text.
     */
    public fun onPasswordChanged(password: String)

    /** Toggles password field masking. */
    public fun onTogglePasswordVisibility()

    /** Submits email, password, and code to create the account. */
    public fun onRegisterClick()

    /**
     * Pops the screen or returns from registration input to email input.
     */
    public fun onBackClick()
}
