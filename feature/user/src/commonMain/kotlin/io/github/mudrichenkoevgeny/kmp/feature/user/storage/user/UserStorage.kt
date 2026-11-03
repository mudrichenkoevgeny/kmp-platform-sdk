package io.github.mudrichenkoevgeny.kmp.feature.user.storage.user

import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserPrivate
import kotlinx.coroutines.flow.Flow

/**
 * Persists user-scoped data (current user snapshot, identifiers, sessions) for offline/UI use.
 *
 * Implementations typically encrypt at rest via the `EncryptedSettings` abstraction from `core:common`.
 */
interface UserStorage {
    /** @return Cached [UserPrivate], or null if none has been stored. */
    suspend fun getCurrentUser(): UserPrivate?

    /** Hot stream of the cached user; emits null until a user is written or after [clear]. */
    fun observeCurrentUser(): Flow<UserPrivate?>

    /** @param currentUser Serialized snapshot to persist as the active user. */
    suspend fun updateCurrentUser(currentUser: UserPrivate)

    /** Drops all user-scoped cached entries managed by this storage. */
    suspend fun clear()
}
