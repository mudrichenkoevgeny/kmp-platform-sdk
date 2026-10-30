package io.github.mudrichenkoevgeny.kmp.core.storage.storage.management

import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.ManagementStorageSettings

/**
 * Storage interface for persisting management storage settings.
 */
interface ManagementStorageSettingsStorage {
    suspend fun getManagementStorageSettings(): ManagementStorageSettings?
    suspend fun updateManagementStorageSettings(managementStorageSettings: ManagementStorageSettings)
    suspend fun clearManagementStorageSettings()
}
