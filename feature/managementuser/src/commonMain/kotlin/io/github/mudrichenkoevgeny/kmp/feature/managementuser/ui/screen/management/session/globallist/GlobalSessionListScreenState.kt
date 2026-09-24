package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.globallist

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession

/**
 * UI state for global sessions list management screen.
 */
sealed interface GlobalSessionListScreenState {

    /** Initial loading of sessions. */
    data object Loading : GlobalSessionListScreenState

    /** Critical error during state initialization. */
    data class Error(val error: AppError) : GlobalSessionListScreenState

    /**
     * Global sessions list with pagination, filtering, and sorting.
     *
     * @param paging cumulative state of the paginated list.
     * @param sortState current sorting option applied.
     * @param filterStates current filtering options applied.
     * @param isFilterPanelExpanded whether the filter panel is expanded on the UI.
     * @param actionLoading true when an action is in progress.
     * @param actionError error from the last action attempt.
     */
    data class Content(
        val paging: PaginationState<UserSession>,
        val sortState: ListingSortState? = null,
        val filterStates: Map<String, ListingFilterState> = emptyMap(),
        val isFilterPanelExpanded: Boolean = false,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : GlobalSessionListScreenState
}
