package io.github.mudrichenkoevgeny.kmp.core.settings.repository

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.OpenGlobalSettings
import kotlinx.coroutines.flow.Flow

/**
 * Abstraction for reading, refreshing, and observing open global application settings.
 *
 * Implementations typically combine REST fetch, encrypted local storage, and WebSocket-driven
 * updates into a single [AppResult]-based API and a reactive [Flow].
 */
interface OpenGlobalSettingsRepository {
    /**
     * Returns in-memory or stored settings when present; otherwise performs a network refresh.
     *
     * @return [AppResult.Success] with current [OpenGlobalSettings], or [AppResult.Error] when loading fails.
     */
    suspend fun getOpenGlobalSettings(): AppResult<OpenGlobalSettings>

    /**
     * Loads the latest global settings from the network and persists them.
     *
     * @return [AppResult.Success] with updated [OpenGlobalSettings], or [AppResult.Error] on failure.
     */
    suspend fun refreshOpenGlobalSettings(): AppResult<OpenGlobalSettings>

    /**
     * Applies an update (for example from a parsed WebSocket payload) and persists it.
     *
     * @param globalSettings New settings snapshot to store and expose to observers.
     */
    suspend fun updateOpenGlobalSettings(globalSettings: OpenGlobalSettings)

    /**
     * Observes the current settings snapshot; emits `null` until a value is known.
     *
     * @return Cold [Flow] of the latest [OpenGlobalSettings] or `null`.
     */
    fun observeOpenGlobalSettings(): Flow<OpenGlobalSettings?>
}
