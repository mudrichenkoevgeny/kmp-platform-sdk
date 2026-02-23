package io.github.mudrichenkoevgeny.kmp.core.security.network.api.securitysettings

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.response.settings.SecuritySettingsResponse
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.route.settings.SecuritySettingsRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class KtorSecuritySettingsApi(
    private val client: HttpClient
) : SecuritySettingsApi {

    override suspend fun getSecuritySettings(): AppResult<SecuritySettingsResponse> = client.callResult {
        get(SecuritySettingsRoutes.GET_SECURITY_SETTINGS)
    }
}