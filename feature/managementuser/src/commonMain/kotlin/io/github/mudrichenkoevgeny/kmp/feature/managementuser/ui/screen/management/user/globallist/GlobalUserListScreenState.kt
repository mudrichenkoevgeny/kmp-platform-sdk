package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.globallist

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserSummary

/**
 * UI state for global platform users list management screen.
 */
sealed interface GlobalUserListScreenState {

    /** Initial loading of users list. */
    object Loading : GlobalUserListScreenState

    /** Critical error during state initialization. */
    data class Error(val error: AppError) : GlobalUserListScreenState

    /**
     * Users list with pagination, sorting, and filtering.
     *
     * @param paging Cumulative state of the paginated list.
     * @param currentUserId Optional ID of the currently logged-in manager account.
     * @param sortState Current sorting option applied.
     * @param filterStates Current filtering options applied.
     * @param isFilterPanelExpanded Whether the filter panel is expanded on the UI.
     * @param actionLoading True when an action is in progress.
     * @param actionError Error from the last action attempt.
     */
    data class Content(
        val paging: PaginationState<UserSummary>,
        val currentUserId: UserId? = null,
        val sortState: ListingSortState? = null,
        val filterStates: Map<String, ListingFilterState> = emptyMap(),
        val isFilterPanelExpanded: Boolean = false,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : GlobalUserListScreenState
}
