package io.github.mudrichenkoevgeny.kmp.feature.user.repository.user

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserPrivate
import kotlinx.coroutines.flow.Flow

/**
 * Access to the signed-in user snapshot and user account management operations.
 */
interface UserRepository {
    /** [Flow] of the cached [UserPrivate], or `null` when none is stored. */
    val currentUser: Flow<UserPrivate?>

    /**
     * Forces a network reload of the current user data and updates local storage on success.
     *
     * @return Fresh [UserPrivate] on success, or an error result when the request fails.
     */
    suspend fun refreshCurrentUser(): AppResult<UserPrivate>

    /**
     * Schedules the current account for permanent deletion and updates local state.
     */
    suspend fun scheduleUserDeletion(): AppResult<UserPrivate>

    /**
     * Cancels a pending account deletion request and updates local state.
     */
    suspend fun restoreUser(): AppResult<UserPrivate>

    /**
     * Clears local user storage and authentication tokens.
     */
    suspend fun clearSession()
}