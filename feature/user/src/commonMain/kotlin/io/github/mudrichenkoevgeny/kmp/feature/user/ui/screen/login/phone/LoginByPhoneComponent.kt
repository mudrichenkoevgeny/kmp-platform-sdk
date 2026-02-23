package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.login.phone

import com.arkivanov.decompose.value.Value

interface LoginByPhoneComponent {
    val state: Value<LoginByPhoneScreenState>

    fun onPhoneChanged(phone: String)
    fun onCodeChanged(code: String)

    fun onSendCodeClick()
    fun onResetPhoneClick()
    fun onConfirmCodeClick()

    fun onBackClick()
}