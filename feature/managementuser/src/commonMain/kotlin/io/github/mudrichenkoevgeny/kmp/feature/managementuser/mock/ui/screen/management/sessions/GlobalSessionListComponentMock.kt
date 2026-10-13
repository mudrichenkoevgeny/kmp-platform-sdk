package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.sessions

import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.MutableValue
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.globallist.GlobalSessionListComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.globallist.GlobalSessionListScreenState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary

@InternalApi
class GlobalSessionListComponentMock(
    initialState: GlobalSessionListScreenState = GlobalSessionListScreenState.Loading
) : GlobalSessionListComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<GlobalSessionListScreenState> get() = _state

    var backCalls = 0
    var refreshCalls = 0
    var sessionClickCalls: MutableList<UserSessionSummary> = mutableListOf()

    fun updateState(newState: GlobalSessionListScreenState) {
        _state.value = newState
    }

    override fun onBackClick() {
        backCalls++
    }

    override fun onRefresh() {
        refreshCalls++
    }

    override fun onLoadNextPage() {}

    override fun onToggleFilterPanel() {}

    override fun onSortChanged(sortState: ListingSortState) {}

    override fun onFilterChanged(filterId: String, filterState: ListingFilterState?) {}

    override fun onApplyFilters() {}

    override fun onSessionClick(session: UserSessionSummary) {
        sessionClickCalls.add(session)
    }

    override fun onSessionRevoked(sessionId: UserSessionId) {}
}