package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession

/**
 * UI state for the active sessions management.
 */
sealed interface SessionListScreenState {

    /** Initial loading of sessions. */
    data object Loading : SessionListScreenState

    /**
     * Active sessions list with pagination.
     *
     * @param paging cumulative state of the paginated list.
     * @param actionLoading true when a session is being revoked.
     * @param actionError error from the last revocation attempt.
     */
    data class Content(
        val paging: PaginationState<UserSession>,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : SessionListScreenState

    /** Critical error during state initialization. */
    data class Error(val error: AppError) : SessionListScreenState
}
