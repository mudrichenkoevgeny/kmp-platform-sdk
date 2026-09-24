package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.globallist

import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.list.SessionListOwner
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId

/**
 * Manages all active sessions globally: listing with filters/sort and revoking specific sessions.
 */
interface GlobalSessionListComponent : SessionListOwner {
    /** Reactive UI state. */
    val state: Value<GlobalSessionListScreenState>

    /** Refreshes the global session list. */
    fun onRefresh()
    
    /** Requests the next page of sessions if available. */
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
    
    /** Navigates to session detail screen for the specified session. */
    fun onSessionClick(session: UserSession)
    
    /** Revokes a specific session. */
    fun onDeleteSessionClick(userId: UserId, sessionId: String)
}
