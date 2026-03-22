package io.github.mudrichenkoevgeny.kmp.feature.user.storage.user

import io.github.mudrichenkoevgeny.kmp.feature.user.model.session.UserSession
import io.github.mudrichenkoevgeny.kmp.feature.user.model.user.CurrentUser
import io.github.mudrichenkoevgeny.kmp.feature.user.model.useridentifier.UserIdentifier
import kotlinx.coroutines.flow.Flow

/**
 * Persists user-scoped profile data (current user snapshot, identifiers, sessions) for offline/UI use.
 *
 * Implementations typically encrypt at rest via the `EncryptedSettings` abstraction from `core:common`.
 */
interface UserStorage {
    /** @return Cached [CurrentUser], or null if none has been stored. */
    suspend fun getCurrentUser(): CurrentUser?

    /** Hot stream of the cached user; emits null until a user is written or after [clear]. */
    fun observeCurrentUser(): Flow<CurrentUser?>

    /** @param currentUser Serialized snapshot to persist as the active user. */
    suspend fun updateCurrentUser(currentUser: CurrentUser)

    /** @return Stored identifiers list, or an empty list when unset. */
    suspend fun getUserIdentifiersList(): List<UserIdentifier>

    /** @param userIdentifiersList Replaces the stored identifiers list. */
    suspend fun updateUserIdentifiersList(userIdentifiersList: List<UserIdentifier>)

    /** @return Stored sessions list, or an empty list when unset. */
    suspend fun getUserSessionsList(): List<UserSession>

    /** @param userSessionsList Replaces the stored sessions list. */
    suspend fun updateUserSessionsList(userSessionsList: List<UserSession>)

    /** Drops all user-scoped cached entries managed by this storage. */
    suspend fun clear()
}
