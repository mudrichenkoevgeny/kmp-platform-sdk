package io.github.mudrichenkoevgeny.kmp.feature.settingsapi.network.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.settings.network.globalsettings.OpenGlobalSettingsApi
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.model.globalsettings.OpenGlobalSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.settingsapi.network.route.open.globalsettings.OpenGlobalSettingsRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.get

/**
 * Ktor [HttpClient] implementation of [OpenGlobalSettingsApi] using foundation route constants.
 *
 * @param client Shared client (typically from `core/common`) with base URL and plugins already applied.
 */
class KtorOpenGlobalSettingsApi(
    private val client: HttpClient
) : OpenGlobalSettingsApi {

    override suspend fun getOpenGlobalSettings(): AppResult<OpenGlobalSettingsPayload> = client.callResult {
        get(OpenGlobalSettingsRoutes.GET_OPEN_GLOBAL_SETTINGS)
    }
}
