package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.auth.settings.AuthSettingsResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.auth.settings.AuthSettingsRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.get

/** [AuthSettingsApi] backed by [HttpClient]. */
class KtorAuthSettingsApi(
    private val client: HttpClient
) : AuthSettingsApi {

    override suspend fun getAuthSettings(): AppResult<AuthSettingsResponse> = client.callResult {
        get(AuthSettingsRoutes.GET_AUTH_SETTINGS)
    }
}