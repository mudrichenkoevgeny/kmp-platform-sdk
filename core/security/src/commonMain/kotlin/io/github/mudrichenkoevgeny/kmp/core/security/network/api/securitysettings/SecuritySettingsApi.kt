package io.github.mudrichenkoevgeny.kmp.core.security.network.api.securitysettings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.response.settings.SecuritySettingsResponse

interface SecuritySettingsApi {
    suspend fun getSecuritySettings(): AppResult<SecuritySettingsResponse>
}