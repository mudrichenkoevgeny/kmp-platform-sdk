package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.settings.ManagementAuthSettingsPayload

/** Fetch and update authentication settings for the signed-in user. */
interface ManagementAuthSettingsApi {
    /**
     * Loads auth-related settings for the current session (providers, policies, etc.).
     *
     * @return Settings DTO from the shared contract, or a mapped failure.
     */
    suspend fun getManagementAuthSettings(): AppResult<ManagementAuthSettingsPayload>

    /**
     * Updates authentication settings and policies for the current account.
     *
     * @param request New authentication settings payload.
     * @return Empty success indicator, or a mapped failure.
     */
    suspend fun updateManagementAuthSettings(request: ManagementAuthSettingsPayload): AppResult<Unit>
}