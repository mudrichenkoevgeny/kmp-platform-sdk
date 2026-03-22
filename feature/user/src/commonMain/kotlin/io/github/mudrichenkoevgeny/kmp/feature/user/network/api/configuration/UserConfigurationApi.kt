package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.configuration

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.configuration.UserConfigurationResponse

/** Server-driven user configuration for the signed-in user. */
interface UserConfigurationApi {
    /**
     * Loads feature flags and server-driven options for the current user.
     *
     * @return Configuration DTO from the shared contract, or a mapped failure.
     */
    suspend fun getUserConfiguration(): AppResult<UserConfigurationResponse>
}