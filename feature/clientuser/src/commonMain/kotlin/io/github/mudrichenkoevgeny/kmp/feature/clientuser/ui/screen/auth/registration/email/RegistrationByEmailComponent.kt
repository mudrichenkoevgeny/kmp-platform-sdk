package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.value.Value

/**
 * Registration by email flow: request code, enter code and password, handles timers and submission.
 */
interface RegistrationByEmailComponent {
    /**
     * Decompose navigation stack driving the registration wizard steps.
     */
    val stack: Value<ChildStack<RegistrationByEmailStepConfig, Child>>

    /**
     * Stack navigator for the registration wizard.
     */
    val stackNavigation: StackNavigation<RegistrationByEmailStepConfig>

    /**
     * Current UI state for [RegistrationByEmailScreen].
     */
    val state: Value<RegistrationByEmailScreenState>

    /**
     * Child instances corresponding to each wizard step.
     */
    sealed interface Child {
        data class EmailInput(val initialEmail: String) : Child
        data class ConfirmInput(val email: String) : Child
    }

    /**
     * Updates the email field; may auto-advance to password/code step if a cooldown is active.
     *
     * @param email new email text.
     */
    fun onEmailChanged(email: String)

    /** Requests a registration confirmation code for the current email. */
    fun onSendCodeClick()

    /**
     * Updates the confirmation code field.
     *
     * @param code new code text.
     */
    fun onCodeChanged(code: String)

    /**
     * Updates the password field; triggers real-time password policy validation.
     *
     * @param password new password text.
     */
    fun onPasswordChanged(password: String)

    /** Toggles password field masking. */
    fun onTogglePasswordVisibility()

    /** Submits email, password, and code to create the account. */
    fun onRegisterClick()

    /**
     * Pops the screen or returns from registration input to email input.
     */
    fun onBackClick()
}
