package io.github.mudrichenkoevgeny.kmp.feature.user.storage.user

import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.UserSessionPayload
import kotlinx.coroutines.flow.Flow

/**
 * Persists user-scoped profile data (current user snapshot, identifiers, sessions) for offline/UI use.
 *
 * Implementations typically encrypt at rest via the `EncryptedSettings` abstraction from `core:common`.
 */
interface UserStorage {
    /** @return Cached [UserDetails], or null if none has been stored. */
    suspend fun getCurrentUser(): UserDetails?

    /** Hot stream of the cached user; emits null until a user is written or after [clear]. */
    fun observeCurrentUser(): Flow<UserDetails?>

    /** @param currentUser Serialized snapshot to persist as the active user. */
    suspend fun updateCurrentUser(currentUser: UserDetails)

    /**
     * Returns a paginated and filtered list of identifiers from local cache based on search criteria.
     *
     * @param pageNumber One-based page index.
     * @param pageSize Maximum items returned per page.
     * @param sortBy Field to sort by (created_at, updated_at).
     * @param sortOrder Sorting direction (ASC, DESC).
     * @param userIds Filters by specific user identifiers.
     * @param userAuthProviders Filters by authentication provider types.
     * @param identifiers Filters by server-defined free-text identifier values.
     * @return Paginated result containing matching user identifier models.
     */
    suspend fun getUserIdentifiersList(
        pageNumber: Int? = null,
        pageSize: Int? = null,
        sortBy: UserSortValues.UserIdentifierSortBy? = null,
        sortOrder: SortOrder? = null,
        userIds: List<String>? = null,
        userAuthProviders: List<UserAuthProvider>? = null,
        identifiers: List<String>? = null
    ): PagedResult<UserIdentifier>

    /**
     * Hot stream of the paginated and filtered list of identifiers from local cache based on search criteria.
     *
     * @param pageNumber One-based page index.
     * @param pageSize Maximum items returned per page.
     * @param sortBy Field to sort by (created_at, updated_at).
     * @param sortOrder Sorting direction (ASC, DESC).
     * @param userIds Filters by specific user identifiers.
     * @param userAuthProviders Filters by authentication provider types.
     * @param identifiers Filters by server-defined free-text identifier values.
     * @return Flow emitting matching user identifier models wrapped in [PagedResult].
     */
    fun observeUserIdentifiersList(
        pageNumber: Int? = null,
        pageSize: Int? = null,
        sortBy: UserSortValues.UserIdentifierSortBy? = null,
        sortOrder: SortOrder? = null,
        userIds: List<String>? = null,
        userAuthProviders: List<UserAuthProvider>? = null,
        identifiers: List<String>? = null
    ): Flow<PagedResult<UserIdentifier>>

    /** @param userIdentifiersList Replaces the stored identifiers paged result. */
    suspend fun updateUserIdentifiersList(userIdentifiersList: PagedResult<UserIdentifier>)

    /** @param userIdentifiersList Replaces the stored identifiers paged result. */
    suspend fun updateUserIdentifiersPayloadList(userIdentifiersList: PagedResult<UserIdentifierPayload>)

    /** Adds a single identifier to the cached list. */
    suspend fun addUserIdentifier(userIdentifier: UserIdentifier)

    /** Removes an identifier by its ID from the cached list. */
    suspend fun removeUserIdentifier(identifierId: UserIdentifierId)

    /**
     * Returns a paginated and filtered list of active sessions from local cache based on search criteria.
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
     * @return Paginated result containing matching user session models.
     */
    suspend fun getUserSessionsList(
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
    ): PagedResult<UserSession>

    /**
     * Hot stream of the paginated and filtered list of active sessions from local cache based on search criteria.
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
     * @return Flow emitting matching user session models wrapped in [PagedResult].
     */
    fun observeUserSessionsList(
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
    ): Flow<PagedResult<UserSession>>

    /** @param userSessionsList Replaces the stored sessions paged result. */
    suspend fun updateUserSessionsList(userSessionsList: PagedResult<UserSession>)

    /** @param userSessionsList Replaces the stored sessions paged result. */
    suspend fun updateUserSessionsPayloadList(userSessionsList: PagedResult<UserSessionPayload>)

    /**
     * Adds a single [UserSession] to the cached list and increments the total count.
     */
    suspend fun addUserSession(userSession: UserSession)

    /**
     * Removes a [UserSession] by its [UserSessionId] from the cached list and decrements the total count.
     */
    suspend fun removeUserSession(sessionId: UserSessionId)

    /**
     * Removes multiple [UserSession]s by their identifiers from the cached list.
     */
    suspend fun removeUserSessions(sessionIds: List<UserSessionId>)

    /** Drops all user-scoped cached entries managed by this storage. */
    suspend fun clear()
}