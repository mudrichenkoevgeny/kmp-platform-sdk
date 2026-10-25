package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.settings.OpenAuthSettingsPayload

/** Fetch open authentication settings for unauthenticated or authenticated users. */
interface OpenAuthSettingsApi {
    /**
     * Loads open auth-related settings (providers, policies, etc.).
     *
     * @return Settings DTO from the shared contract, or a mapped failure.
     */
    suspend fun getAuthSettings(): AppResult<OpenAuthSettingsPayload>
}
