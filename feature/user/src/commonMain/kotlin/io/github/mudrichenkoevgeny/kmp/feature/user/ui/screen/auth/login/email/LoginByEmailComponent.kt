package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email

import com.arkivanov.decompose.value.Value

interface LoginByEmailComponent {
    val state: Value<LoginByEmailScreenState>

    fun onEmailChanged(email: String)
    fun onPasswordChanged(password: String)
    fun onTogglePasswordVisibility()

    fun onLoginClick()

    fun onForgotPasswordClick()
    fun onRegistrationClick()

    fun onBackClick()
}