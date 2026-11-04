package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId

/**
 * Universal screen state hierarchy for session list screens (client and management).
 */
sealed interface SessionListScreenState {

    /** Initial loading state. */
    object Loading : SessionListScreenState

    /**
     * Global error state.
     *
     * @property error The application error that occurred during initialization or fetch.
     */
    data class Error(val error: AppError) : SessionListScreenState

    /**
     * Active content state displaying paginated sessions with optional filter/sort options.
     *
     * @property paging Current pagination state of sessions.
     * @property currentSessionId Active session ID if resolved.
     * @property currentUserId Current authenticated user ID if resolved.
     * @property isFilterPanelExpanded Whether the filter options panel is currently open.
     * @property sortState Active sort option.
     * @property filterStates Map of active filter IDs to filter states.
     * @property actionLoading Whether an async action (such as session revocation) is in progress.
     * @property actionError Error resulting from an async action attempt.
     */
    data class Content(
        val paging: PaginationState<UserSessionSummary> = PaginationState(),
        val currentSessionId: UserSessionId? = null,
        val currentUserId: UserId? = null,
        val isFilterPanelExpanded: Boolean = false,
        val sortState: ListingSortState? = null,
        val filterStates: Map<String, ListingFilterState> = emptyMap(),
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : SessionListScreenState
}
