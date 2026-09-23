package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.session.detail

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.detail.SessionDetailComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.detail.SessionDetailScreenState

@InternalApi
class SessionDetailComponentMock(
    initialState: SessionDetailScreenState = SessionDetailScreenState.Loading
) : SessionDetailComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<SessionDetailScreenState> = _state

    var revokeSessionCalls = 0
    var retryCalls = 0
    var backCalls = 0

    fun updateState(state: SessionDetailScreenState) {
        _state.value = state
    }

    override fun onRevokeSessionClick() {
        revokeSessionCalls++
    }

    override fun onRetry() {
        retryCalls++
    }

    override fun onBackClick() {
        backCalls++
    }
}
