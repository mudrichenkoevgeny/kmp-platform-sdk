package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.registration.email

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError

sealed interface RegistrationByEmailScreenState {

    data object Loading : RegistrationByEmailScreenState

    data class EmailInput(
        val email: String = "",
        val isEmailValid: Boolean = false,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : RegistrationByEmailScreenState

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