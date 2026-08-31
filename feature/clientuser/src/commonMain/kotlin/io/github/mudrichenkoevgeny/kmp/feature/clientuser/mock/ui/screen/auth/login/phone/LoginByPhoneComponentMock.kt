package io.github.mudrichenkoevgeny.kmp.feature.clientuser.mock.ui.screen.auth.login.phone

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.phone.LoginByPhoneComponent
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.phone.LoginByPhoneScreenState

@InternalApi
class LoginByPhoneComponentMock(
    initialState: LoginByPhoneScreenState = LoginByPhoneScreenState.PhoneInput()
) : LoginByPhoneComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<LoginByPhoneScreenState> = _state

    var sendCodeCalls = 0
    var confirmCodeCalls = 0
    var resetPhoneCalls = 0
    var backCalls = 0
    var lastPhoneChanged: String? = null
    var lastCodeChanged: String? = null

    fun updateState(state: LoginByPhoneScreenState) {
        _state.value = state
    }

    override fun onPhoneChanged(phone: String) {
        lastPhoneChanged = phone
    }

    override fun onSendCodeClick() {
        sendCodeCalls++
    }

    override fun onCodeChanged(code: String) {
        lastCodeChanged = code
    }

    override fun onConfirmCodeClick() {
        confirmCodeCalls++
    }

    override fun onResetPhoneClick() {
        resetPhoneCalls++
    }

    override fun onBackClick() {
        backCalls++
    }
}
