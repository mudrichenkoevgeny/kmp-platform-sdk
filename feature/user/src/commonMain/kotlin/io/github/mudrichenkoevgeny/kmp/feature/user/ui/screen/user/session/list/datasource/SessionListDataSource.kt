package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.datasource

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary

/**
 * Data source abstraction providing session fetch and revocation capabilities for session list screens.
 */
interface SessionListDataSource {

    /** Whether revoking a single session is available in this data source contour. */
    val isRevokeAvailable: Boolean

    /** Whether revoking all other sessions / all user sessions is available in this data source contour. */
    val isRevokeAllOthersAvailable: Boolean

    /**
     * Fetches a paginated list of user sessions according to [query].
     *
     * @param query Filtering, sorting, and pagination parameters.
     * @return [AppResult] containing a [PagedResult] of [UserSessionSummary].
     */
    suspend fun getSessions(query: SessionListQuery): AppResult<PagedResult<UserSessionSummary>>

    /**
     * Revokes a single session by ID and optional owner user ID.
     *
     * @param sessionId Target session ID to revoke.
     * @param userId Optional target user ID (for management contour).
     * @return [AppResult] indicating success or failure error.
     */
    suspend fun revokeSession(sessionId: String, userId: String? = null): AppResult<Unit>

    /**
     * Revokes all other sessions (or all user sessions).
     *
     * @param userId Optional target user ID (for management contour).
     * @return [AppResult] indicating success or failure error.
     */
    suspend fun revokeAllOtherSessions(userId: String? = null): AppResult<Unit>
}
