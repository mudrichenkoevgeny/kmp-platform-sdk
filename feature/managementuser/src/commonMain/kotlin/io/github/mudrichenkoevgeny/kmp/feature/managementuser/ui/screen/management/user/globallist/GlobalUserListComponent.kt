package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.globallist

import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId

interface GlobalUserListComponent {
    val state: Value<GlobalUserListScreenState>

    fun onRefresh()
    fun onUserClick(userId: UserId)
    fun onCreateUserClick()
    fun onLoadNextPage()
    fun onBackClick()
    fun onToggleFilterPanel()
    fun onSortChanged(sortState: ListingSortState)
    fun onFilterChanged(filterId: String, filterState: ListingFilterState?)
    fun onApplyFilters()
}
