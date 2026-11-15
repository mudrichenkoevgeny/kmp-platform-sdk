package io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.user

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.user.UserPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.user.SelfUpdateUserRequest

/** Current user data fetch and account deletion. */
public interface OpenUserApi {
    /**
     * Fetches user data for the signed-in user.
     *
     * @return Current user DTO from the shared contract, or a mapped failure.
     */
    public suspend fun getUser(): AppResult<UserPrivatePayload>

    /**
     * Partially updates user details and profile fields of the currently authenticated user.
     *
     * @param request Patch payload containing fields to change.
     * @return Updated current user DTO, or a mapped failure.
     */
    public suspend fun selfUpdateUser(request: SelfUpdateUserRequest): AppResult<UserPrivatePayload>

    /**
     * Permanently deletes the signed-in account on the server.
     *
     * @return Success or a mapped failure.
     */
    public suspend fun scheduleUserDeletion(): AppResult<UserPrivatePayload>

    /**
     * Cancels a scheduled account deletion and restores the user to an active state.
     *
     * @return Current user DTO reflecting the restored account status, or a mapped failure.
     */
    public suspend fun restoreUser(): AppResult<UserPrivatePayload>
}
