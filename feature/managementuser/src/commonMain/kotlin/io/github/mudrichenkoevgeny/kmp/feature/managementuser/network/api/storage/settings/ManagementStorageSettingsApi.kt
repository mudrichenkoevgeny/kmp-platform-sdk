package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.storage.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.model.storagesettings.ManagementStorageSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.model.storagesettings.OpenStorageSettingsPayload

/**
 * Fetch and update storage settings via the management API.
 */
interface ManagementStorageSettingsApi {
    /**
     * Fetches storage settings for management.
     *
     * @return [AppResult.Success] containing [ManagementStorageSettingsPayload] or [AppResult.Error].
     */
    suspend fun getManagementStorageSettings(): AppResult<ManagementStorageSettingsPayload>

    /**
     * Fetches open (public) storage settings for unauthenticated management users.
     *
     * @return [AppResult.Success] containing [OpenStorageSettingsPayload] or [AppResult.Error].
     */
    suspend fun getOpenStorageSettings(): AppResult<OpenStorageSettingsPayload>

    /**
     * Updates platform-wide storage settings and policies.
     *
     * @param request New storage settings payload.
     * @return [AppResult.Success] containing updated [ManagementStorageSettingsPayload], or a mapped failure.
     */
    suspend fun updateManagementStorageSettings(request: ManagementStorageSettingsPayload): AppResult<ManagementStorageSettingsPayload>

    /**
     * Resets global storage settings and policies to default values.
     *
     * @return Fresh default [ManagementStorageSettingsPayload], or a mapped failure.
     */
    suspend fun resetManagementStorageSettings(): AppResult<ManagementStorageSettingsPayload>
}
