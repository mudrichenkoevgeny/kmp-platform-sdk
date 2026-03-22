package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.registration.email

import com.arkivanov.decompose.value.Value

/**
 * Email registration: send verification code, then complete account with code and password.
 */
interface RegistrationByEmailComponent {
    /**
     * Current UI state for [RegistrationByEmailScreen].
     *
     * @return reactive [RegistrationByEmailScreenState].
     */
    val state: Value<RegistrationByEmailScreenState>

    /**
     * Updates email; may skip to code step if a cooldown still applies for this address.
     *
     * @param email new email text.
     */
    fun onEmailChanged(email: String)

    /** Sends or re-sends the registration confirmation code to the current email. */
    fun onSendCodeClick()

    /**
     * Updates the confirmation code field.
     *
     * @param code new code text.
     */
    fun onCodeChanged(code: String)

    /**
     * Updates password and asynchronously revalidates minimum length.
     *
     * @param password new password text.
     */
    fun onPasswordChanged(password: String)

    /** Toggles password visibility on the registration step. */
    fun onTogglePasswordVisibility()

    /** Validates password and submits registration; on success the parent flow completes. */
    fun onRegisterClick()

    /**
     * From the code step, returns to email input preserving email; from email step, pops the parent stack.
     */
    fun onBackClick()
}