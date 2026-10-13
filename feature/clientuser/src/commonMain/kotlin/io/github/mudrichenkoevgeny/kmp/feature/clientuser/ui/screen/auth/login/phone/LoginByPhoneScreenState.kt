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
data class LoginByPhoneScreenState(
    val phoneNumber: String = "",
    val isPhoneNumberValid: Boolean = false,
    val code: String = "",
    val codeLength: Int = FieldValidator.DEFAULT_OTP_LENGTH,
    val resendTimerSeconds: Int = 0,
    val actionLoading: Boolean = false,
    val actionError: AppError? = null
) {
    val canSendCode: Boolean get() = isPhoneNumberValid && !actionLoading
    val isCodeFullLength: Boolean get() = code.length == codeLength
    val canConfirmCode: Boolean get() = isCodeFullLength && !actionLoading
    val canResendCode: Boolean get() = resendTimerSeconds <= 0 && !actionLoading
}
