package io.github.mudrichenkoevgeny.kmp.feature.user.repository.user

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails
import kotlinx.coroutines.flow.Flow

/**
 * Access to the signed-in user snapshot and profile management operations.
 */
interface UserRepository {
    /** [Flow] of the cached [UserDetails], or `null` when none is stored. */
    val currentUser: Flow<UserDetails?>

    /**
     * Forces a network reload of the current user profile and updates local storage on success.
     *
     * @return Fresh [UserDetails] on success, or an error result when the request fails.
     */
    suspend fun refreshCurrentUser(): AppResult<UserDetails>

    /**
     * Schedules the current account for permanent deletion and updates local state.
     */
    suspend fun scheduleUserDeletion(): AppResult<UserDetails>

    /**
     * Cancels a pending account deletion request and updates local state.
     */
    suspend fun restoreUser(): AppResult<UserDetails>

    /**
     * Clears local user profile storage and authentication tokens.
     */
    suspend fun clearSession()
}