package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.configuration

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.configuration.UserConfigurationPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.management.configuration.ManagementUserConfigurationRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.get

/** [ManagementUserConfigurationApi] backed by [HttpClient]. */
class KtorManagementUserConfigurationApi(
    private val client: HttpClient
) : ManagementUserConfigurationApi {

    override suspend fun getUserConfiguration(): AppResult<UserConfigurationPayload> = client.callResult {
        get(ManagementUserConfigurationRoutes.GET_CONFIGURATION)
    }
}