package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.session.detail

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.detail.SessionDetailComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.detail.SessionDetailScreenState

/**
 * Mock implementation of [SessionDetailComponent] for Compose previews and testing.
 */
@InternalApi
public class SessionDetailComponentMock(
    initialState: SessionDetailScreenState = SessionDetailScreenState.Loading
) : SessionDetailComponent {

    private val _state: MutableValue<SessionDetailScreenState> = MutableValue(initialState)
    public override val state: Value<SessionDetailScreenState> = _state

    public var revokeSessionCalls: Int = 0
    public var retryCalls: Int = 0
    public var backCalls: Int = 0
    public var userClickCalls: Int = 0

    /**
     * Updates the component state value.
     *
     * @param state The new state value.
     */
    public fun updateState(state: SessionDetailScreenState) {
        _state.value = state
    }

    public override fun onRevokeSessionClick() {
        revokeSessionCalls++
    }

    public override fun onRetry() {
        retryCalls++
    }

    public override fun onBackClick() {
        backCalls++
    }

    public override fun onIdentifierClick(): Unit = Unit

    public override fun onUserClick() {
        userClickCalls++
    }
}
