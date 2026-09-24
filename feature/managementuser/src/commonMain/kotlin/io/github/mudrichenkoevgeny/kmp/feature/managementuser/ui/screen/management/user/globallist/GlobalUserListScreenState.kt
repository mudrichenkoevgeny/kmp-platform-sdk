package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.globallist

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails

sealed interface GlobalUserListScreenState {
    object Loading : GlobalUserListScreenState
    data class Error(val error: AppError) : GlobalUserListScreenState
    data class Content(
        val paging: PaginationState<UserDetails>,
        val sortState: ListingSortState? = null,
        val filterStates: Map<String, ListingFilterState> = emptyMap(),
        val isFilterPanelExpanded: Boolean = false,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : GlobalUserListScreenState
}
