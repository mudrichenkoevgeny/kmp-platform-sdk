package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.ManagementAuthSettings
import kotlinx.coroutines.flow.Flow

/**
 * Auth-related remote settings: fetch, refresh, push updates, and observe the latest snapshot.
 */
public interface ManagementAuthSettingsRepository {
    /**
     * Returns cached settings when already loaded or stored; otherwise loads from the network or
     * storage as implemented.
     *
     * @return [AppResult.Success] with [ManagementAuthSettings], or [AppResult.Error] when load fails.
     */
    public suspend fun getManagementAuthSettings(): AppResult<ManagementAuthSettings>

    /**
     * Pushes [authSettings] to the remote server and updates the local storage and state flow on success.
     *
     * @param authSettings New configuration payload to apply.
     * @return Updated [ManagementAuthSettings] on success, or an error result when the remote update fails.
     */
    public suspend fun saveRemoteManagementAuthSettings(
        authSettings: ManagementAuthSettings
    ): AppResult<ManagementAuthSettings>

    /**
     * Resets auth settings on the remote server to default values and updates local state.
     *
     * @return Fresh [ManagementAuthSettings] on success, or an error result when the remote reset fails.
     */
    public suspend fun resetRemoteManagementAuthSettings(): AppResult<ManagementAuthSettings>

    /**
     * Forces a network reload and updates the observable snapshot on success.
     *
     * @return Fresh [ManagementAuthSettings] on success, or an error result when the request fails.
     */
    public suspend fun refreshManagementAuthSettings(): AppResult<ManagementAuthSettings>

    /**
     * Persists [authSettings] and publishes them to observers. Failures are not represented as
     * [AppResult]; they surface via the underlying storage or coroutine error channel.
     *
     * @param authSettings Complete settings payload to apply locally and in persistence.
     */
    public suspend fun updateManagementAuthSettings(authSettings: ManagementAuthSettings)

    /**
     * Observes the in-memory settings snapshot (including `null` before the first successful load).
     *
     * @return [Flow] of the current [ManagementAuthSettings] or `null`.
     */
    public fun observeManagementAuthSettings(): Flow<ManagementAuthSettings?>
}
