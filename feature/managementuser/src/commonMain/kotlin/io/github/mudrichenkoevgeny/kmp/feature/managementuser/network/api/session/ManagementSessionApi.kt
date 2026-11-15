package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.session

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.session.globallist.ManagementGlobalSessionListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.session.userlist.ManagementUserSessionListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.UserSessionPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.UserSessionSummaryPayload

/** Administrative user sessions management. */
public interface ManagementSessionApi {

    /**
     * Returns a paginated and filtered list of active sessions based on search criteria.
     *
     * @param params Administrative session list query and filter parameters DTO.
     * @return Paginated result containing matching session payloads, or a mapped failure.
     */
    public suspend fun getSessions(
        params: ManagementGlobalSessionListParams? = null
    ): AppResult<PagedResult<UserSessionSummaryPayload>>

    /**
     * Returns a paginated and filtered list of active summary sessions for a specific target user.
     *
     * @param params Per-user session list query and filter parameters DTO.
     * @return Paginated result containing matching session payloads, or a mapped failure.
     */
    public suspend fun getUserSessions(
        params: ManagementUserSessionListParams
    ): AppResult<PagedResult<UserSessionSummaryPayload>>

    /**
     * Retrieves specific session details.
     *
     * @param sessionId Unique session identifier.
     * @return Detailed information of the target session, or a mapped failure.
     */
    public suspend fun getSession(sessionId: String): AppResult<UserSessionPrivatePayload>

    /**
     * Deletes a specific session for the given user.
     *
     * @param userId Unique identifier of the session owner.
     * @param sessionId Unique session identifier to revoke.
     * @return Empty success indicator, or a mapped failure.
     */
    public suspend fun deleteSession(
        userId: UserId,
        sessionId: String
    ): AppResult<Unit>

    /**
     * Deletes all active sessions for the specified user.
     *
     * @param userId Unique identifier of the target account.
     * @return Empty success indicator, or a mapped failure.
     */
    public suspend fun deleteAllUserSessions(userId: UserId): AppResult<Unit>
}
