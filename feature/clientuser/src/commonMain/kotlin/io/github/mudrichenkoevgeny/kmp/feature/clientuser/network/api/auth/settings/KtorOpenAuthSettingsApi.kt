package io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.settings.OpenAuthSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.open.auth.settings.OpenAuthSettingsRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.get

/** [OpenAuthSettingsApi] backed by [HttpClient]. */
class KtorOpenAuthSettingsApi(
    private val client: HttpClient
) : OpenAuthSettingsApi {

    override suspend fun getAuthSettings(): AppResult<OpenAuthSettingsPayload> = client.callResult {
        get(OpenAuthSettingsRoutes.GET_OPEN_AUTH_SETTINGS)
    }
}
