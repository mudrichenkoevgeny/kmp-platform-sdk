package io.github.mudrichenkoevgeny.kmp.feature.user.storage.user

import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
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

    /** @return Stored identifiers paged result, or an empty result when unset. */
    suspend fun getUserIdentifiersList(): PagedResult<UserIdentifier>

    /** Hot stream of the cached identifiers list; emits an empty [PagedResult] until data is written. */
    fun observeUserIdentifiersList(): Flow<PagedResult<UserIdentifier>>

    /** @param userIdentifiersList Replaces the stored identifiers paged result. */
    suspend fun updateUserIdentifiersList(userIdentifiersList: PagedResult<UserIdentifier>)

    /** @param userIdentifiersList Replaces the stored identifiers paged result. */
    suspend fun updateUserIdentifiersPayloadList(userIdentifiersList: PagedResult<UserIdentifierPayload>)

    /** Adds a single identifier to the cached list. */
    suspend fun addUserIdentifier(userIdentifier: UserIdentifier)

    /** Removes an identifier by its ID from the cached list. */
    suspend fun removeUserIdentifier(identifierId: UserIdentifierId)

    /** @return Stored sessions paged result, or an empty result when unset. */
    suspend fun getUserSessionsList(): PagedResult<UserSession>

    /** Hot stream of the cached sessions list; emits an empty [PagedResult] until data is written. */
    fun observeUserSessionsList(): Flow<PagedResult<UserSession>>

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