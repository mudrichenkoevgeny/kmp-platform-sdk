package io.github.mudrichenkoevgeny.kmp.feature.user.repository.session

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.session.list.SelfSessionListParams

/**
 * Manages active sessions linked to the current authenticated management account.
 */
interface SessionRepository {

    /**
     * Returns a paginated and filtered list of active sessions for the current authenticated account.
     *
     * @param params Listing query and filter parameters DTO.
     * @return Paginated result containing matching active session models, or a mapped failure.
     */
    suspend fun getSessions(params: SelfSessionListParams? = null): AppResult<PagedResult<UserSessionSummary>>

    /**
     * Returns details of a specific session owned by the current management account.
     *
     * @param userSessionId Unique session identifier.
     * @return Detailed session model or a mapped failure.
     */
    suspend fun getSession(userSessionId: UserSessionId): AppResult<UserSessionPrivate>

    /**
     * Ends the current active management session on the server (sign-out for this client).
     *
     * @return Empty success indicator, or a mapped failure.
     */
    suspend fun logout(): AppResult<Unit>

    /**
     * Deletes a specific active session for the current management account.
     *
     * @param userSessionId Unique session identifier to revoke.
     * @return Empty success indicator, or a mapped failure.
     */
    suspend fun deleteSession(userSessionId: UserSessionId): AppResult<Unit>

    /**
     * Deletes all sessions for the current management account except the one used for this request.
     *
     * @return Empty success indicator, or a mapped failure.
     */
    suspend fun deleteAllOtherSessions(): AppResult<Unit>

    /**
     * Performs re-authentication via TOTP for the current management session to update its trust level.
     *
     * @param mfaToken Opaque intermediate verification token.
     * @param code time-based verification code.
     * @return Empty success indicator, or a mapped failure.
     */
    suspend fun reauthenticateSession(mfaToken: String, code: String): AppResult<Unit>
}
