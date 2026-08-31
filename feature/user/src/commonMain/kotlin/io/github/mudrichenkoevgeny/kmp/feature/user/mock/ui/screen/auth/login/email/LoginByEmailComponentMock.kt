package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.login.email

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email.LoginByEmailComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email.LoginByEmailScreenState

@InternalApi
class LoginByEmailComponentMock(
    initialState: LoginByEmailScreenState = LoginByEmailScreenState.Content()
) : LoginByEmailComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<LoginByEmailScreenState> = _state

    var loginCalls = 0
    var registrationCalls = 0
    var forgotPasswordCalls = 0
    var backCalls = 0
    var togglePasswordVisibilityCalls = 0
    var lastEmailChanged: String? = null
    var lastPasswordChanged: String? = null

    fun updateState(state: LoginByEmailScreenState) {
        _state.value = state
    }

    override fun onEmailChanged(email: String) {
        lastEmailChanged = email
    }

    override fun onPasswordChanged(password: String) {
        lastPasswordChanged = password
    }

    override fun onTogglePasswordVisibility() {
        togglePasswordVisibilityCalls++
    }

    override fun onLoginClick() {
        loginCalls++
    }

    override fun onRegistrationClick() {
        registrationCalls++
    }

    override fun onForgotPasswordClick() {
        forgotPasswordCalls++
    }

    override fun onBackClick() {
        backCalls++
    }
}
