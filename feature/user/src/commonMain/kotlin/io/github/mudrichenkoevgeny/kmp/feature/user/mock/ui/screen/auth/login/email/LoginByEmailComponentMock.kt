package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.login.email

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email.LoginByEmailComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email.LoginByEmailScreenState

/**
 * Mock implementation of [LoginByEmailComponent] for Compose previews and testing.
 */
@InternalApi
public class LoginByEmailComponentMock(
    initialState: LoginByEmailScreenState = LoginByEmailScreenState.Content()
) : LoginByEmailComponent {

    private val _state = MutableValue(initialState)
    public override val state: Value<LoginByEmailScreenState> = _state

    public var loginCalls: Int = 0
    public var registrationCalls: Int = 0
    public var forgotPasswordCalls: Int = 0
    public var backCalls: Int = 0
    public var togglePasswordVisibilityCalls: Int = 0
    public var lastEmailChanged: String? = null
    public var lastPasswordChanged: String? = null

    /**
     * Updates the component state value.
     *
     * @param state The new state value.
     */
    public fun updateState(state: LoginByEmailScreenState) {
        _state.value = state
    }

    public override fun onEmailChanged(email: String) {
        lastEmailChanged = email
    }

    public override fun onPasswordChanged(password: String) {
        lastPasswordChanged = password
    }

    public override fun onTogglePasswordVisibility() {
        togglePasswordVisibilityCalls++
    }

    public override fun onLoginClick() {
        loginCalls++
    }

    public override fun onRegistrationClick() {
        registrationCalls++
    }

    public override fun onForgotPasswordClick() {
        forgotPasswordCalls++
    }

    public override fun onBackClick() {
        backCalls++
    }
}
