package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.session

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.SessionListComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.SessionListScreenState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary

@InternalApi
class SessionListComponentMock(
    initialState: SessionListScreenState = SessionListScreenState.Loading,
    override val isRevokeAvailable: Boolean = true,
    override val isRevokeAllOthersAvailable: Boolean = true,
    override val includeFilterPanel: Boolean = false
) : SessionListComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<SessionListScreenState> = _state

    var refreshCalls: Int = 0
    var sessionClickCalls: MutableList<UserSessionSummary> = mutableListOf()
    var sessionRevokedCalls: MutableList<UserSessionId> = mutableListOf()
    var revokeSessionCalls: MutableList<UserSessionId> = mutableListOf()
    var revokeAllOtherSessionsCalls: Int = 0
    var loadNextPageCalls: Int = 0
    var backCalls: Int = 0
    var toggleFilterPanelCalls: Int = 0
    var applyFiltersCalls: Int = 0

    fun updateState(state: SessionListScreenState) {
        _state.value = state
    }

    override fun onRefresh() {
        refreshCalls++
    }

    override fun onLoadNextPage() {
        loadNextPageCalls++
    }

    override fun onToggleFilterPanel() {
        toggleFilterPanelCalls++
    }

    override fun onSortChanged(sortState: ListingSortState) {
    }

    override fun onFilterChanged(filterId: String, filterState: ListingFilterState?) {
    }

    override fun onApplyFilters() {
        applyFiltersCalls++
    }

    override fun onSessionClick(session: UserSessionSummary) {
        sessionClickCalls.add(session)
    }

    override fun onSessionRevoked(sessionId: UserSessionId) {
        sessionRevokedCalls.add(sessionId)
    }

    override fun onRevokeSessionClick(sessionId: UserSessionId) {
        revokeSessionCalls.add(sessionId)
    }

    override fun onRevokeAllOtherSessionsClick() {
        revokeAllOtherSessionsCalls++
    }

    override fun onBackClick() {
        backCalls++
    }
}
