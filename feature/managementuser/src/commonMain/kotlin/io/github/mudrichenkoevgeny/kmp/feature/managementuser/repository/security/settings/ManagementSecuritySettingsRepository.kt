package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.security.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.ManagementSecuritySettings
import kotlinx.coroutines.flow.Flow

/**
 * Security settings repository for management: fetch, push updates, refresh, and observe.
 */
interface ManagementSecuritySettingsRepository {
    /**
     * Returns cached settings when already loaded or stored; otherwise loads from network or storage.
     *
     * @return [AppResult.Success] with [ManagementSecuritySettings], or [AppResult.Error] on failure.
     */
    suspend fun getManagementSecuritySettings(): AppResult<ManagementSecuritySettings>

    /**
     * Pushes [securitySettings] to the remote server and updates local state on success.
     *
     * @param securitySettings New security configuration payload to apply.
     * @return Empty success indicator, or an error result when update fails.
     */
    suspend fun saveRemoteManagementSecuritySettings(securitySettings: ManagementSecuritySettings): AppResult<Unit>

    /**
     * Forces a network reload and updates the observable snapshot on success.
     *
     * @return Fresh [ManagementSecuritySettings] on success, or an error result when the request fails.
     */
    suspend fun refreshManagementSecuritySettings(): AppResult<ManagementSecuritySettings>

    /**
     * Persists [securitySettings] locally and publishes them to observers.
     *
     * @param securitySettings Complete settings payload to apply.
     */
    suspend fun updateManagementSecuritySettings(securitySettings: ManagementSecuritySettings)

    /**
     * Observes the in-memory settings snapshot.
     *
     * @return [Flow] of the current [ManagementSecuritySettings] or `null`.
     */
    fun observeManagementSecuritySettings(): Flow<ManagementSecuritySettings?>
}
