package io.github.mudrichenkoevgeny.kmp.feature.managementuser.storage.security.settings

import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.ManagementSecuritySettings

/**
 * Persistence port for [ManagementSecuritySettings].
 */
public interface ManagementSecuritySettingsStorage {
    /** @return Stored [ManagementSecuritySettings], or `null` if none are persisted yet. */
    public suspend fun getManagementSecuritySettings(): ManagementSecuritySettings?

    /** @param managementSecuritySettings Snapshot to serialize and persist. */
    public suspend fun updateManagementSecuritySettings(managementSecuritySettings: ManagementSecuritySettings)

    /** Removes persisted management security settings, if any. */
    public suspend fun clearManagementSecuritySettings()
}
