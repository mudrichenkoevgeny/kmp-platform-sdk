package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.list

import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventId

/**
 * Manages the audit events listing lifecycle and navigation.
 */
public interface AuditEventListComponent {
    /** Reactive UI state. */
    public val state: Value<AuditEventListScreenState>

    /** Refreshes the audit events list. */
    public fun onRefresh()

    /** Navigates to a specific audit event detail view. */
    public fun onEventClick(eventId: AuditEventId)

    /** Requests the next page of audit events if available. */
    public fun onLoadNextPage()

    /** Navigates back. */
    public fun onBackClick()

    /** Toggles the filter/sort panel visibility. */
    public fun onToggleFilterPanel()

    /** Updates current sort state and reloads events. */
    public fun onSortChanged(sortState: ListingSortState)

    /** Updates current filter state and reloads events. */
    public fun onFilterChanged(
        filterId: String,
        filterState: ListingFilterState?
    )

    /** Applies current filters and reloads events. */
    public fun onApplyFilters()
}
