package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.security.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.securitysettings.ManagementSecuritySettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.securitysettings.OpenSecuritySettingsPayload

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
     * Fetches open (public) security settings for unauthenticated management users.
     *
     * @return [AppResult.Success] containing [OpenSecuritySettingsPayload] or [AppResult.Error].
     */
    suspend fun getOpenSecuritySettings(): AppResult<OpenSecuritySettingsPayload>

    /**
     * Updates platform-wide security settings and policies.
     *
     * @param request New security settings payload.
     * @return [AppResult.Success] containing updated [ManagementSecuritySettingsPayload], or a mapped failure.
     */
    suspend fun updateManagementSecuritySettings(request: ManagementSecuritySettingsPayload): AppResult<ManagementSecuritySettingsPayload>

    /**
     * Resets global security settings and policies to default values.
     *
     * @return Fresh default [ManagementSecuritySettingsPayload], or a mapped failure.
     */
    suspend fun resetManagementSecuritySettings(): AppResult<ManagementSecuritySettingsPayload>
}
