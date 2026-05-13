package io.github.mudrichenkoevgeny.kmp.feature.settingsapi.network.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.settings.network.globalsettings.GlobalSettingsApi
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.model.globalsettings.GlobalSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.settingsapi.network.route.open.globalsettings.OpenGlobalSettingsRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.get

/**
 * Ktor [HttpClient] implementation of [GlobalSettingsApi] using foundation route constants.
 *
 * @param client Shared client (typically from `core/common`) with base URL and plugins already applied.
 */
class KtorGlobalSettingsApi(
    private val client: HttpClient
) : GlobalSettingsApi {

    override suspend fun getGlobalSettings(): AppResult<GlobalSettingsPayload> = client.callResult {
        get(OpenGlobalSettingsRoutes.GET_GLOBAL_SETTINGS)
    }
}