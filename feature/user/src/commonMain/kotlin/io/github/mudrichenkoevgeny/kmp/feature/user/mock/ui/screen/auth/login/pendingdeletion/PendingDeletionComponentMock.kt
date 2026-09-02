package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.login.pendingdeletion

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.pendingdeletion.PendingDeletionComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.pendingdeletion.PendingDeletionScreenState

@InternalApi
class PendingDeletionComponentMock(
    initialState: PendingDeletionScreenState = PendingDeletionScreenState()
) : PendingDeletionComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<PendingDeletionScreenState> = _state

    var restoreAccountCalls: Int = 0
    var signOutCalls: Int = 0

    fun updateState(state: PendingDeletionScreenState) {
        _state.value = state
    }

    override fun onRestoreAccountClick() {
        restoreAccountCalls++
    }

    override fun onSignOutClick() {
        signOutCalls++
    }
}
