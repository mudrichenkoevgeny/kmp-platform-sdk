package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.main

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.main.UsersManagementMainComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.main.UsersManagementMainScreenState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId

@InternalApi
class UsersManagementMainComponentMock(
    initialState: UsersManagementMainScreenState = UsersManagementMainScreenState.Loading,
) : UsersManagementMainComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<UsersManagementMainScreenState> = _state

    var refreshCalls = 0
    var createUserCalls = 0
    var loadNextPageCalls = 0
    var backCalls = 0
    var toggleFilterPanelCalls = 0
    var lastUserClicked: UserId? = null

    fun updateState(state: UsersManagementMainScreenState) {
        _state.value = state
    }

    override fun onRefresh() {
        refreshCalls++
    }

    override fun onUserClick(userId: UserId) {
        lastUserClicked = userId
    }

    override fun onCreateUserClick() {
        createUserCalls++
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
        val current = _state.value as? UsersManagementMainScreenState.Content ?: return
        _state.value = current.copy(sortState = sortState)
    }

    override fun onFilterChanged(filterId: String, filterState: ListingFilterState?) {
        val current = _state.value as? UsersManagementMainScreenState.Content ?: return
        val newFilters = current.filterStates.toMutableMap()
        if (filterState == null) {
            newFilters.remove(filterId)
        } else {
            newFilters[filterId] = filterState
        }
        _state.value = current.copy(filterStates = newFilters)
    }

    override fun onApplyFilters() {
        val current = _state.value as? UsersManagementMainScreenState.Content ?: return
        _state.value = current.copy(isFilterPanelExpanded = false)
    }
}
