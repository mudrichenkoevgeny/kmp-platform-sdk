package io.github.mudrichenkoevgeny.kmp.core.security.network.securitysettings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.securitysettings.OpenSecuritySettingsPayload

/**
 * Direct HTTP API for fetching open security settings.
 */
interface OpenSecuritySettingsApi {
    /**
     * Fetches the active open security settings payload from the server.
     *
     * @return [AppResult.Success] containing [OpenSecuritySettingsPayload] or [AppResult.Error].
     */
    suspend fun getSecuritySettings(): AppResult<OpenSecuritySettingsPayload>
}
