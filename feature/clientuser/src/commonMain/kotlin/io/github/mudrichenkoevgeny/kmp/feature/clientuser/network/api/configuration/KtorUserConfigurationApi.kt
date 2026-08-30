package io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.configuration

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.configuration.UserConfigurationPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.open.configuration.OpenUserConfigurationRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.get

/** [UserConfigurationApi] backed by [HttpClient]. */
class KtorUserConfigurationApi(
    private val client: HttpClient
) : UserConfigurationApi {

    override suspend fun getUserConfiguration(): AppResult<UserConfigurationPayload> = client.callResult {
        get(OpenUserConfigurationRoutes.GET_CONFIGURATION)
    }
}