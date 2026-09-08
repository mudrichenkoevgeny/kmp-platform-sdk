package io.github.mudrichenkoevgeny.kmp.feature.managementuser.storage.auth.settings

import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.ManagementAuthSettings

/**
 * Persists cached management auth settings.
 */
interface ManagementAuthSettingsStorage {
    /** @return Last known management auth settings snapshot, or null if never loaded. */
    suspend fun getManagementAuthSettings(): ManagementAuthSettings?

    /** @param managementAuthSettings Replaces cached management auth settings. */
    suspend fun updateManagementAuthSettings(managementAuthSettings: ManagementAuthSettings)

    /** Clears cached management auth settings. */
    suspend fun clearManagementAuthSettings()
}
