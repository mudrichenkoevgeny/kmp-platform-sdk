package io.github.mudrichenkoevgeny.kmp.core.security.repository.securitysettings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.model.securitysettings.SecuritySettings
import kotlinx.coroutines.flow.Flow

/**
 * Abstraction for reading, refreshing, and observing server-driven security settings (password policy).
 *
 * Implementations combine REST fetch, encrypted storage, and WebSocket updates into [AppResult] and a
 * reactive [Flow].
 */
interface SecuritySettingsRepository {
    /**
     * Returns in-memory or stored settings when present; otherwise loads from storage or refreshes from the network.
     *
     * @return [AppResult.Success] with current [SecuritySettings], or [AppResult.Error] when loading fails.
     */
    suspend fun getSecuritySettings(): AppResult<SecuritySettings>

    /**
     * Loads the latest security settings from the network and persists them.
     *
     * @return [AppResult.Success] with updated [SecuritySettings], or [AppResult.Error] on failure.
     */
    suspend fun refreshSecuritySettings(): AppResult<SecuritySettings>

    /**
     * Applies an update (for example from a parsed WebSocket payload) and persists it.
     *
     * @param securitySettings New settings snapshot to store and expose to observers.
     */
    suspend fun updateSecuritySettings(securitySettings: SecuritySettings)

    /**
     * Observes the current settings snapshot; emits `null` until a value is known.
     *
     * @return Cold [Flow] of the latest [SecuritySettings] or `null`.
     */
    fun observeSecuritySettings(): Flow<SecuritySettings?>
}