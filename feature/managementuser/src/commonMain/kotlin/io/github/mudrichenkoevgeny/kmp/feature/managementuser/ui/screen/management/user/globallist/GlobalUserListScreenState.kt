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
public sealed interface GlobalUserListScreenState {

    /** Initial loading of users list. */
    public object Loading : GlobalUserListScreenState

    /** Critical error during state initialization. */
    public data class Error(public val error: AppError) : GlobalUserListScreenState

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
    public data class Content(
        public val paging: PaginationState<UserSummary>,
        public val currentUserId: UserId? = null,
        public val sortState: ListingSortState? = null,
        public val filterStates: Map<String, ListingFilterState> = emptyMap(),
        public val isFilterPanelExpanded: Boolean = false,
        public val actionLoading: Boolean = false,
        public val actionError: AppError? = null
    ) : GlobalUserListScreenState
}
