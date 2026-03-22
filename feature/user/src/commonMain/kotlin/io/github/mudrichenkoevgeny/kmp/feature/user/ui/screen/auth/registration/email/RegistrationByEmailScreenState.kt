package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.registration.email

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError

/**
 * UI state for email registration: send code to email, then enter code and password with resend timer.
 */
sealed interface RegistrationByEmailScreenState {

    /** Reserved loading shell; the implementation currently starts in [EmailInput]. */
    data object Loading : RegistrationByEmailScreenState

    /**
     * User enters email and requests a confirmation code.
     *
     * @param email current email text.
     * @param isEmailValid whether the email passes client-side validation.
     * @param actionLoading while the send-code request runs.
     * @param actionError error from send-code or rate limiting.
     */
    data class EmailInput(
        val email: String = "",
        val isEmailValid: Boolean = false,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : RegistrationByEmailScreenState

    /**
     * User enters confirmation code and password; optional resend countdown for a new code.
     *
     * @param email email address being registered.
     * @param code confirmation code input.
     * @param codeLength expected code length (default six).
     * @param resendTimerSeconds seconds until resend is allowed.
     * @param password chosen password.
     * @param isPasswordValid whether the password passes minimum length rules.
     * @param isPasswordVisible password field visibility toggle.
     * @param actionLoading while registration request runs.
     * @param actionError error from registration or validation.
     */
    data class RegistrationInput(
        val email: String,
        val code: String = "",
        val codeLength: Int = 6,
        val resendTimerSeconds: Int = 0,
        val password: String = "",
        val isPasswordValid: Boolean = false,
        val isPasswordVisible: Boolean = false,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : RegistrationByEmailScreenState {

        val isCodeFullLength: Boolean = code.length == codeLength

        val canResendCode: Boolean = resendTimerSeconds == 0 && !actionLoading

        val canRegister: Boolean = isCodeFullLength && isPasswordValid && !actionLoading
    }
}