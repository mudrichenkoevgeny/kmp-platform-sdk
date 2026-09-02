package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.login.totp

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.totp.LoginByTotpComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.totp.LoginByTotpScreenState

/**
 * Mock [LoginByTotpComponent] for Compose previews and testing.
 */
@InternalApi
class LoginByTotpComponentMock(
    initialState: LoginByTotpScreenState = LoginByTotpScreenState.Content(mfaToken = "mock_token")
) : LoginByTotpComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<LoginByTotpScreenState> = _state

    var submitCalls = 0
    var toggleModeCalls = 0
    var backCalls = 0
    var lastCodeChanged: String? = null

    fun updateState(state: LoginByTotpScreenState) {
        _state.value = state
    }

    override fun onCodeChanged(code: String) {
        lastCodeChanged = code
    }

    override fun onToggleModeClick() {
        toggleModeCalls++
    }

    override fun onSubmitClick() {
        submitCalls++
    }

    override fun onBackClick() {
        backCalls++
    }
}