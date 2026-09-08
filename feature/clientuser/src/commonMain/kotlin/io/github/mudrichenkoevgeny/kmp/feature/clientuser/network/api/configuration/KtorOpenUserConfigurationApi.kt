package io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.configuration

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.configuration.OpenUserConfigurationPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.open.configuration.OpenUserConfigurationRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.get

/** [OpenUserConfigurationApi] implementation backed by Ktor [HttpClient]. */
class KtorOpenUserConfigurationApi(
    private val client: HttpClient
) : OpenUserConfigurationApi {

    override suspend fun getOpenUserConfiguration(): AppResult<OpenUserConfigurationPayload> = client.callResult {
        get(OpenUserConfigurationRoutes.GET_CONFIGURATION)
    }
}
