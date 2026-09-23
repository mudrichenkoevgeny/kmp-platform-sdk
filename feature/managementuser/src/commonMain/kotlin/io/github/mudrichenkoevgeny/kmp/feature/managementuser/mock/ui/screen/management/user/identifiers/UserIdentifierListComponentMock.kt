package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.identifiers

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.userlist.UserIdentifierListComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.userlist.UserIdentifierListScreenState

@InternalApi
class UserIdentifierListComponentMock(
    initialState: UserIdentifierListScreenState = UserIdentifierListScreenState.Loading
) : UserIdentifierListComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<UserIdentifierListScreenState> = _state

    var refreshCalls = 0
    var loadNextPageCalls = 0
    var backCalls = 0
    var toggleFilterPanelCalls = 0
    var deleteIdentifierCalls = 0
    var deleteIdentifierPasswordCalls = 0

    fun updateState(state: UserIdentifierListScreenState) {
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
        val current = _state.value as? UserIdentifierListScreenState.Content ?: return
        _state.value = current.copy(sortState = sortState)
    }

    override fun onFilterChanged(filterId: String, filterState: ListingFilterState?) {
        val current = _state.value as? UserIdentifierListScreenState.Content ?: return
        val newFilters = current.filterStates.toMutableMap()
        if (filterState == null) {
            newFilters.remove(filterId)
        } else {
            newFilters[filterId] = filterState
        }
        _state.value = current.copy(filterStates = newFilters)
    }

    override fun onApplyFilters() {
        val current = _state.value as? UserIdentifierListScreenState.Content ?: return
        _state.value = current.copy(isFilterPanelExpanded = false)
    }

    override fun onDeleteIdentifierClick(identifierId: String) {
        deleteIdentifierCalls++
    }

    override fun onDeleteIdentifierPasswordClick(identifierId: String) {
        deleteIdentifierPasswordCalls++
    }
}
