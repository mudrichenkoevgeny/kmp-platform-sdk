package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.model.globalsettings.ManagementGlobalSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.model.globalsettings.OpenGlobalSettingsPayload

/**
 * Fetch and update global application settings via the management API.
 */
interface ManagementGlobalSettingsApi {
    /**
     * Fetches global settings for management.
     *
     * @return [AppResult.Success] containing [ManagementGlobalSettingsPayload] or [AppResult.Error].
     */
    suspend fun getManagementGlobalSettings(): AppResult<ManagementGlobalSettingsPayload>

    /**
     * Fetches open (public) global settings for unauthenticated management users.
     *
     * @return [AppResult.Success] containing [OpenGlobalSettingsPayload] or [AppResult.Error].
     */
    suspend fun getOpenGlobalSettings(): AppResult<OpenGlobalSettingsPayload>

    /**
     * Updates platform-wide global settings.
     *
     * @param request New global settings payload.
     * @return [AppResult.Success] containing updated [ManagementGlobalSettingsPayload], or a mapped failure.
     */
    suspend fun updateManagementGlobalSettings(request: ManagementGlobalSettingsPayload): AppResult<ManagementGlobalSettingsPayload>

    /**
     * Resets platform-wide global settings to default values.
     *
     * @return Fresh default [ManagementGlobalSettingsPayload], or a mapped failure.
     */
    suspend fun resetManagementGlobalSettings(): AppResult<ManagementGlobalSettingsPayload>
}
