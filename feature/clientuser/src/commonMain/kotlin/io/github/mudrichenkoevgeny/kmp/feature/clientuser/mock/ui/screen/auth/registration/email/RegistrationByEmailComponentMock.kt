package io.github.mudrichenkoevgeny.kmp.feature.clientuser.mock.ui.screen.auth.registration.email

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email.RegistrationByEmailComponent
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email.RegistrationByEmailScreenState

@InternalApi
class RegistrationByEmailComponentMock(
    initialState: RegistrationByEmailScreenState = RegistrationByEmailScreenState.EmailInput()
) : RegistrationByEmailComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<RegistrationByEmailScreenState> = _state

    var sendCodeCalls = 0
    var registerCalls = 0
    var backCalls = 0
    var togglePasswordVisibilityCalls = 0
    var lastEmailChanged: String? = null
    var lastCodeChanged: String? = null
    var lastPasswordChanged: String? = null

    fun updateState(state: RegistrationByEmailScreenState) {
        _state.value = state
    }

    override fun onEmailChanged(email: String) {
        lastEmailChanged = email
    }

    override fun onSendCodeClick() {
        sendCodeCalls++
    }

    override fun onCodeChanged(code: String) {
        lastCodeChanged = code
    }

    override fun onPasswordChanged(password: String) {
        lastPasswordChanged = password
    }

    override fun onTogglePasswordVisibility() {
        togglePasswordVisibilityCalls++
    }

    override fun onRegisterClick() {
        registerCalls++
    }

    override fun onBackClick() {
        backCalls++
    }
}
