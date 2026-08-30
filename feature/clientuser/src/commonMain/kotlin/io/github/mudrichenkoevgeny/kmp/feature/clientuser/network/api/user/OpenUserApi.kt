package io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.user

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.user.UserDetailsPayload

/** Current user profile fetch and account deletion. */
interface OpenUserApi {
    /**
     * Fetches the profile for the signed-in user.
     *
     * @return Current user DTO from the shared contract, or a mapped failure.
     */
    suspend fun getUser(): AppResult<UserDetailsPayload>

    /**
     * Permanently deletes the signed-in account on the server.
     *
     * @return Success or a mapped failure.
     */
    suspend fun scheduleUserDeletion(): AppResult<UserDetailsPayload>

    /**
     * Cancels a scheduled account deletion and restores the user to an active state.
     *
     * @return Current user DTO reflecting the restored account status, or a mapped failure.
     */
    suspend fun restoreUser(): AppResult<UserDetailsPayload>
}