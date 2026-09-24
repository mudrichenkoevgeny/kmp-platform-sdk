package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.identifiers

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.globallist.GlobalIdentifierListComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.globallist.GlobalIdentifierListScreenState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId

@InternalApi
class GlobalIdentifierListComponentMock(
    initialState: GlobalIdentifierListScreenState = GlobalIdentifierListScreenState.Loading
) : GlobalIdentifierListComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<GlobalIdentifierListScreenState> = _state

    var refreshCalls = 0
    var loadNextPageCalls = 0
    var backCalls = 0
    var toggleFilterPanelCalls = 0
    var identifierClickCalls = mutableListOf<String>()

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
        val current = _state.value as? GlobalIdentifierListScreenState.Content ?: return
        _state.value = current.copy(sortState = sortState)
    }

    override fun onFilterChanged(filterId: String, filterState: ListingFilterState?) {
        val current = _state.value as? GlobalIdentifierListScreenState.Content ?: return
        val newFilters = current.filterStates.toMutableMap()
        if (filterState == null) {
            newFilters.remove(filterId)
        } else {
            newFilters[filterId] = filterState
        }
        _state.value = current.copy(filterStates = newFilters)
    }

    override fun onApplyFilters() {
        val current = _state.value as? GlobalIdentifierListScreenState.Content ?: return
        _state.value = current.copy(isFilterPanelExpanded = false)
    }

    override fun onIdentifierClick(identifierId: String) {
        identifierClickCalls.add(identifierId)
    }

    override fun onIdentifierDeleted(identifierId: UserIdentifierId) {
        val current = _state.value as? GlobalIdentifierListScreenState.Content ?: return
        val updated = current.paging.items.filterNot { it.id == identifierId }
        _state.value = current.copy(paging = current.paging.copy(items = updated))
    }
}
