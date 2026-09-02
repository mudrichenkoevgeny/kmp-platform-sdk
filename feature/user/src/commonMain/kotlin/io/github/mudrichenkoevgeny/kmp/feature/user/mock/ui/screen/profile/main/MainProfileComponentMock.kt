package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.main

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.main.MainProfileComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.main.MainProfileScreenState

@InternalApi
class MainProfileComponentMock(
    initialState: MainProfileScreenState = MainProfileScreenState.Loading
) : MainProfileComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<MainProfileScreenState> = _state

    var loginCalls = 0
    var logoutCalls = 0
    var totpSettingsCalls = 0
    var sessionsCalls = 0
    var identifiersCalls = 0
    var deleteAccountCalls = 0

    fun updateState(state: MainProfileScreenState) {
        _state.value = state
    }

    override fun onLoginClick() {
        loginCalls++
    }

    override fun onLogoutClick() {
        logoutCalls++
    }

    override fun onTotpSettingsClick() {
        totpSettingsCalls++
    }

    override fun onSessionsClick() {
        sessionsCalls++
    }

    override fun onIdentifiersClick() {
        identifiersCalls++
    }

    override fun onDeleteAccountClick() {
        deleteAccountCalls++
    }

    override fun onConfirmDeleteAccount() {}

    override fun onDismissDialog() {}
}