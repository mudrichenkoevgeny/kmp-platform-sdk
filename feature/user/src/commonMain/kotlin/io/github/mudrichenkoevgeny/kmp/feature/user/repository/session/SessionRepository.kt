package io.github.mudrichenkoevgeny.kmp.feature.user.repository.session

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId

/**
 * Manages active sessions linked to the current authenticated management account.
 */
interface SessionRepository {

    /**
     * Returns a paginated and filtered list of active sessions for the current authenticated account.
     *
     * @param pageNumber One-based page index.
     * @param pageSize Maximum items returned per page.
     * @param sortBy Field to sort by (last_accessed_at, last_reauthenticated_at, expires_at, created_at, updated_at).
     * @param sortOrder Sorting direction (ASC, DESC).
     * @param identifiers Filters by server-defined free-text identifier values.
     * @param identifierIds Filters by unique credential record IDs.
     * @param userAuthProviders Filters by authentication provider types.
     * @param clientTypes Filters by client category types.
     * @param userAgents Filters by server-defined user agent substrings.
     * @param ipAddresses Filters by server-defined IP address substrings.
     * @param languages Filters by server-defined language tags.
     * @param deviceIds Filters by opaque unique device IDs.
     * @param deviceNames Filters by server-defined device name substrings.
     * @param appVersions Filters by application version strings.
     * @param operationSystemVersions Filters by server-defined operating system version substrings.
     * @return Paginated result containing matching active session models, or a mapped failure.
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
    ): AppResult<PagedResult<UserSession>>

    /**
     * Returns details of a specific session owned by the current management account.
     *
     * @param userSessionId Unique session identifier.
     * @return Detailed session model or a mapped failure.
     */
    suspend fun getSession(userSessionId: UserSessionId): AppResult<UserSession>

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