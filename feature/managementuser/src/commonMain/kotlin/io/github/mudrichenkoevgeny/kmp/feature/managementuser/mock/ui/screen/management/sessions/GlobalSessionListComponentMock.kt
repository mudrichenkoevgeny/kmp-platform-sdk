package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.sessions

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.globallist.GlobalSessionListComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.globallist.GlobalSessionListScreenState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId

class GlobalSessionListComponentMock(
    initialState: GlobalSessionListScreenState = GlobalSessionListScreenState.Loading
) : GlobalSessionListComponent {
    private val _state = MutableValue(initialState)
    override val state: Value<GlobalSessionListScreenState> = _state

    var refreshCalls: Int = 0
    var sessionClickCalls: MutableList<UserSession> = mutableListOf()
    var sessionRevokedCalls: MutableList<UserSessionId> = mutableListOf()
    var loadNextPageCalls: Int = 0
    var backCalls: Int = 0
    var toggleFilterPanelCalls: Int = 0

    override fun onRefresh() {
        refreshCalls++
    }

    override fun onLoadNextPage() {
        loadNextPageCalls++
    }

    override fun onBackClick() {
        backCalls++
    }

    override fun onToggleFilterPanel() {
        toggleFilterPanelCalls++
    }

    override fun onSortChanged(sortState: ListingSortState) {}
    override fun onFilterChanged(filterId: String, filterState: ListingFilterState?) {}
    override fun onApplyFilters() {}
    override fun onSessionClick(session: UserSession) {
        sessionClickCalls.add(session)
    }
    override fun onSessionRevoked(sessionId: UserSessionId) {
        sessionRevokedCalls.add(sessionId)
    }
    override fun onDeleteSessionClick(userId: UserId, sessionId: String) {}
}
