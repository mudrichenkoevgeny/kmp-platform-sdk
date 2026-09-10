package io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.events

import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventId

/**
 * Manages the audit events listing lifecycle and navigation.
 */
interface AuditEventsComponent {
    /** Reactive UI state. */
    val state: Value<AuditEventsScreenState>

    /** Refreshes the audit events list. */
    fun onRefresh()

    /** Navigates to a specific audit event detail view. */
    fun onEventClick(eventId: AuditEventId)

    /** Requests the next page of audit events if available. */
    fun onLoadNextPage()

    /** Navigates back. */
    fun onBackClick()

    /** Toggles the filter/sort panel visibility. */
    fun onToggleFilterPanel()

    /** Updates current sort state and reloads events. */
    fun onSortChanged(sortState: ListingSortState)

    /** Updates current filter state and reloads events. */
    fun onFilterChanged(filterId: String, filterState: ListingFilterState?)

    /** Applies current filters and reloads events. */
    fun onApplyFilters()
}
