package io.github.mudrichenkoevgeny.kmp.core.security.repository

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.OpenSecuritySettings
import kotlinx.coroutines.flow.Flow

/**
 * Abstraction for reading, refreshing, and observing server-driven open security settings.
 *
 * Implementations combine REST fetch, encrypted storage, and WebSocket updates into [AppResult] and a
 * reactive [Flow].
 */
interface OpenSecuritySettingsRepository {
    /**
     * Returns in-memory or stored settings when present; otherwise loads from storage or refreshes from the network.
     *
     * @return [AppResult.Success] with current [OpenSecuritySettings], or [AppResult.Error] when loading fails.
     */
    suspend fun getOpenSecuritySettings(): AppResult<OpenSecuritySettings>

    /**
     * Loads the latest security settings from the network and persists them.
     *
     * @return [AppResult.Success] with updated [OpenSecuritySettings], or [AppResult.Error] on failure.
     */
    suspend fun refreshOpenSecuritySettings(): AppResult<OpenSecuritySettings>

    /**
     * Applies an update (for example from a parsed WebSocket payload) and persists it.
     *
     * @param securitySettings New settings snapshot to store and expose to observers.
     */
    suspend fun updateOpenSecuritySettings(securitySettings: OpenSecuritySettings)

    /**
     * Observes the current settings snapshot; emits `null` until a value is known.
     *
     * @return Cold [Flow] of the latest [OpenSecuritySettings] or `null`.
     */
    fun observeOpenSecuritySettings(): Flow<OpenSecuritySettings?>
}
