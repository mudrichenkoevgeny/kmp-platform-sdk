package io.github.mudrichenkoevgeny.kmp.core.security.network.api.securitysettings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.response.settings.SecuritySettingsResponse

/**
 * REST boundary for loading security settings (including password policy) from the backend.
 */
interface SecuritySettingsApi {
    /**
     * Performs the configured GET for security settings.
     *
     * @return [AppResult.Success] with [SecuritySettingsResponse], or [AppResult.Error] on HTTP or parse failure.
     */
    suspend fun getSecuritySettings(): AppResult<SecuritySettingsResponse>
}