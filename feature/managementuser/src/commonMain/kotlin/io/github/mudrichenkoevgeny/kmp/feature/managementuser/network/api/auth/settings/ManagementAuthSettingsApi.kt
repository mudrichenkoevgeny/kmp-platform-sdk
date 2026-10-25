package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.settings.ManagementAuthSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.settings.OpenAuthSettingsPayload

/** Fetch and update authentication settings for the signed-in user. */
interface ManagementAuthSettingsApi {
    /**
     * Loads auth-related settings for the current session (providers, policies, etc.).
     *
     * @return Settings DTO from the shared contract, or a mapped failure.
     */
    suspend fun getManagementAuthSettings(): AppResult<ManagementAuthSettingsPayload>

    /**
     * Loads open auth-related settings for unauthenticated management users.
     *
     * @return Open settings DTO from the shared contract, or a mapped failure.
     */
    suspend fun getOpenAuthSettings(): AppResult<OpenAuthSettingsPayload>

    /**
     * Updates authentication settings and policies for the current account.
     *
     * @param request New authentication settings payload.
     * @return [AppResult.Success] containing updated [ManagementAuthSettingsPayload], or a mapped failure.
     */
    suspend fun updateManagementAuthSettings(request: ManagementAuthSettingsPayload): AppResult<ManagementAuthSettingsPayload>

    /**
     * Resets global authentication settings to default values.
     *
     * @return Fresh default [ManagementAuthSettingsPayload], or a mapped failure.
     */
    suspend fun resetManagementAuthSettings(): AppResult<ManagementAuthSettingsPayload>
}
