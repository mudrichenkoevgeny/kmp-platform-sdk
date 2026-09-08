package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.security.settings

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.securitysettings.ManagementSecuritySettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.securityapi.network.route.management.security.settings.ManagementSecuritySettingsRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody

/**
 * [ManagementSecuritySettingsApi] implementation backed by Ktor [HttpClient].
 *
 * @param client Shared Ktor client configured with base URL.
 */
class KtorManagementSecuritySettingsApi(
    private val client: HttpClient
) : ManagementSecuritySettingsApi {

    override suspend fun getManagementSecuritySettings(): AppResult<ManagementSecuritySettingsPayload> = client.callResult {
        get(ManagementSecuritySettingsRoutes.GET_MANAGEMENT_SECURITY_SETTINGS)
    }

    override suspend fun updateManagementSecuritySettings(
        request: ManagementSecuritySettingsPayload
    ): AppResult<Unit> = client.callResult {
        put(ManagementSecuritySettingsRoutes.UPDATE_MANAGEMENT_SECURITY_SETTINGS) {
            setBody(request)
        }
    }
}
