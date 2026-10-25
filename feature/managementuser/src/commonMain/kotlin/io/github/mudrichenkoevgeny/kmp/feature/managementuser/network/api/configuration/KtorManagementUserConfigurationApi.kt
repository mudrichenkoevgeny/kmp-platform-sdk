package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.configuration

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.network.auth.markAsPublic
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.configuration.ManagementUserConfigurationPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.configuration.OpenUserConfigurationPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.management.configuration.ManagementUserConfigurationRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.get

/** [ManagementUserConfigurationApi] backed by [HttpClient]. */
class KtorManagementUserConfigurationApi(
    private val client: HttpClient
) : ManagementUserConfigurationApi {

    override suspend fun getManagementUserConfiguration(): AppResult<ManagementUserConfigurationPayload> = client.callResult {
        get(ManagementUserConfigurationRoutes.GET_MANAGEMENT_USER_CONFIGURATION)
    }

    override suspend fun getOpenUserConfiguration(): AppResult<OpenUserConfigurationPayload> = client.callResult {
        get(ManagementUserConfigurationRoutes.GET_OPEN_USER_CONFIGURATION) {
            markAsPublic()
        }
    }
}
