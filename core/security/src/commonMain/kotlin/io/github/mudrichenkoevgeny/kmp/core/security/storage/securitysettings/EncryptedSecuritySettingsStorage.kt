package io.github.mudrichenkoevgeny.kmp.core.security.storage.securitysettings

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.kmp.core.security.model.securitysettings.SecuritySettings
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson

class EncryptedSecuritySettingsStorage(
    private val encryptedSettings: EncryptedSettings
) : SecuritySettingsStorage {

    private val json = FoundationJson

    override suspend fun getSecuritySettings(): SecuritySettings? {
        val data = encryptedSettings.get(KEY_SECURITY_SETTINGS)
            ?: return null
        return json.decodeFromString(data)
    }

    override suspend fun updateSecuritySettings(securitySettings: SecuritySettings) {
        val data = json.encodeToString(securitySettings)
        encryptedSettings.put(KEY_SECURITY_SETTINGS, data)
    }

    override suspend fun clearSecuritySettings() {
        encryptedSettings.remove(KEY_SECURITY_SETTINGS)
    }

    companion object {
        private const val KEY_SECURITY_SETTINGS = "security_settings"
    }
}