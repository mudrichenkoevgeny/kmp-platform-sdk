package io.github.mudrichenkoevgeny.kmp.feature.securityapi.network.securitysettings

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.network.securitysettings.OpenSecuritySettingsApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.securitysettings.OpenSecuritySettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.securityapi.network.route.open.security.settings.OpenSecuritySettingsRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.get

/**
 * Ktor [HttpClient] implementation of [OpenSecuritySettingsApi] using foundation route constants.
 *
 * @param client Shared client (typically from `core/common`) with base URL and plugins already applied.
 */
class KtorOpenSecuritySettingsApi(
    private val client: HttpClient
) : OpenSecuritySettingsApi {

    override suspend fun getSecuritySettings(): AppResult<OpenSecuritySettingsPayload> = client.callResult {
        get(OpenSecuritySettingsRoutes.GET_OPEN_SECURITY_SETTINGS)
    }
}
