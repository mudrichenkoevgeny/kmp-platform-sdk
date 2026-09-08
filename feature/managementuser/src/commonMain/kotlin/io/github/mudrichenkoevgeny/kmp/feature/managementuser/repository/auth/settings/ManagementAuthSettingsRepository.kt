package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.ManagementAuthSettings
import kotlinx.coroutines.flow.Flow

/**
 * Auth-related remote settings: fetch, refresh, push updates, and observe the latest snapshot.
 */
interface ManagementAuthSettingsRepository {
    /**
     * Returns cached settings when already loaded or stored; otherwise loads from the network or
     * storage as implemented.
     *
     * @return [AppResult.Success] with [ManagementAuthSettings], or [AppResult.Error] when load fails.
     */
    suspend fun getManagementAuthSettings(): AppResult<ManagementAuthSettings>

    /**
     * Pushes [authSettings] to the remote server and updates the local storage and state flow on success.
     *
     * @param authSettings New configuration payload to apply.
     * @return Empty success indicator, or an error result when the remote update fails.
     */
    suspend fun saveRemoteManagementAuthSettings(authSettings: ManagementAuthSettings): AppResult<Unit>

    /**
     * Forces a network reload and updates the observable snapshot on success.
     *
     * @return Fresh [ManagementAuthSettings] on success, or an error result when the request fails.
     */
    suspend fun refreshManagementAuthSettings(): AppResult<ManagementAuthSettings>

    /**
     * Persists [authSettings] and publishes them to observers. Failures are not represented as
     * [AppResult]; they surface via the underlying storage or coroutine error channel.
     *
     * @param authSettings Complete settings payload to apply locally and in persistence.
     */
    suspend fun updateManagementAuthSettings(authSettings: ManagementAuthSettings)

    /**
     * Observes the in-memory settings snapshot (including `null` before the first successful load).
     *
     * @return [Flow] of the current [ManagementAuthSettings] or `null`.
     */
    fun observeManagementAuthSettings(): Flow<ManagementAuthSettings?>
}
