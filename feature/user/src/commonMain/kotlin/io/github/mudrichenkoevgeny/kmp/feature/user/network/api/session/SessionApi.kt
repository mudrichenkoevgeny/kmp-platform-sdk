package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.session

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.session.list.SelfSessionListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.DeletedSessionsPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.UserSessionPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.UserSessionSummaryPayload

/** Session listing, logout, and per-session revocation for the authenticated user. */
public interface SessionApi {

    /**
     * Returns a paginated and filtered list of active sessions for the current authenticated account.
     *
     * @param params Query and filter parameters DTO.
     * @return [AppResult.Success] containing a [PagedResult] of [UserSessionSummaryPayload] or [AppResult.Error].
     */
    public suspend fun getSessions(params: SelfSessionListParams? = null): AppResult<PagedResult<UserSessionSummaryPayload>>

    /**
     * Returns details of a specific session owned by the current management account.
     */
    public suspend fun getSession(userSessionId: UserSessionId): AppResult<UserSessionPrivatePayload>

    /**
     * Ends the current active management session on the server (sign-out for this client).
     */
    public suspend fun logout(): AppResult<Unit>

    /**
     * Deletes a specific active session for the current management account.
     */
    public suspend fun deleteSession(userSessionId: UserSessionId): AppResult<Unit>

    /**
     * Deletes all sessions for the current management account except the one used for this request.
     */
    public suspend fun deleteAllOtherSessions(): AppResult<DeletedSessionsPayload>

    /**
     * Performs re-authentication via TOTP for the current management session to update its trust level.
     */
    public suspend fun reauthenticateSession(request: VerifyTotpPayload): AppResult<Unit>
}
