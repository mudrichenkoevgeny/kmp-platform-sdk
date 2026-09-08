package io.github.mudrichenkoevgeny.kmp.feature.managementuser.storage.security.settings

import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.ManagementSecuritySettings

/**
 * Persistence port for [ManagementSecuritySettings].
 */
interface ManagementSecuritySettingsStorage {
    /** @return Stored [ManagementSecuritySettings], or `null` if none are persisted yet. */
    suspend fun getManagementSecuritySettings(): ManagementSecuritySettings?

    /** @param managementSecuritySettings Snapshot to serialize and persist. */
    suspend fun updateManagementSecuritySettings(managementSecuritySettings: ManagementSecuritySettings)

    /** Removes persisted management security settings, if any. */
    suspend fun clearManagementSecuritySettings()
}
