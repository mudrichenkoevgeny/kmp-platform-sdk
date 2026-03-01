package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError

sealed interface LoginByEmailScreenState {

    data object Loading : LoginByEmailScreenState

    data class Content(
        val email: String = "",
        val isEmailValid: Boolean = false,
        val password: String = "",
        val isPasswordValid: Boolean = false,
        val isPasswordVisible: Boolean = false,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : LoginByEmailScreenState {

        val canLogin: Boolean = isEmailValid && isPasswordValid && !actionLoading
    }
}