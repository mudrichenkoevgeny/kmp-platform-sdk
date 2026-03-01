package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.registration.email

import com.arkivanov.decompose.value.Value

interface RegistrationByEmailComponent {
    val state: Value<RegistrationByEmailScreenState>

    fun onEmailChanged(email: String)
    fun onSendCodeClick()

    fun onCodeChanged(code: String)
    fun onPasswordChanged(password: String)
    fun onTogglePasswordVisibility()
    fun onRegisterClick()

    fun onBackClick()
}