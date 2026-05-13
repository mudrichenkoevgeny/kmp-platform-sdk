package io.github.mudrichenkoevgeny.kmp.feature.securityapi.network.securitysettings

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.network.securitysettings.SecuritySettingsApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.securitysettings.SecuritySettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.securityapi.network.route.open.security.settings.OpenSecuritySettingsRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.get

/**
 * Ktor [HttpClient] implementation of [SecuritySettingsApi] using foundation route constants.
 *
 * @param client Shared client (typically from `core/common`) with base URL and plugins already applied.
 */
class KtorSecuritySettingsApi(
    private val client: HttpClient
) : SecuritySettingsApi {

    override suspend fun getSecuritySettings(): AppResult<SecuritySettingsPayload> = client.callResult {
        get(OpenSecuritySettingsRoutes.GET_SECURITY_SETTINGS)
    }
}