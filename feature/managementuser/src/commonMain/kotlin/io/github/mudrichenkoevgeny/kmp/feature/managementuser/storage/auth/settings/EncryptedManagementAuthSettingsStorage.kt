package io.github.mudrichenkoevgeny.kmp.feature.managementuser.storage.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.ManagementAuthSettings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.auth.settings.toManagementAuthSettings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.auth.settings.toManagementAuthSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.settings.ManagementAuthSettingsPayload

/**
 * [ManagementAuthSettingsStorage] implementation using [EncryptedSettings] for persistence.
 *
 * @param encryptedSettings Host-provided encrypted settings.
 */
class EncryptedManagementAuthSettingsStorage(
    private val encryptedSettings: EncryptedSettings
) : ManagementAuthSettingsStorage {

    private val json = FoundationJson

    override suspend fun getManagementAuthSettings(): ManagementAuthSettings? {
        val data = encryptedSettings.get(KEY_MANAGEMENT_AUTH_SETTINGS) ?: return null
        return try {
            json.decodeFromString<ManagementAuthSettingsPayload>(data).toManagementAuthSettings()
        } catch (_: Exception) {
            encryptedSettings.remove(KEY_MANAGEMENT_AUTH_SETTINGS)
            null
        }
    }

    override suspend fun updateManagementAuthSettings(managementAuthSettings: ManagementAuthSettings) {
        val payload = managementAuthSettings.toManagementAuthSettingsPayload()
        val data = json.encodeToString(payload)
        encryptedSettings.put(KEY_MANAGEMENT_AUTH_SETTINGS, data)
    }

    override suspend fun clearManagementAuthSettings() {
        encryptedSettings.remove(KEY_MANAGEMENT_AUTH_SETTINGS)
    }

    companion object {
        private const val KEY_MANAGEMENT_AUTH_SETTINGS = "auth_management_settings"
    }
}
