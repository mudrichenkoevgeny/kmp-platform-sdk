package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.list

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId

/**
 * UI state for the user's self active sessions management.
 */
sealed interface SelfSessionListScreenState {

    /** Initial loading of sessions. */
    data object Loading : SelfSessionListScreenState

    /**
     * Active sessions list with pagination.
     *
     * @param paging cumulative state of the paginated list.
     * @param actionLoading true when a session is being revoked.
     * @param actionError error from the last revocation attempt.
     */
    data class Content(
        val paging: PaginationState<UserSession>,
        val currentSessionId: UserSessionId? = null,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : SelfSessionListScreenState

    /** Critical error during state initialization. */
    data class Error(val error: AppError) : SelfSessionListScreenState
}
