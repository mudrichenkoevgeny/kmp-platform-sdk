package io.github.mudrichenkoevgeny.kmp.core.settings.network.api.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.response.GlobalSettingsResponse
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.route.GlobalSettingsRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class KtorGlobalSettingsApi(
    private val client: HttpClient
) : GlobalSettingsApi {

    override suspend fun getGlobalSettings(): AppResult<GlobalSettingsResponse> = client.callResult {
        get(GlobalSettingsRoutes.GET_GLOBAL_SETTINGS)
    }
}