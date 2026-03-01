package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.password

import com.arkivanov.decompose.value.Value

interface ResetEmailPasswordComponent {
    val state: Value<ResetEmailPasswordScreenState>

    fun onEmailChanged(email: String)
    fun onCodeChanged(code: String)
    fun onPasswordChanged(password: String)

    fun onSendCodeClick()
    fun onResetEmailClick()
    fun onConfirmResetClick()

    fun onBackClick()
}