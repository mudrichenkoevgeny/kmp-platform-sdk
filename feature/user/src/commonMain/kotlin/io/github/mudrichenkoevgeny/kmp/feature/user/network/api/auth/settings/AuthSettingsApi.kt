package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.auth.settings.AuthSettingsResponse

/** Fetch current authentication settings for the signed-in user. */
interface AuthSettingsApi {
    /**
     * Loads auth-related settings for the current session (providers, policies, etc.).
     *
     * @return Settings DTO from the shared contract, or a mapped failure.
     */
    suspend fun getAuthSettings(): AppResult<AuthSettingsResponse>
}