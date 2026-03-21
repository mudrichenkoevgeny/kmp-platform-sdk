package io.github.mudrichenkoevgeny.kmp.core.settings.network.api.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.response.GlobalSettingsResponse

/**
 * REST boundary for loading global settings from the backend.
 */
interface GlobalSettingsApi {
    /**
     * Performs the configured GET for global settings.
     *
     * @return [AppResult.Success] with [GlobalSettingsResponse], or [AppResult.Error] on HTTP or parse failure.
     */
    suspend fun getGlobalSettings(): AppResult<GlobalSettingsResponse>
}