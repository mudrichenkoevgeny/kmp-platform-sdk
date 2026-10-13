package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.resetpassword

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.feature.user.utils.FieldValidator

/**
 * Unified UI state for the email-based password reset component.
 *
 * @param email current email text.
 * @param isEmailValid whether the email passes client-side validation.
 * @param code confirmation code input.
 * @param newPassword new password to set after verification.
 * @param isPasswordValid whether the new password passes minimum length rules.
 * @param codeLength expected code length (default six).
 * @param resendTimerSeconds seconds until resend is allowed.
 * @param actionLoading while API requests are running.
 * @param actionError error from API requests or validation.
 */
data class ResetEmailPasswordScreenState(
    val email: String = "",
    val isEmailValid: Boolean = false,
    val code: String = "",
    val newPassword: String = "",
    val isPasswordValid: Boolean = false,
    val codeLength: Int = FieldValidator.DEFAULT_OTP_LENGTH,
    val resendTimerSeconds: Int = 0,
    val actionLoading: Boolean = false,
    val actionError: AppError? = null
) {
    val isCodeFullLength: Boolean = code.length == codeLength
    val canConfirm: Boolean = isCodeFullLength && isPasswordValid && !actionLoading
    val canResendCode: Boolean = resendTimerSeconds <= 0 && !actionLoading
}