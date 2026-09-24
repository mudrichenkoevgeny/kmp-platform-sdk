package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.identifiers

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.userlist.UserIdentifierListComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.userlist.UserIdentifierListScreenState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId

@InternalApi
class UserIdentifierListComponentMock(
    initialState: UserIdentifierListScreenState = UserIdentifierListScreenState.Loading
) : UserIdentifierListComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<UserIdentifierListScreenState> = _state

    var refreshCalls = 0
    var identifierClickCalls = mutableListOf<String>()
    var loadNextPageCalls = 0
    var backCalls = 0

    override fun onRefresh() {
        refreshCalls++
    }

    override fun onIdentifierClick(identifierId: String) {
        identifierClickCalls.add(identifierId)
    }

    override fun onIdentifierDeleted(identifierId: UserIdentifierId) {
        val current = _state.value as? UserIdentifierListScreenState.Content ?: return
        val updated = current.paging.items.filterNot { it.id == identifierId }
        _state.value = current.copy(paging = current.paging.copy(items = updated))
    }

    override fun onLoadNextPage() {
        loadNextPageCalls++
    }

    override fun onBackClick() {
        backCalls++
    }
}
