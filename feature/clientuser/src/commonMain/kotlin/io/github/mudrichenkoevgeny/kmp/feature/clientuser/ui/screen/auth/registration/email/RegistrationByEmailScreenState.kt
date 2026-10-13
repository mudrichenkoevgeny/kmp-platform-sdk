package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.feature.user.utils.FieldValidator

/**
 * Unified UI state for the registration by email screen.
 *
 * @param email current email input text.
 * @param isEmailValid whether [email] passes client-side validation.
 * @param code confirmation code input.
 * @param codeLength expected length for the confirmation code.
 * @param password chosen password.
 * @param isPasswordValid whether [password] passes password policy rules.
 * @param isPasswordVisible toggles visibility of the password input.
 * @param resendTimerSeconds seconds until code resend is allowed.
 * @param actionLoading true when an API request is in progress.
 * @param actionError error from the last API request.
 */
data class RegistrationByEmailScreenState(
    val email: String = "",
    val isEmailValid: Boolean = false,
    val code: String = "",
    val codeLength: Int = FieldValidator.DEFAULT_OTP_LENGTH,
    val password: String = "",
    val isPasswordValid: Boolean = true,
    val isPasswordVisible: Boolean = false,
    val resendTimerSeconds: Int = 0,
    val actionLoading: Boolean = false,
    val actionError: AppError? = null
) {
    val canSendCode: Boolean get() = isEmailValid && !actionLoading
    val isCodeFullLength: Boolean get() = code.length == codeLength
    val canRegister: Boolean get() = isCodeFullLength && isPasswordValid && password.isNotEmpty() && !actionLoading
    val canResendCode: Boolean get() = resendTimerSeconds <= 0 && !actionLoading
}
