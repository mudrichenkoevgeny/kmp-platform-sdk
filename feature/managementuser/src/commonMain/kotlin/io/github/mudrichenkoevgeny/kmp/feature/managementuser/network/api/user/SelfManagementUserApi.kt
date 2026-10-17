package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.user

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.user.UserPrivatePayload

/** Current user data fetch. */
interface SelfManagementUserApi {
    /**
     * Fetches user data for the signed-in user.
     *
     * @return Current user DTO from the shared contract, or a mapped failure.
     */
    suspend fun getUser(): AppResult<UserPrivatePayload>
}