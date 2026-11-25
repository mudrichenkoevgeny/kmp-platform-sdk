package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.globallist

import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserId

/**
 * Manages all platform users globally: paginated listing with filters/sort and navigation to user detail/create screens.
 */
public interface GlobalUserListComponent : UserListOwner {

    /** Reactive UI state. */
    public val state: Value<GlobalUserListScreenState>

    /** Refreshes the user list. */
    public fun onRefresh()

    /**
     * Navigates to user detail screen for the specified user.
     *
     * @param userId Target user ID.
     */
    public fun onUserClick(userId: UserId)

    /** Navigates to user creation form screen. */
    public fun onCreateUserClick()

    /** Requests the next page of users if available. */
    public fun onLoadNextPage()

    /** Navigates back. */
    public fun onBackClick()

    /** Toggles the filter/sort panel visibility. */
    public fun onToggleFilterPanel()

    /**
     * Applies a new sort state.
     *
     * @param sortState New sort option applied.
     */
    public fun onSortChanged(sortState: ListingSortState)

    /**
     * Applies a new filter state for a specific filter ID.
     *
     * @param filterId ID of the target filter field.
     * @param filterState New filter value, or null to clear the filter.
     */
    public fun onFilterChanged(
        filterId: String,
        filterState: ListingFilterState?
    )

    /** Confirms and applies current filters and sort options. */
    public fun onApplyFilters()
}
