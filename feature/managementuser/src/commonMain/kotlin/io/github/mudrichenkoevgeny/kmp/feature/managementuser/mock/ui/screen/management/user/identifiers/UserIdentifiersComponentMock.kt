package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.identifiers

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.identifiers.UserIdentifiersComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.identifiers.UserIdentifiersScreenState

@InternalApi
class UserIdentifiersComponentMock(
    initialState: UserIdentifiersScreenState = UserIdentifiersScreenState.Loading,
) : UserIdentifiersComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<UserIdentifiersScreenState> = _state

    var refreshCalls = 0
    var loadNextPageCalls = 0
    var backCalls = 0

    fun updateState(state: UserIdentifiersScreenState) {
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
