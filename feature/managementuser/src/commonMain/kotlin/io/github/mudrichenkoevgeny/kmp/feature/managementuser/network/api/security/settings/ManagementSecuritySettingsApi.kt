package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.security.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.securitysettings.ManagementSecuritySettingsPayload

/**
 * Fetch and update security settings via the management API.
 */
interface ManagementSecuritySettingsApi {
    /**
     * Fetches security settings for management.
     *
     * @return [AppResult.Success] containing [ManagementSecuritySettingsPayload] or [AppResult.Error].
     */
    suspend fun getManagementSecuritySettings(): AppResult<ManagementSecuritySettingsPayload>

    /**
     * Updates platform-wide security settings and policies.
     *
     * @param request New security settings payload.
     * @return Empty success indicator, or a mapped failure.
     */
    suspend fun updateManagementSecuritySettings(request: ManagementSecuritySettingsPayload): AppResult<Unit>
}
