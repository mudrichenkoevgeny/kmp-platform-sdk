package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.session

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.SessionListComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.SessionListScreenState
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary

/**
 * Mock implementation of [SessionListComponent] for Compose previews and testing.
 */
@InternalApi
public class SessionListComponentMock(
    initialState: SessionListScreenState = SessionListScreenState.Loading,
    public override val isRevokeAvailable: Boolean = true,
    public override val isRevokeAllOthersAvailable: Boolean = true,
    public override val includeFilterPanel: Boolean = false
) : SessionListComponent {

    private val _state: MutableValue<SessionListScreenState> = MutableValue(initialState)
    public override val state: Value<SessionListScreenState> = _state

    public var refreshCalls: Int = 0
    public var sessionClickCalls: MutableList<UserSessionSummary> = mutableListOf()
    public var sessionRevokedCalls: MutableList<UserSessionId> = mutableListOf()
    public var revokeSessionCalls: MutableList<UserSessionId> = mutableListOf()
    public var revokeAllOtherSessionsCalls: Int = 0
    public var loadNextPageCalls: Int = 0
    public var backCalls: Int = 0
    public var toggleFilterPanelCalls: Int = 0
    public var applyFiltersCalls: Int = 0

    /**
     * Updates the component state value.
     *
     * @param state The new state value.
     */
    public fun updateState(state: SessionListScreenState) {
        _state.value = state
    }

    public override fun onRefresh() {
        refreshCalls++
    }

    public override fun onLoadNextPage() {
        loadNextPageCalls++
    }

    public override fun onToggleFilterPanel() {
        toggleFilterPanelCalls++
    }

    public override fun onSortChanged(sortState: ListingSortState): Unit = Unit

    public override fun onFilterChanged(
        filterId: String,
        filterState: ListingFilterState?
    ): Unit = Unit

    public override fun onApplyFilters() {
        applyFiltersCalls++
    }

    public override fun onSessionClick(session: UserSessionSummary) {
        sessionClickCalls.add(session)
    }

    public override fun onSessionRevoked(sessionId: UserSessionId) {
        sessionRevokedCalls.add(sessionId)
    }

    public override fun onRevokeSessionClick(sessionId: UserSessionId) {
        revokeSessionCalls.add(sessionId)
    }

    public override fun onRevokeAllOtherSessionsClick() {
        revokeAllOtherSessionsCalls++
    }

    public override fun onBackClick() {
        backCalls++
    }
}
