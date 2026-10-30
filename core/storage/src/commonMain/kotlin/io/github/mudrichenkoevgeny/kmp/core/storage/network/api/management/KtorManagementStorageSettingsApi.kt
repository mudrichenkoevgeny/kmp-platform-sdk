package io.github.mudrichenkoevgeny.kmp.core.storage.network.api.management

import io.github.mudrichenkoevgeny.kmp.core.common.network.httprequest.markAsPublic
import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.model.storagesettings.ManagementStorageSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.model.storagesettings.OpenStorageSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.route.management.ManagementStorageSettingsRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

/**
 * [ManagementStorageSettingsApi] implementation backed by Ktor [HttpClient].
 *
 * @param client Shared Ktor client configured with base URL.
 */
class KtorManagementStorageSettingsApi(
    private val client: HttpClient
) : ManagementStorageSettingsApi {

    override suspend fun getManagementStorageSettings(): AppResult<ManagementStorageSettingsPayload> =
        client.callResult {
            get(ManagementStorageSettingsRoutes.GET_MANAGEMENT_STORAGE_SETTINGS)
        }

    override suspend fun getOpenStorageSettings(): AppResult<OpenStorageSettingsPayload> =
        client.callResult {
            get(ManagementStorageSettingsRoutes.GET_OPEN_STORAGE_SETTINGS) {
                markAsPublic()
            }
        }

    override suspend fun updateManagementStorageSettings(
        request: ManagementStorageSettingsPayload
    ): AppResult<ManagementStorageSettingsPayload> =
        client.callResult {
            put(ManagementStorageSettingsRoutes.UPDATE_MANAGEMENT_STORAGE_SETTINGS) {
                setBody(request)
            }
        }

    override suspend fun resetManagementStorageSettings(): AppResult<ManagementStorageSettingsPayload> =
        client.callResult {
            post(ManagementStorageSettingsRoutes.RESET_MANAGEMENT_STORAGE_SETTINGS)
        }
}
