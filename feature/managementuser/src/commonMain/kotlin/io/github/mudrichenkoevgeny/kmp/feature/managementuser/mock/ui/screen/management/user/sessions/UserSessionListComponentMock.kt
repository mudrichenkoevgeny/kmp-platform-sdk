package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.sessions

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.userlist.UserSessionListComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.userlist.UserSessionListScreenState

@InternalApi
class UserSessionListComponentMock(
    initialState: UserSessionListScreenState = UserSessionListScreenState.Loading
) : UserSessionListComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<UserSessionListScreenState> = _state

    var refreshCalls = 0
    var loadNextPageCalls = 0
    var backCalls = 0
    var toggleFilterPanelCalls = 0
    var deleteSessionCalls = 0
    var deleteAllSessionsCalls = 0

    fun updateState(state: UserSessionListScreenState) {
        _state.value = state
    }

    override fun onRefresh() {
        refreshCalls++
    }

    override fun onLoadNextPage() {
        loadNextPageCalls++
    }

    override fun onBackClick() {
        backCalls++
    }

    override fun onDeleteSessionClick(sessionId: String) {
        deleteSessionCalls++
    }

    override fun onDeleteAllSessionsClick() {
        deleteAllSessionsCalls++
    }
}
