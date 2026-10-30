package io.github.mudrichenkoevgeny.kmp.core.storage.network.api.open

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.model.storagesettings.OpenStorageSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.route.open.OpenStorageSettingsRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.get

/**
 * Ktor [HttpClient] implementation of [OpenStorageSettingsApi] using foundation route constants.
 *
 * @param client Shared client (typically from `core/common`) with base URL and plugins already applied.
 */
class KtorOpenStorageSettingsApi(
    private val client: HttpClient
) : OpenStorageSettingsApi {

    override suspend fun getStorageSettings(): AppResult<OpenStorageSettingsPayload> = client.callResult {
        get(OpenStorageSettingsRoutes.GET_OPEN_STORAGE_SETTINGS)
    }
}
