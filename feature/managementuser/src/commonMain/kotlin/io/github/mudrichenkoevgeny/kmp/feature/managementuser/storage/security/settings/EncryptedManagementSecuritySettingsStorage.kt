package io.github.mudrichenkoevgeny.kmp.feature.managementuser.storage.security.settings

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.ManagementSecuritySettings
import io.github.mudrichenkoevgeny.shared.foundation.core.security.mapper.securitysettings.toManagementSecuritySettings
import io.github.mudrichenkoevgeny.shared.foundation.core.security.mapper.securitysettings.toManagementSecuritySettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.securitysettings.ManagementSecuritySettingsPayload

/**
 * [ManagementSecuritySettingsStorage] implementation using [EncryptedSettings] for persistence.
 *
 * @param encryptedSettings Host-provided encrypted settings.
 */
class EncryptedManagementSecuritySettingsStorage(
    private val encryptedSettings: EncryptedSettings
) : ManagementSecuritySettingsStorage {

    private val json = FoundationJson

    override suspend fun getManagementSecuritySettings(): ManagementSecuritySettings? {
        val data = encryptedSettings.get(KEY_MANAGEMENT_SECURITY_SETTINGS) ?: return null
        return try {
            json.decodeFromString<ManagementSecuritySettingsPayload>(data).toManagementSecuritySettings()
        } catch (_: Exception) {
            encryptedSettings.remove(KEY_MANAGEMENT_SECURITY_SETTINGS)
            null
        }
    }

    override suspend fun updateManagementSecuritySettings(managementSecuritySettings: ManagementSecuritySettings) {
        val payload = managementSecuritySettings.toManagementSecuritySettingsPayload()
        val data = json.encodeToString(payload)
        encryptedSettings.put(KEY_MANAGEMENT_SECURITY_SETTINGS, data)
    }

    override suspend fun clearManagementSecuritySettings() {
        encryptedSettings.remove(KEY_MANAGEMENT_SECURITY_SETTINGS)
    }

    companion object {
        private const val KEY_MANAGEMENT_SECURITY_SETTINGS = "security_management_settings"
    }
}
