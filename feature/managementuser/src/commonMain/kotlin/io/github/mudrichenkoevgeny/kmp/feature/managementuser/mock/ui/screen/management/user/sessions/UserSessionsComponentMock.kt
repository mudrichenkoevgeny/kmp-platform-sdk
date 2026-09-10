package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.sessions

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.sessions.UserSessionsComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.sessions.UserSessionsScreenState

@InternalApi
class UserSessionsComponentMock(
    initialState: UserSessionsScreenState = UserSessionsScreenState.Loading
) : UserSessionsComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<UserSessionsScreenState> = _state

    var refreshCalls = 0
    var loadNextPageCalls = 0
    var backCalls = 0
    var toggleFilterPanelCalls = 0

    fun updateState(state: UserSessionsScreenState) {
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

    override fun onToggleFilterPanel() {
        toggleFilterPanelCalls++
    }

    override fun onSortChanged(sortState: ListingSortState) {
        val current = _state.value as? UserSessionsScreenState.Content ?: return
        _state.value = current.copy(sortState = sortState)
    }

    override fun onFilterChanged(filterId: String, filterState: ListingFilterState?) {
        val current = _state.value as? UserSessionsScreenState.Content ?: return
        val newFilters = current.filterStates.toMutableMap()
        if (filterState == null) {
            newFilters.remove(filterId)
        } else {
            newFilters[filterId] = filterState
        }
        _state.value = current.copy(filterStates = newFilters)
    }

    override fun onApplyFilters() {
        val current = _state.value as? UserSessionsScreenState.Content ?: return
        _state.value = current.copy(isFilterPanelExpanded = false)
    }
}
