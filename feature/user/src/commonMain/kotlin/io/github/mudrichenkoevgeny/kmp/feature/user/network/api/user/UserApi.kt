package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.user

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.user.CurrentUserResponse

/** Current user profile fetch and account deletion. */
interface UserApi {
    /**
     * Fetches the profile for the signed-in user.
     *
     * @return Current user DTO from the shared contract, or a mapped failure.
     */
    suspend fun getUser(): AppResult<CurrentUserResponse>

    /**
     * Permanently deletes the signed-in account on the server.
     *
     * @return Success or a mapped failure.
     */
    suspend fun deleteUser(): AppResult<Unit>
}