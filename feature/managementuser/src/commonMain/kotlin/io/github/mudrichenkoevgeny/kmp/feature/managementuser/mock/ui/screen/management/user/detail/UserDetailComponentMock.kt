package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.detail

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail.UserDetailComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail.UserDetailScreenState

@InternalApi
class UserDetailComponentMock(
    initialState: UserDetailScreenState = UserDetailScreenState.Loading,
) : UserDetailComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<UserDetailScreenState> = _state

    var updateCalls = 0
    var deleteCalls = 0
    var sessionsCalls = 0
    var identifiersCalls = 0
    var retryCalls = 0
    var backCalls = 0
    var lastAuthorityLevelChanged: String? = null
    var lastAccountStatusChanged: String? = null

    fun updateState(state: UserDetailScreenState) {
        _state.value = state
    }

    override fun onAuthorityLevelChanged(value: String) {
        lastAuthorityLevelChanged = value
    }

    override fun onAccountStatusChanged(value: String) {
        lastAccountStatusChanged = value
    }

    override fun onUpdateClick() {
        updateCalls++
    }

    override fun onDeleteClick() {
        deleteCalls++
    }

    override fun onSessionsClick() {
        sessionsCalls++
    }

    override fun onIdentifiersClick() {
        identifiersCalls++
    }

    override fun onRetry() {
        retryCalls++
    }

    override fun onBackClick() {
        backCalls++
    }
}
