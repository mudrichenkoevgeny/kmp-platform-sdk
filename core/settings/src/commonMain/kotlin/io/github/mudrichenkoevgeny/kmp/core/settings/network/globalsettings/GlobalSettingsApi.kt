package io.github.mudrichenkoevgeny.kmp.core.settings.network.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.model.globalsettings.GlobalSettingsPayload

/**
 * REST boundary for loading global settings from the backend.
 */
interface GlobalSettingsApi {
    /**
     * Performs the configured GET for global settings.
     *
     * @return [AppResult.Success] with [GlobalSettingsPayload], or [AppResult.Error] on HTTP or parse failure.
     */
    suspend fun getGlobalSettings(): AppResult<GlobalSettingsPayload>
}