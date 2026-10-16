package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.main

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.main.MainUserComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.main.MainUserScreenState

@InternalApi
open class MainUserComponentMock(
    initialState: MainUserScreenState = MainUserScreenState.Loading
) : MainUserComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<MainUserScreenState> = _state

    var retryCalls = 0
    var totpMainCalls = 0
    var sessionsCalls = 0
    var identifiersCalls = 0
    var deleteUserCalls = 0
    var confirmDeleteUserCalls = 0
    var dismissDeleteUserDialogCalls = 0
    var logoutCalls = 0
    var loginCalls = 0

    fun setState(newState: MainUserScreenState) {
        _state.value = newState
    }

    override fun onRetry() {
        retryCalls++
    }

    override fun onTotpMainClick() {
        totpMainCalls++
    }

    override fun onSessionsClick() {
        sessionsCalls++
    }

    override fun onIdentifiersClick() {
        identifiersCalls++
    }

    override fun onDeleteUserClick() {
        deleteUserCalls++
    }

    override fun onConfirmDeleteUserClick() {
        confirmDeleteUserCalls++
    }

    override fun onDismissDeleteUserDialog() {
        dismissDeleteUserDialogCalls++
    }

    override fun onLogoutClick() {
        logoutCalls++
    }

    override fun onLoginClick() {
        loginCalls++
    }
}
