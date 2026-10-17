package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.globallist

import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId

/**
 * Manages all platform users globally: paginated listing with filters/sort and navigation to user detail/create screens.
 */
interface GlobalUserListComponent : UserListOwner {

    /** Reactive UI state. */
    val state: Value<GlobalUserListScreenState>

    /** Refreshes the user list. */
    fun onRefresh()

    /**
     * Navigates to user detail screen for the specified user.
     *
     * @param userId Target user ID.
     */
    fun onUserClick(userId: UserId)

    /** Navigates to user creation form screen. */
    fun onCreateUserClick()

    /** Requests the next page of users if available. */
    fun onLoadNextPage()

    /** Navigates back. */
    fun onBackClick()

    /** Toggles the filter/sort panel visibility. */
    fun onToggleFilterPanel()

    /**
     * Applies a new sort state.
     *
     * @param sortState New sort option applied.
     */
    fun onSortChanged(sortState: ListingSortState)

    /**
     * Applies a new filter state for a specific filter ID.
     *
     * @param filterId ID of the target filter field.
     * @param filterState New filter value, or null to clear the filter.
     */
    fun onFilterChanged(filterId: String, filterState: ListingFilterState?)

    /** Confirms and applies current filters and sort options. */
    fun onApplyFilters()
}
