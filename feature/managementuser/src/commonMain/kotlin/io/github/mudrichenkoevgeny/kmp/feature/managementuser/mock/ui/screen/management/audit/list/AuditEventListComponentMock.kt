package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.audit.list

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.list.AuditEventListComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.list.AuditEventListScreenState
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventId

@InternalApi
class AuditEventListComponentMock(
    initialState: AuditEventListScreenState = AuditEventListScreenState.Loading
) : AuditEventListComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<AuditEventListScreenState> = _state

    var backCalls = 0
    var refreshCalls = 0
    var loadNextPageCalls = 0
    var toggleFilterPanelCalls = 0
    var lastEventClicked: AuditEventId? = null

    fun updateState(state: AuditEventListScreenState) {
        _state.value = state
    }

    override fun onEventClick(eventId: AuditEventId) {
        lastEventClicked = eventId
    }

    override fun onLoadNextPage() {
        loadNextPageCalls++
    }

    override fun onRefresh() {
        refreshCalls++
    }

    override fun onBackClick() {
        backCalls++
    }

    override fun onToggleFilterPanel() {
        toggleFilterPanelCalls++
    }

    override fun onSortChanged(sortState: ListingSortState) {
        val current = _state.value as? AuditEventListScreenState.Content ?: return
        _state.value = current.copy(sortState = sortState)
    }

    override fun onFilterChanged(filterId: String, filterState: ListingFilterState?) {
        val current = _state.value as? AuditEventListScreenState.Content ?: return
        val newFilters = current.filterStates.toMutableMap()
        if (filterState == null) {
            newFilters.remove(filterId)
        } else {
            newFilters[filterId] = filterState
        }
        _state.value = current.copy(filterStates = newFilters)
    }

    override fun onApplyFilters() {
        val current = _state.value as? AuditEventListScreenState.Content ?: return
        _state.value = current.copy(isFilterPanelExpanded = false)
    }
}