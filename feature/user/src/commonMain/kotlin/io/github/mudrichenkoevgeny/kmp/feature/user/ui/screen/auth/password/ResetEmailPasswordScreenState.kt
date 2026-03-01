package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.password

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError

sealed interface ResetEmailPasswordScreenState {

    data object Loading : ResetEmailPasswordScreenState

    data class EmailInput(
        val email: String = "",
        val isEmailValid: Boolean = false,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : ResetEmailPasswordScreenState

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