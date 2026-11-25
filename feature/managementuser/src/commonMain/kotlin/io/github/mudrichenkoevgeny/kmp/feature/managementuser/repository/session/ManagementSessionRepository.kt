package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.session

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.session.globallist.ManagementGlobalSessionListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.session.userlist.ManagementUserSessionListParams

/**
 * Administrative repository for managing user sessions across all accounts.
 */
public interface ManagementSessionRepository {

    /**
     * Returns a paginated and filtered list of active sessions based on search criteria.
     *
     * @param params Administrative session list query and filter parameters DTO.
     * @return Paginated result containing matching user session models, or a mapped failure.
     */
    public suspend fun getSessions(
        params: ManagementGlobalSessionListParams? = null
    ): AppResult<PagedResult<UserSessionSummary>>

    /**
     * Returns a paginated and filtered list of active sessions for a specific target user.
     *
     * @param params Per-user session list query and filter parameters DTO.
     * @return Paginated result containing matching session models, or a mapped failure.
     */
    public suspend fun getUserSessions(
        params: ManagementUserSessionListParams
    ): AppResult<PagedResult<UserSessionSummary>>

    /**
     * Retrieves specific session details.
     *
     * @param sessionId Unique session identifier.
     * @return Detailed information of the target session model, or a mapped failure.
     */
    public suspend fun getSession(sessionId: UserSessionId): AppResult<UserSessionPrivate>

    /**
     * Deletes a specific session for the given user.
     *
     * @param userId Unique identifier of the session owner.
     * @param sessionId Unique session identifier to revoke.
     * @return Empty success indicator, or a mapped failure.
     */
    public suspend fun deleteSession(
        userId: UserId,
        sessionId: UserSessionId
    ): AppResult<Unit>

    /**
     * Deletes all active sessions for the specified user.
     *
     * @param userId Unique identifier of the target account.
     * @return Empty success indicator, or a mapped failure.
     */
    public suspend fun deleteAllUserSessions(userId: UserId): AppResult<Unit>
}
