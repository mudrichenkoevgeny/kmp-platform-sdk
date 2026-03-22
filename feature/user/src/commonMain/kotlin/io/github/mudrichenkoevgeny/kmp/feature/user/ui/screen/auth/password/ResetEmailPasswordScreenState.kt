package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.password

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError

/**
 * UI state for email-based password reset: request code for email, then enter code and new password.
 */
sealed interface ResetEmailPasswordScreenState {

    /** Reserved loading shell; the implementation currently starts in [EmailInput]. */
    data object Loading : ResetEmailPasswordScreenState

    /**
     * User enters account email and requests a reset code.
     *
     * @param email current email text.
     * @param isEmailValid whether the email passes client-side validation.
     * @param actionLoading while send-code runs.
     * @param actionError error from send-code or rate limiting.
     */
    data class EmailInput(
        val email: String = "",
        val isEmailValid: Boolean = false,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : ResetEmailPasswordScreenState

    /**
     * User enters confirmation code and new password; optional resend countdown.
     *
     * @param email account email receiving the code.
     * @param code confirmation code input.
     * @param newPassword new password to set after verification.
     * @param isPasswordValid whether the new password passes minimum length rules.
     * @param codeLength expected code length (default six).
     * @param resendTimerSeconds seconds until resend is allowed.
     * @param actionLoading while reset request runs.
     * @param actionError error from reset or validation.
     */
    data class ResetInput(
        val email: String,
        val code: String = "",
        val newPassword: String = "",
        val isPasswordValid: Boolean = false,
        val codeLength: Int = 6,
        val resendTimerSeconds: Int = 0,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : ResetEmailPasswordScreenState {
        val isCodeFullLength: Boolean = code.length == codeLength
        val canConfirm: Boolean = isCodeFullLength && isPasswordValid && !actionLoading
        val canResendCode: Boolean = resendTimerSeconds == 0 && !actionLoading
    }
}