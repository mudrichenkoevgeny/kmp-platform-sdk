package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.login.welcome

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome.LoginWelcomeComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome.LoginWelcomeScreenState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider

@InternalApi
class LoginWelcomeComponentMock(
    initialState: LoginWelcomeScreenState = LoginWelcomeScreenState.Loading
) : LoginWelcomeComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<LoginWelcomeScreenState> = _state

    var retryInitCalls = 0
    var privacyPolicyCalls = 0
    var termsOfServiceCalls = 0
    val loginClicks = mutableListOf<UserAuthProvider>()

    fun updateState(state: LoginWelcomeScreenState) {
        _state.value = state
    }

    override fun onRetryInitClick() {
        retryInitCalls++
    }

    override fun onLoginClick(authProvider: UserAuthProvider) {
        loginClicks.add(authProvider)
    }

    override fun onPrivacyPolicyClick() {
        privacyPolicyCalls++
    }

    override fun onTermsOfServiceClick() {
        termsOfServiceCalls++
    }
}
