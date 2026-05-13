package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.session

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.DeletedSessionsPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.UserSessionPayload

/** Session listing, logout, and per-session revocation for the authenticated user. */
interface SessionApi {
    /**
     * Lists active sessions for the current user.
     *
     * @return Session rows from the shared contract, or a mapped failure.
     */
    suspend fun getSessions(): AppResult<PagedResult<UserSessionPayload>>

    suspend fun getSession(
        userSessionId: UserSessionId
    ): AppResult<UserSessionPayload>

    /**
     * Ends the current session on the server (sign-out for this client).
     *
     * @return Success or a mapped failure.
     */
    suspend fun logout(): AppResult<Unit>

    /**
     * Revokes a specific session by its server identifier.
     *
     * @param userSessionId Session id as returned by [getSessions].
     * @return Success or a mapped failure.
     */
    suspend fun deleteSession(userSessionId: UserSessionId): AppResult<Unit>

    /**
     * Revokes every session except the current one.
     *
     * @return Success or a mapped failure.
     */
    suspend fun deleteAllOtherSessions(): AppResult<DeletedSessionsPayload>

    suspend fun reauthenticateSession(request: VerifyTotpPayload): AppResult<Unit>
}