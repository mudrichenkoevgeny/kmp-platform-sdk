package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.model.globalsettings.ManagementGlobalSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.settingsapi.network.route.management.globalsettings.ManagementGlobalSettingsRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody

/**
 * [ManagementGlobalSettingsApi] implementation backed by Ktor [HttpClient].
 *
 * @param client Shared Ktor client configured with base URL.
 */
class KtorManagementGlobalSettingsApi(
    private val client: HttpClient
) : ManagementGlobalSettingsApi {

    override suspend fun getManagementGlobalSettings(): AppResult<ManagementGlobalSettingsPayload> = client.callResult {
        get(ManagementGlobalSettingsRoutes.GET_MANAGEMENT_GLOBAL_SETTINGS)
    }

    override suspend fun updateManagementGlobalSettings(
        request: ManagementGlobalSettingsPayload
    ): AppResult<Unit> = client.callResult {
        put(ManagementGlobalSettingsRoutes.UPDATE_MANAGEMENT_GLOBAL_SETTINGS) {
            setBody(request)
        }
    }
}
