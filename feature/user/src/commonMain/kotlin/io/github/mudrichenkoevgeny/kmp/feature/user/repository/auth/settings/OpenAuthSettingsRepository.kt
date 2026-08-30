package io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.PublicAuthSettings
import kotlinx.coroutines.flow.Flow

/**
 * Auth-related remote settings: fetch, refresh, push updates, and observe the latest snapshot.
 */
interface OpenAuthSettingsRepository {
    /**
     * Returns cached settings when already loaded or stored; otherwise loads from the network or
     * storage as implemented.
     *
     * @return [AppResult.Success] with [PublicAuthSettings], or [AppResult.Error] when load fails.
     */
    suspend fun getAuthSettings(): AppResult<PublicAuthSettings>

    /**
     * Forces a network reload and updates the observable snapshot on success.
     *
     * @return Fresh [PublicAuthSettings] on success, or an error result when the request fails.
     */
    suspend fun refreshAuthSettings(): AppResult<PublicAuthSettings>

    /**
     * Persists [authSettings] and publishes them to observers. Failures are not represented as
     * [AppResult]; they surface via the underlying storage or coroutine error channel.
     *
     * @param authSettings Complete settings payload to apply locally and in persistence.
     */
    suspend fun updateAuthSettings(authSettings: PublicAuthSettings)

    /**
     * Observes the in-memory settings snapshot (including `null` before the first successful load).
     *
     * @return [Flow] of the current [PublicAuthSettings] or `null`.
     */
    fun observeAuthSettings(): Flow<PublicAuthSettings?>
}