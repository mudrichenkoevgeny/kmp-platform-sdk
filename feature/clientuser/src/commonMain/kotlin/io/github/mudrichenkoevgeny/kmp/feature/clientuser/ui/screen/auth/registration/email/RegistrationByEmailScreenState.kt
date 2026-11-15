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
public data class RegistrationByEmailScreenState(
    public val email: String = "",
    public val isEmailValid: Boolean = false,
    public val code: String = "",
    public val codeLength: Int = FieldValidator.DEFAULT_OTP_LENGTH,
    public val password: String = "",
    public val isPasswordValid: Boolean = true,
    public val isPasswordVisible: Boolean = false,
    public val resendTimerSeconds: Int = 0,
    public val actionLoading: Boolean = false,
    public val actionError: AppError? = null
) {
    public val canSendCode: Boolean get() = isEmailValid && !actionLoading
    public val isCodeFullLength: Boolean get() = code.length == codeLength
    public val canRegister: Boolean
        get() = isCodeFullLength &&
            isPasswordValid &&
            password.isNotEmpty() &&
            !actionLoading
    public val canResendCode: Boolean get() = resendTimerSeconds <= 0 && !actionLoading
}
