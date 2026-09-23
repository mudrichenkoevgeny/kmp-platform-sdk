package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.userlist

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier

sealed interface UserIdentifierListScreenState {
    object Loading : UserIdentifierListScreenState
    data class Error(val error: AppError) : UserIdentifierListScreenState
    data class Content(
        val paging: PaginationState<UserIdentifier>,
        val sortState: ListingSortState? = null,
        val filterStates: Map<String, ListingFilterState> = emptyMap(),
        val isFilterPanelExpanded: Boolean = false,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : UserIdentifierListScreenState
}
