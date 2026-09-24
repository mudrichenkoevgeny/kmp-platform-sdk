package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.userlist

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession

/**
 * UI state for a specific user's active sessions management screen.
 */
sealed interface UserSessionListScreenState {
    
    /** Initial loading of sessions. */
    object Loading : UserSessionListScreenState
    
    /** Critical error during state initialization. */
    data class Error(val error: AppError) : UserSessionListScreenState
    
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
    ) : UserSessionListScreenState
}
