package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.phone

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError

/**
 * UI states for [LoginByPhoneScreen].
 */
sealed interface LoginByPhoneScreenState {
    val actionLoading: Boolean
    val actionError: AppError?

    /**
     * Initial step: enter the phone number and request a code.
     *
     * @param phoneNumber current input value.
     * @param isPhoneNumberValid whether the phone meets client-side format rules.
     */
    data class PhoneInput(
        val phoneNumber: String = "",
        val isPhoneNumberValid: Boolean = false,
        override val actionLoading: Boolean = false,
        override val actionError: AppError? = null
    ) : LoginByPhoneScreenState {
        val canSendCode: Boolean get() = isPhoneNumberValid && !actionLoading
    }

    /**
     * Second step: enter the OTP code received via SMS/Phone.
     *
     * @param phoneNumber the number confirmed in the first step.
     * @param code current OTP input value.
     * @param codeLength required length to enable submission (usually 6).
     * @param resendTimerSeconds remaining cooldown before another code can be requested.
     */
    data class CodeInput(
        val phoneNumber: String,
        val code: String = "",
        val codeLength: Int = 6,
        val resendTimerSeconds: Int = 0,
        override val actionLoading: Boolean = false,
        override val actionError: AppError? = null
    ) : LoginByPhoneScreenState {
        val isCodeFullLength: Boolean get() = code.length == codeLength
        val canConfirmCode: Boolean get() = isCodeFullLength && !actionLoading
        val canResendCode: Boolean get() = resendTimerSeconds <= 0 && !actionLoading
    }
}
