package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.session

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.SessionListComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.SessionListScreenState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId

@InternalApi
class SessionListComponentMock(
    initialState: SessionListScreenState = SessionListScreenState.Loading
) : SessionListComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<SessionListScreenState> = _state

    var refreshCalls: Int = 0
    var revokeSessionCalls: MutableList<UserSessionId> = mutableListOf()
    var revokeAllOtherSessionsCalls: Int = 0
    var loadNextPageCalls: Int = 0
    var backCalls: Int = 0

    fun updateState(state: SessionListScreenState) {
        _state.value = state
    }

    override fun onRefresh() {
        refreshCalls++
    }

    override fun onRevokeSessionClick(sessionId: UserSessionId) {
        revokeSessionCalls.add(sessionId)
    }

    override fun onRevokeAllOtherSessionsClick() {
        revokeAllOtherSessionsCalls++
    }

    override fun onLoadNextPage() {
        loadNextPageCalls++
    }

    override fun onBackClick() {
        backCalls++
    }
}
