package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.configuration

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.configuration.UserConfigurationResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.configuration.UserConfigurationRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.get

/** [UserConfigurationApi] backed by [HttpClient]. */
class KtorUserConfigurationApi(
    private val client: HttpClient
) : UserConfigurationApi {

    override suspend fun getUserConfiguration(): AppResult<UserConfigurationResponse> = client.callResult {
        get(UserConfigurationRoutes.GET_CONFIGURATION)
    }
}