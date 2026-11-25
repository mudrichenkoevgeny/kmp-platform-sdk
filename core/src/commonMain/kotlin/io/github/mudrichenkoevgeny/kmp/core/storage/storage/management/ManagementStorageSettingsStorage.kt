package io.github.mudrichenkoevgeny.kmp.core.storage.storage.management

import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.ManagementStorageSettings

/**
 * Storage interface for persisting management storage settings.
 */
public interface ManagementStorageSettingsStorage {
    /** Retrieves the persisted management storage settings, or null if none exist. */
    public suspend fun getManagementStorageSettings(): ManagementStorageSettings?

    /** Updates the persisted management storage settings with [managementStorageSettings]. */
    public suspend fun updateManagementStorageSettings(managementStorageSettings: ManagementStorageSettings)

    /** Clears any persisted management storage settings. */
    public suspend fun clearManagementStorageSettings()
}
