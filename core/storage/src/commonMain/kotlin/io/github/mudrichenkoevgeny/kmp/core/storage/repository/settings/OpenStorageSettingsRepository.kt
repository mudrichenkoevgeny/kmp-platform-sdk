package io.github.mudrichenkoevgeny.kmp.core.storage.repository.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.OpenStorageSettings
import kotlinx.coroutines.flow.Flow

/**
 * Abstraction for reading, refreshing, and observing server-driven open storage settings.
 *
 * Implementations combine REST fetch, encrypted storage, and WebSocket updates into [AppResult] and a
 * reactive [Flow].
 */
interface OpenStorageSettingsRepository {
    /**
     * Returns in-memory or stored settings when present; otherwise loads from storage or refreshes from the network.
     *
     * @return [AppResult.Success] with current [OpenStorageSettings], or [AppResult.Error] when loading fails.
     */
    suspend fun getOpenStorageSettings(): AppResult<OpenStorageSettings>

    /**
     * Loads the latest storage settings from the network and persists them.
     *
     * @return [AppResult.Success] with updated [OpenStorageSettings], or [AppResult.Error] on failure.
     */
    suspend fun refreshOpenStorageSettings(): AppResult<OpenStorageSettings>

    /**
     * Applies an update (for example from a parsed WebSocket payload) and persists it.
     *
     * @param storageSettings New settings snapshot to store and expose to observers.
     */
    suspend fun updateOpenStorageSettings(storageSettings: OpenStorageSettings)

    /**
     * Observes the current settings snapshot; emits `null` until a value is known.
     *
     * @return Cold [Flow] of the latest [OpenStorageSettings] or `null`.
     */
    fun observeOpenStorageSettings(): Flow<OpenStorageSettings?>
}
