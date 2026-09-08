package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.model.globalsettings.ManagementGlobalSettingsPayload

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
     * Updates platform-wide global settings.
     *
     * @param request New global settings payload.
     * @return Empty success indicator, or a mapped failure.
     */
    suspend fun updateManagementGlobalSettings(request: ManagementGlobalSettingsPayload): AppResult<Unit>
}
