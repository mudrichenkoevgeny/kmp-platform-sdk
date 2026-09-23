package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.userlist

import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState

interface UserIdentifierListComponent {
    val state: Value<UserIdentifierListScreenState>

    fun onRefresh()
    fun onLoadNextPage()
    fun onBackClick()
    fun onToggleFilterPanel()
    fun onSortChanged(sortState: ListingSortState)
    fun onFilterChanged(filterId: String, filterState: ListingFilterState?)
    fun onApplyFilters()
    fun onDeleteIdentifierClick(identifierId: String)
    fun onDeleteIdentifierPasswordClick(identifierId: String)
}
