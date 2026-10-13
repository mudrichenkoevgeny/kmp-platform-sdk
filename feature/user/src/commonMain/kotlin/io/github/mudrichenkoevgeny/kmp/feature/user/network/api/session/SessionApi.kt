package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.session

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.DeletedSessionsPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.UserSessionPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.UserSessionSummaryPayload

/** Session listing, logout, and per-session revocation for the authenticated user. */
interface SessionApi {

    /**
     * Returns a paginated and filtered list of active sessions for the current authenticated account.
     */
    suspend fun getSessions(
        pageNumber: Int? = null,
        pageSize: Int? = null,
        sortBy: UserSortValues.UserSessionSortBy? = null,
        sortOrder: SortOrder? = null,
        identifiers: List<String>? = null,
        identifierIds: List<String>? = null,
        userAuthProviders: List<UserAuthProvider>? = null,
        clientTypes: List<ClientType>? = null,
        userAgents: List<String>? = null,
        ipAddresses: List<String>? = null,
        languages: List<String>? = null,
        deviceIds: List<String>? = null,
        deviceNames: List<String>? = null,
        appVersions: List<String>? = null,
        operationSystemVersions: List<String>? = null
    ): AppResult<PagedResult<UserSessionSummaryPayload>>

    /**
     * Returns details of a specific session owned by the current management account.
     */
    suspend fun getSession(userSessionId: UserSessionId): AppResult<UserSessionPrivatePayload>

    /**
     * Ends the current active management session on the server (sign-out for this client).
     */
    suspend fun logout(): AppResult<Unit>

    /**
     * Deletes a specific active session for the current management account.
     */
    suspend fun deleteSession(userSessionId: UserSessionId): AppResult<Unit>

    /**
     * Deletes all sessions for the current management account except the one used for this request.
     */
    suspend fun deleteAllOtherSessions(): AppResult<DeletedSessionsPayload>

    /**
     * Performs re-authentication via TOTP for the current management session to update its trust level.
     */
    suspend fun reauthenticateSession(request: VerifyTotpPayload): AppResult<Unit>
}