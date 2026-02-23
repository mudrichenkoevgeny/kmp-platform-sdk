package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.login.phone

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError

sealed interface LoginByPhoneScreenState {

    data object Loading : LoginByPhoneScreenState

    data class PhoneInput(
        val phoneNumber: String = "",
        val isPhoneNumberValid: Boolean = false,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : LoginByPhoneScreenState

    data class CodeInput(
        val phoneNumber: String,
        val code: String = "",
        val codeLength: Int = 6,
        val resendTimerSeconds: Int = 0,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : LoginByPhoneScreenState {
        val isCodeFullLength: Boolean = code.length == codeLength
        val canResendCode: Boolean = resendTimerSeconds == 0 && !actionLoading
    }
}