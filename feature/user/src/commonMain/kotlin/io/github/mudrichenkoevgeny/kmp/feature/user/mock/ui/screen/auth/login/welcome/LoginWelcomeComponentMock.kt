package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.login.welcome

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome.LoginWelcomeComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome.LoginWelcomeScreenState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider

/**
 * Mock implementation of [LoginWelcomeComponent] for Compose previews and testing.
 */
@InternalApi
public class LoginWelcomeComponentMock(
    initialState: LoginWelcomeScreenState = LoginWelcomeScreenState.Loading
) : LoginWelcomeComponent {

    private val _state = MutableValue(initialState)
    public override val state: Value<LoginWelcomeScreenState> = _state

    public var retryInitCalls: Int = 0
    public var privacyPolicyCalls: Int = 0
    public var termsOfServiceCalls: Int = 0
    public val loginClicks: MutableList<UserAuthProvider> = mutableListOf()

    /**
     * Updates the component state value.
     *
     * @param state The new state value.
     */
    public fun updateState(state: LoginWelcomeScreenState) {
        _state.value = state
    }

    public override fun onRetryInitClick() {
        retryInitCalls++
    }

    public override fun onLoginClick(authProvider: UserAuthProvider) {
        loginClicks.add(authProvider)
    }

    public override fun onPrivacyPolicyClick() {
        privacyPolicyCalls++
    }

    public override fun onTermsOfServiceClick() {
        termsOfServiceCalls++
    }
}
