package io.github.mudrichenkoevgeny.kmp.core.settings.network.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.model.globalsettings.OpenGlobalSettingsPayload

/**
 * Direct HTTP API for fetching open global platform settings.
 */
interface OpenGlobalSettingsApi {
    /**
     * Fetches the active open global settings payload from the server.
     *
     * @return [AppResult.Success] containing [OpenGlobalSettingsPayload] or [AppResult.Error].
     */
    suspend fun getOpenGlobalSettings(): AppResult<OpenGlobalSettingsPayload>
}
