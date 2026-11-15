package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.login.pendingdeletion

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.pendingdeletion.PendingDeletionComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.pendingdeletion.PendingDeletionScreenState

/**
 * Mock implementation of [PendingDeletionComponent] for Compose previews and testing.
 */
@InternalApi
public class PendingDeletionComponentMock(
    initialState: PendingDeletionScreenState = PendingDeletionScreenState()
) : PendingDeletionComponent {

    private val _state = MutableValue(initialState)
    public override val state: Value<PendingDeletionScreenState> = _state

    public var restoreAccountCalls: Int = 0
    public var signOutCalls: Int = 0

    /**
     * Updates the component state value.
     *
     * @param state The new state value.
     */
    public fun updateState(state: PendingDeletionScreenState) {
        _state.value = state
    }

    public override fun onRestoreAccountClick() {
        restoreAccountCalls++
    }

    public override fun onSignOutClick() {
        signOutCalls++
    }
}
