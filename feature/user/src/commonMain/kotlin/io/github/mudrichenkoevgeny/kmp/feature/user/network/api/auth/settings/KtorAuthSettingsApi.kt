package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.settings.PublicAuthSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.open.auth.settings.OpenAuthSettingsRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.get

/** [AuthSettingsApi] backed by [HttpClient]. */
class KtorAuthSettingsApi(
    private val client: HttpClient
) : AuthSettingsApi {

    override suspend fun getAuthSettings(): AppResult<PublicAuthSettingsPayload> = client.callResult {
        get(OpenAuthSettingsRoutes.GET_AUTH_SETTINGS)
    }
}