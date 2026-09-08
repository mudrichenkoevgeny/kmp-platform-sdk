package io.github.mudrichenkoevgeny.kmp.feature.managementuser.storage.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.ManagementGlobalSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.mapper.globalsettings.toManagementGlobalSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.mapper.globalsettings.toManagementGlobalSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.model.globalsettings.ManagementGlobalSettingsPayload

/**
 * [ManagementGlobalSettingsStorage] implementation using [EncryptedSettings] for persistence.
 *
 * @param encryptedSettings Host-provided encrypted settings.
 */
class EncryptedManagementGlobalSettingsStorage(
    private val encryptedSettings: EncryptedSettings
) : ManagementGlobalSettingsStorage {

    private val json = FoundationJson

    override suspend fun getManagementGlobalSettings(): ManagementGlobalSettings? {
        val data = encryptedSettings.get(KEY_MANAGEMENT_GLOBAL_SETTINGS) ?: return null
        return try {
            json.decodeFromString<ManagementGlobalSettingsPayload>(data).toManagementGlobalSettings()
        } catch (_: Exception) {
            encryptedSettings.remove(KEY_MANAGEMENT_GLOBAL_SETTINGS)
            null
        }
    }

    override suspend fun updateManagementGlobalSettings(managementGlobalSettings: ManagementGlobalSettings) {
        val payload = managementGlobalSettings.toManagementGlobalSettingsPayload()
        val data = json.encodeToString(payload)
        encryptedSettings.put(KEY_MANAGEMENT_GLOBAL_SETTINGS, data)
    }

    override suspend fun clearManagementGlobalSettings() {
        encryptedSettings.remove(KEY_MANAGEMENT_GLOBAL_SETTINGS)
    }

    companion object {
        private const val KEY_MANAGEMENT_GLOBAL_SETTINGS = "global_management_settings"
    }
}
