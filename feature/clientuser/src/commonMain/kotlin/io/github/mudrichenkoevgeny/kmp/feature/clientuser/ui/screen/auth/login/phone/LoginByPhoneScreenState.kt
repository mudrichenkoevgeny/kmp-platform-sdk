package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.phone

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.feature.user.utils.FieldValidator

/**
 * Unified UI state for [LoginByPhoneScreen].
 *
 * @param phoneNumber current phone number input value.
 * @param isPhoneNumberValid whether the phone meets client-side format rules.
 * @param code current OTP input value.
 * @param codeLength required length to enable submission (usually 6).
 * @param resendTimerSeconds remaining cooldown before another code can be requested.
 * @param actionLoading true when an API action is running.
 * @param actionError error from the last API action.
 */
public data class LoginByPhoneScreenState(
    public val phoneNumber: String = "",
    public val isPhoneNumberValid: Boolean = false,
    public val code: String = "",
    public val codeLength: Int = FieldValidator.DEFAULT_OTP_LENGTH,
    public val resendTimerSeconds: Int = 0,
    public val actionLoading: Boolean = false,
    public val actionError: AppError? = null
) {
    public val canSendCode: Boolean get() = isPhoneNumberValid && !actionLoading
    public val isCodeFullLength: Boolean get() = code.length == codeLength
    public val canConfirmCode: Boolean get() = isCodeFullLength && !actionLoading
    public val canResendCode: Boolean get() = resendTimerSeconds <= 0 && !actionLoading
}
