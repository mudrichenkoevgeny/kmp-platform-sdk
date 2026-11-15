package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.main

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.main.MainUserComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.main.MainUserScreenState

/**
 * Mock implementation of [MainUserComponent] for Compose previews and testing.
 */
@InternalApi
public open class MainUserComponentMock(
    initialState: MainUserScreenState = MainUserScreenState.Loading
) : MainUserComponent {

    private val _state: MutableValue<MainUserScreenState> = MutableValue(initialState)
    public override val state: Value<MainUserScreenState> = _state

    public var retryCalls: Int = 0
    public var totpMainCalls: Int = 0
    public var editUserCalls: Int = 0
    public var sessionsCalls: Int = 0
    public var identifiersCalls: Int = 0
    public var filesCalls: Int = 0
    public var deleteUserCalls: Int = 0
    public var confirmDeleteUserCalls: Int = 0
    public var dismissDeleteUserDialogCalls: Int = 0
    public var logoutCalls: Int = 0
    public var confirmLogoutCalls: Int = 0
    public var dismissLogoutDialogCalls: Int = 0
    public var loginCalls: Int = 0

    /**
     * Updates the component state value.
     *
     * @param newState The new state value.
     */
    public fun setState(newState: MainUserScreenState) {
        _state.value = newState
    }

    public override fun onRetry() {
        retryCalls++
    }

    public override fun onTotpMainClick() {
        totpMainCalls++
    }

    public override fun onEditUserClick() {
        editUserCalls++
    }

    public override fun onSessionsClick() {
        sessionsCalls++
    }

    public override fun onIdentifiersClick() {
        identifiersCalls++
    }

    public override fun onFilesListClick() {
        filesCalls++
    }

    public override fun onDeleteUserClick() {
        deleteUserCalls++
    }

    public override fun onConfirmDeleteUserClick() {
        confirmDeleteUserCalls++
    }

    public override fun onDismissDeleteUserDialog() {
        dismissDeleteUserDialogCalls++
    }

    public override fun onLogoutClick() {
        logoutCalls++
    }

    public override fun onConfirmLogoutClick() {
        confirmLogoutCalls++
    }

    public override fun onDismissLogoutDialog() {
        dismissLogoutDialogCalls++
    }

    public override fun onLoginClick() {
        loginCalls++
    }
}
