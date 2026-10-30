package io.github.mudrichenkoevgeny.kmp.core.storage.network.api.open

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.model.storagesettings.OpenStorageSettingsPayload

/**
 * Direct HTTP API for fetching open storage settings.
 */
interface OpenStorageSettingsApi {
    /**
     * Fetches the active open storage settings payload from the server.
     *
     * @return [AppResult.Success] containing [OpenStorageSettingsPayload] or [AppResult.Error].
     */
    suspend fun getStorageSettings(): AppResult<OpenStorageSettingsPayload>
}
