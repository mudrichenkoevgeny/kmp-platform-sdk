package io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.configuration

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.configuration.OpenUserConfigurationPayload

/** Fetch combined user configuration bundle. */
interface OpenUserConfigurationApi {
    /**
     * Loads the combined user configuration (global, security, auth settings).
     *
     * @return [AppResult.Success] with [OpenUserConfigurationPayload], or [AppResult.Error].
     */
    suspend fun getOpenUserConfiguration(): AppResult<OpenUserConfigurationPayload>
}
