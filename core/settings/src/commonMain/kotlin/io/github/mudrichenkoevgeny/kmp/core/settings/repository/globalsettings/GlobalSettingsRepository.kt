package io.github.mudrichenkoevgeny.kmp.core.settings.repository.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.settings.model.globalsettings.GlobalSettings
import kotlinx.coroutines.flow.Flow

/**
 * Abstraction for reading, refreshing, and observing global application settings.
 *
 * Implementations typically combine REST fetch, encrypted local storage, and WebSocket-driven
 * updates into a single [AppResult]-based API and a reactive [Flow].
 */
interface GlobalSettingsRepository {
    /**
     * Returns in-memory or stored settings when present; otherwise performs a network refresh.
     *
     * @return [AppResult.Success] with current [GlobalSettings], or [AppResult.Error] when loading fails.
     */
    suspend fun getGlobalSettings(): AppResult<GlobalSettings>

    /**
     * Loads the latest global settings from the network and persists them.
     *
     * @return [AppResult.Success] with updated [GlobalSettings], or [AppResult.Error] on failure.
     */
    suspend fun refreshGlobalSettings(): AppResult<GlobalSettings>

    /**
     * Applies an update (for example from a parsed WebSocket payload) and persists it.
     *
     * @param globalSettings New settings snapshot to store and expose to observers.
     */
    suspend fun updateGlobalSettings(globalSettings: GlobalSettings)

    /**
     * Observes the current settings snapshot; emits `null` until a value is known.
     *
     * @return Cold [Flow] of the latest [GlobalSettings] or `null`.
     */
    fun observeGlobalSettings(): Flow<GlobalSettings?>
}