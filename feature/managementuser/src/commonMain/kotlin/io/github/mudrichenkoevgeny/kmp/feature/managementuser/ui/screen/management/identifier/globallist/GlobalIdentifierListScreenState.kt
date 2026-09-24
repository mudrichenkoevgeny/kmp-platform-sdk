package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.globallist

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier

/**
 * UI state for global user identifiers list management screen.
 */
sealed interface GlobalIdentifierListScreenState {

    /** Initial loading of identifiers. */
    data object Loading : GlobalIdentifierListScreenState

    /** Critical error during state initialization. */
    data class Error(val error: AppError) : GlobalIdentifierListScreenState

    /**
     * Global identifiers list with pagination, filtering, and sorting.
     *
     * @param paging cumulative state of the paginated list.
     * @param sortState current sorting option applied.
     * @param filterStates current filtering options applied.
     * @param isFilterPanelExpanded whether the filter panel is expanded on the UI.
     * @param actionLoading true when an action is in progress.
     * @param actionError error from the last action attempt.
     */
    data class Content(
        val paging: PaginationState<UserIdentifier>,
        val sortState: ListingSortState? = null,
        val filterStates: Map<String, ListingFilterState> = emptyMap(),
        val isFilterPanelExpanded: Boolean = false,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : GlobalIdentifierListScreenState
}
