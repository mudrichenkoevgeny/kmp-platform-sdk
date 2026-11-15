package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.main

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.globallist.GlobalUserListComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.globallist.GlobalUserListScreenState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId

/**
 * Mock implementation of [GlobalUserListComponent] for testing.
 */
@InternalApi
public class GlobalUserListComponentMock(
    initialState: GlobalUserListScreenState = GlobalUserListScreenState.Loading
) : GlobalUserListComponent {

    private val _state = MutableValue(initialState)
    public override val state: Value<GlobalUserListScreenState> = _state

    public var refreshCalls: Int = 0
    public var createUserCalls: Int = 0
    public var loadNextPageCalls: Int = 0
    public var backCalls: Int = 0
    public var toggleFilterPanelCalls: Int = 0
    public var lastUserClicked: UserId? = null

    /** Updates the current screen state. */
    public fun updateState(state: GlobalUserListScreenState) {
        _state.value = state
    }

    public override fun onRefresh() {
        refreshCalls++
    }

    public override fun onUserDeleted(userId: UserId) {
        val current = _state.value as? GlobalUserListScreenState.Content ?: return
        val updatedItems = current.paging.items.filterNot { it.id == userId }
        _state.value = current.copy(paging = current.paging.copy(items = updatedItems))
    }

    public override fun onUserCreated() {
        onRefresh()
    }

    public override fun onUserClick(userId: UserId) {
        lastUserClicked = userId
    }

    public override fun onCreateUserClick() {
        createUserCalls++
    }

    public override fun onLoadNextPage() {
        loadNextPageCalls++
    }

    public override fun onBackClick() {
        backCalls++
    }

    public override fun onToggleFilterPanel() {
        toggleFilterPanelCalls++
    }

    public override fun onSortChanged(sortState: ListingSortState) {
        val current = _state.value as? GlobalUserListScreenState.Content ?: return
        _state.value = current.copy(sortState = sortState)
    }

    public override fun onFilterChanged(
        filterId: String,
        filterState: ListingFilterState?
    ) {
        val current = _state.value as? GlobalUserListScreenState.Content ?: return
        val newFilters = current.filterStates.toMutableMap()
        if (filterState == null) {
            newFilters.remove(filterId)
        } else {
            newFilters[filterId] = filterState
        }
        _state.value = current.copy(filterStates = newFilters)
    }

    public override fun onApplyFilters() {
        val current = _state.value as? GlobalUserListScreenState.Content ?: return
        _state.value = current.copy(isFilterPanelExpanded = false)
    }
}
