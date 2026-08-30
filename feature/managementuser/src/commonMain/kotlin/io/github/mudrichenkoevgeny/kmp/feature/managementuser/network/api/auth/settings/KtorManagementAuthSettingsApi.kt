package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.settings.ManagementAuthSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.management.auth.settings.ManagementAuthSettingsRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody

/** [ManagementAuthSettingsApi] backed by [HttpClient]. */
class KtorManagementAuthSettingsApi(
    private val client: HttpClient
) : ManagementAuthSettingsApi {

    override suspend fun getAuthSettings(): AppResult<ManagementAuthSettingsPayload> = client.callResult {
        get(ManagementAuthSettingsRoutes.GET_AUTH_SETTINGS_MANAGEMENT)
    }

    override suspend fun updateAuthSettings(
        request: ManagementAuthSettingsPayload
    ): AppResult<Unit> = client.callResult {
        put(ManagementAuthSettingsRoutes.UPDATE_AUTH_SETTINGS) {
            setBody(request)
        }
    }
}