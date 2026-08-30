package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.session

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId

/**
 * Administrative repository for managing user sessions across all accounts.
 */
interface ManagementSessionRepository {

    /**
     * Returns a paginated and filtered list of active sessions based on search criteria.
     *
     * @param pageNumber One-based page index.
     * @param pageSize Maximum items returned per page.
     * @param sortBy Field to sort by (last_accessed_at, last_reauthenticated_at, expires_at, created_at, updated_at).
     * @param sortOrder Sorting direction (ASC, DESC).
     * @param userIds Filters by specific user identifiers.
     * @param userRoles Filters by user role types.
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
     * @return Paginated result containing matching user session models, or a mapped failure.
     */
    suspend fun getSessions(
        pageNumber: Int? = null,
        pageSize: Int? = null,
        sortBy: UserSortValues.UserSessionSortBy? = null,
        sortOrder: SortOrder? = null,
        userIds: List<String>? = null,
        userRoles: List<UserRole>? = null,
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
     * Retrieves specific session details.
     *
     * @param sessionId Unique session identifier.
     * @return Detailed information of the target session model, or a mapped failure.
     */
    suspend fun getSession(sessionId: String): AppResult<UserSession>

    /**
     * Deletes a specific session for the given user.
     *
     * @param userId Unique identifier of the session owner.
     * @param sessionId Unique session identifier to revoke.
     * @return Empty success indicator, or a mapped failure.
     */
    suspend fun deleteSession(userId: UserId, sessionId: String): AppResult<Unit>

    /**
     * Deletes all active sessions for the specified user.
     *
     * @param userId Unique identifier of the target account.
     * @return Empty success indicator, or a mapped failure.
     */
    suspend fun deleteAllUserSessions(userId: UserId): AppResult<Unit>
}