package io.github.mudrichenkoevgeny.kmp.core.storage.storage.management

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.ManagementStorageSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.mapper.storagesettings.toManagementStorageSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.mapper.storagesettings.toManagementStorageSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.model.storagesettings.ManagementStorageSettingsPayload

/**
 * [ManagementStorageSettingsStorage] implementation using [EncryptedSettings] for persistence.
 *
 * @param encryptedSettings Host-provided encrypted settings.
 */
class EncryptedManagementStorageSettingsStorage(
    private val encryptedSettings: EncryptedSettings
) : ManagementStorageSettingsStorage {

    private val json = FoundationJson

    override suspend fun getManagementStorageSettings(): ManagementStorageSettings? {
        val data = encryptedSettings.get(KEY_MANAGEMENT_STORAGE_SETTINGS) ?: return null
        return try {
            json.decodeFromString<ManagementStorageSettingsPayload>(data).toManagementStorageSettings()
        } catch (_: Exception) {
            encryptedSettings.remove(KEY_MANAGEMENT_STORAGE_SETTINGS)
            null
        }
    }

    override suspend fun updateManagementStorageSettings(managementStorageSettings: ManagementStorageSettings) {
        val payload = managementStorageSettings.toManagementStorageSettingsPayload()
        val data = json.encodeToString(payload)
        encryptedSettings.put(KEY_MANAGEMENT_STORAGE_SETTINGS, data)
    }

    override suspend fun clearManagementStorageSettings() {
        encryptedSettings.remove(KEY_MANAGEMENT_STORAGE_SETTINGS)
    }

    companion object {
        private const val KEY_MANAGEMENT_STORAGE_SETTINGS = "storage_management_settings"
    }
}
