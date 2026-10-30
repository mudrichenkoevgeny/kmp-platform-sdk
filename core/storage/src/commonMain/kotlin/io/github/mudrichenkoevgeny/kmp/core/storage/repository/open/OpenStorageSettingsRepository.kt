package io.github.mudrichenkoevgeny.kmp.core.storage.repository.open

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.OpenStorageSettings
import kotlinx.coroutines.flow.Flow

/**
 * Access and refresh open storage settings, including max file size and allowed MIME types.
 */
interface OpenStorageSettingsRepository {

    /**
     * Returns cached open storage settings when available; otherwise loads from the network.
     *
     * @return [AppResult.Success] containing [OpenStorageSettings], or [AppResult.Error] on failure.
     */
    suspend fun getOpenStorageSettings(): AppResult<OpenStorageSettings>

    /**
     * Forces a network reload of open storage settings and updates local cache on success.
     *
     * @return [AppResult.Success] containing refreshed [OpenStorageSettings], or [AppResult.Error] on failure.
     */
    suspend fun refreshOpenStorageSettings(): AppResult<OpenStorageSettings>

    /**
     * Updates in-memory and local cache with new open storage settings.
     *
     * @param openStorageSettings New settings model to store.
     */
    suspend fun updateOpenStorageSettings(openStorageSettings: OpenStorageSettings)

    /**
     * Returns a cold flow emitting open storage settings updates.
     *
     * @return [Flow] emitting [OpenStorageSettings] or `null`.
     */
    fun observeOpenStorageSettings(): Flow<OpenStorageSettings?>
}
