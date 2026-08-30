package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email

import com.arkivanov.decompose.value.Value

/**
 * Registration by email flow: request code, enter code and password, handles timers and submission.
 */
interface RegistrationByEmailComponent {
    /**
     * Current UI state for [RegistrationByEmailScreen].
     *
     * @return reactive [RegistrationByEmailScreenState].
     */
    val state: Value<RegistrationByEmailScreenState>

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
