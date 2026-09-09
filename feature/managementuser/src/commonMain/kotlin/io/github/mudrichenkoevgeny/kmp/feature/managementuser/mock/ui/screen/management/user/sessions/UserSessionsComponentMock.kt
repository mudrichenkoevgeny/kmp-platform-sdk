package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.sessions

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.sessions.UserSessionsComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.sessions.UserSessionsScreenState

@InternalApi
class UserSessionsComponentMock(
    initialState: UserSessionsScreenState = UserSessionsScreenState.Loading,
) : UserSessionsComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<UserSessionsScreenState> = _state

    var refreshCalls = 0
    var loadNextPageCalls = 0
    var backCalls = 0

    fun updateState(state: UserSessionsScreenState) {
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
}
