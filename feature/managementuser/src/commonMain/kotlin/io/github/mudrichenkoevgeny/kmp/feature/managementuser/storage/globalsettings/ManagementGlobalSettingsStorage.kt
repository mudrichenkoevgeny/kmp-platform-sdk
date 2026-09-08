package io.github.mudrichenkoevgeny.kmp.feature.managementuser.storage.globalsettings

import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.ManagementGlobalSettings

/**
 * Persistence port for [ManagementGlobalSettings].
 */
interface ManagementGlobalSettingsStorage {
    /** @return Stored [ManagementGlobalSettings], or `null` if none are persisted yet. */
    suspend fun getManagementGlobalSettings(): ManagementGlobalSettings?

    /** @param managementGlobalSettings Snapshot to serialize and persist. */
    suspend fun updateManagementGlobalSettings(managementGlobalSettings: ManagementGlobalSettings)

    /** Removes persisted management global settings, if any. */
    suspend fun clearManagementGlobalSettings()
}
