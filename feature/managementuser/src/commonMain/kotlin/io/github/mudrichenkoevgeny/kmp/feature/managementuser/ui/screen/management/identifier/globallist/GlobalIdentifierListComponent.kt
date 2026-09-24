package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.globallist

import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.list.IdentifierListOwner

/**
 * Manages all user identifiers globally: listing with filters/sort and navigation to detail screen.
 */
interface GlobalIdentifierListComponent : IdentifierListOwner {
    /** Reactive UI state. */
    val state: Value<GlobalIdentifierListScreenState>

    /** Refreshes the identifier list. */
    fun onRefresh()
    
    /** Requests the next page of identifiers if available. */
    fun onLoadNextPage()
    
    /** Navigates back. */
    fun onBackClick()
    
    /** Toggles the filter/sort panel visibility. */
    fun onToggleFilterPanel()
    
    /** Applies a new sort state. */
    fun onSortChanged(sortState: ListingSortState)
    
    /** Applies a new filter state for a specific filter ID. */
    fun onFilterChanged(filterId: String, filterState: ListingFilterState?)
    
    /** Confirms and applies current filters and sort options. */
    fun onApplyFilters()
    
    /** Navigates to identifier detail screen. */
    fun onIdentifierClick(identifierId: String)
}
