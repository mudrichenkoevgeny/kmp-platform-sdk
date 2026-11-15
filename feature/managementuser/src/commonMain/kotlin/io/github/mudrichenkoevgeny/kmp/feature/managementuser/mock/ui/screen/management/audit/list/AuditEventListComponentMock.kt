package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.audit.list

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.list.AuditEventListComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.list.AuditEventListScreenState
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventId

/**
 * Mock implementation of [AuditEventListComponent] for testing.
 */
@InternalApi
public class AuditEventListComponentMock(
    initialState: AuditEventListScreenState = AuditEventListScreenState.Loading
) : AuditEventListComponent {

    private val _state = MutableValue(initialState)
    public override val state: Value<AuditEventListScreenState> = _state

    public var backCalls: Int = 0
    public var refreshCalls: Int = 0
    public var loadNextPageCalls: Int = 0
    public var toggleFilterPanelCalls: Int = 0
    public var lastEventClicked: AuditEventId? = null

    /** Updates the current screen state. */
    public fun updateState(state: AuditEventListScreenState) {
        _state.value = state
    }

    public override fun onEventClick(eventId: AuditEventId) {
        lastEventClicked = eventId
    }

    public override fun onLoadNextPage() {
        loadNextPageCalls++
    }

    public override fun onRefresh() {
        refreshCalls++
    }

    public override fun onBackClick() {
        backCalls++
    }

    public override fun onToggleFilterPanel() {
        toggleFilterPanelCalls++
    }

    public override fun onSortChanged(sortState: ListingSortState) {
        val current = _state.value as? AuditEventListScreenState.Content ?: return
        _state.value = current.copy(sortState = sortState)
    }

    public override fun onFilterChanged(
        filterId: String,
        filterState: ListingFilterState?
    ) {
        val current = _state.value as? AuditEventListScreenState.Content ?: return
        val newFilters = current.filterStates.toMutableMap()
        if (filterState == null) {
            newFilters.remove(filterId)
        } else {
            newFilters[filterId] = filterState
        }
        _state.value = current.copy(filterStates = newFilters)
    }

    public override fun onApplyFilters() {
        val current = _state.value as? AuditEventListScreenState.Content ?: return
        _state.value = current.copy(isFilterPanelExpanded = false)
    }
}
