package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.configuration

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.configuration.ManagementUserConfigurationPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.configuration.OpenUserConfigurationPayload

/** Server-driven user configuration for the signed-in user. */
interface ManagementUserConfigurationApi {
    /**
     * Loads the combined user configuration for management tasks.
     *
     * @return Configuration DTO from the shared contract, or a mapped failure.
     */
    suspend fun getManagementUserConfiguration(): AppResult<ManagementUserConfigurationPayload>

    /**
     * Loads the open user configuration for unauthenticated management users.
     *
     * @return Open configuration DTO from the shared contract, or a mapped failure.
     */
    suspend fun getOpenUserConfiguration(): AppResult<OpenUserConfigurationPayload>
}
