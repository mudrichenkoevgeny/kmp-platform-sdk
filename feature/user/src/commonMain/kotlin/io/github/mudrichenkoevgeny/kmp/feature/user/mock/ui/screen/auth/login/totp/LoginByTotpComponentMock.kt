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
public class LoginByTotpComponentMock(
    initialState: LoginByTotpScreenState = LoginByTotpScreenState.Content(mfaToken = "mock_token")
) : LoginByTotpComponent {

    private val _state = MutableValue(initialState)
    public override val state: Value<LoginByTotpScreenState> = _state

    public var submitCalls: Int = 0
    public var toggleModeCalls: Int = 0
    public var backCalls: Int = 0
    public var lastCodeChanged: String? = null

    /**
     * Updates the component state value.
     *
     * @param state The new state value.
     */
    public fun updateState(state: LoginByTotpScreenState) {
        _state.value = state
    }

    public override fun onCodeChanged(code: String) {
        lastCodeChanged = code
    }

    public override fun onToggleModeClick() {
        toggleModeCalls++
    }

    public override fun onSubmitClick() {
        submitCalls++
    }

    public override fun onBackClick() {
        backCalls++
    }
}
