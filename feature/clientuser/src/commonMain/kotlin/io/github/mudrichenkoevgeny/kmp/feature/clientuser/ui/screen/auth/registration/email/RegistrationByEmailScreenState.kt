package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError

/**
 * UI states for [RegistrationByEmailScreen].
 */
sealed interface RegistrationByEmailScreenState {
    val actionLoading: Boolean
    val actionError: AppError?

    /**
     * Initial step: enter the email and request a code.
     */
    data class EmailInput(
        val email: String = "",
        val isEmailValid: Boolean = false,
        override val actionLoading: Boolean = false,
        override val actionError: AppError? = null
    ) : RegistrationByEmailScreenState {
        val canSendCode: Boolean get() = isEmailValid && !actionLoading
    }

    /**
     * Second step: enter the OTP code and chosen password.
     */
    data class RegistrationInput(
        val email: String,
        val code: String = "",
        val codeLength: Int = 6,
        val password: String = "",
        val isPasswordValid: Boolean = true,
        val isPasswordVisible: Boolean = false,
        val resendTimerSeconds: Int = 0,
        override val actionLoading: Boolean = false,
        override val actionError: AppError? = null
    ) : RegistrationByEmailScreenState {
        val isCodeFullLength: Boolean get() = code.length == codeLength
        val canRegister: Boolean get() = isCodeFullLength && isPasswordValid && password.isNotEmpty() && !actionLoading
        val canResendCode: Boolean get() = resendTimerSeconds <= 0 && !actionLoading
    }
}
