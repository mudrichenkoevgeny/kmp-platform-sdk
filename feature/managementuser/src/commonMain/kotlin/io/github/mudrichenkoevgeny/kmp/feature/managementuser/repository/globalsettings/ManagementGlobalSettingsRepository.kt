package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.ManagementGlobalSettings
import kotlinx.coroutines.flow.Flow

/**
 * Global application settings repository for management: fetch, push updates, refresh, and observe.
 */
interface ManagementGlobalSettingsRepository {
    /**
     * Returns cached settings when already loaded or stored; otherwise loads from network or storage.
     *
     * @return [AppResult.Success] with [ManagementGlobalSettings], or [AppResult.Error] on failure.
     */
    suspend fun getManagementGlobalSettings(): AppResult<ManagementGlobalSettings>

    /**
     * Pushes [globalSettings] to the remote server and updates local state on success.
     *
     * @param globalSettings New global configuration payload to apply.
     * @return Empty success indicator, or an error result when update fails.
     */
    suspend fun saveRemoteManagementGlobalSettings(globalSettings: ManagementGlobalSettings): AppResult<Unit>

    /**
     * Forces a network reload and updates the observable snapshot on success.
     *
     * @return Fresh [ManagementGlobalSettings] on success, or an error result when the request fails.
     */
    suspend fun refreshManagementGlobalSettings(): AppResult<ManagementGlobalSettings>

    /**
     * Persists [globalSettings] locally and publishes them to observers.
     *
     * @param globalSettings Complete settings payload to apply.
     */
    suspend fun updateManagementGlobalSettings(globalSettings: ManagementGlobalSettings)

    /**
     * Observes the in-memory settings snapshot.
     *
     * @return [Flow] of the current [ManagementGlobalSettings] or `null`.
     */
    fun observeManagementGlobalSettings(): Flow<ManagementGlobalSettings?>
}
