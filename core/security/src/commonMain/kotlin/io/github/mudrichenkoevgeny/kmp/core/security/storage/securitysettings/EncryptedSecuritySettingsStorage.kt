package io.github.mudrichenkoevgeny.kmp.core.security.storage.securitysettings

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.SecuritySettings
import io.github.mudrichenkoevgeny.shared.foundation.core.security.mapper.securitysettings.toSecuritySettings
import io.github.mudrichenkoevgeny.shared.foundation.core.security.mapper.securitysettings.toSecuritySettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.securitysettings.SecuritySettingsPayload

/**
 * [SecuritySettingsStorage] backed by [EncryptedSettings], using shared [FoundationJson] for
 * serialization.
 *
 * @param encryptedSettings Key-value store used for the `security_settings` entry.
 */
class EncryptedSecuritySettingsStorage(
    private val encryptedSettings: EncryptedSettings
) : SecuritySettingsStorage {

    private val json = FoundationJson

    override suspend fun getSecuritySettings(): SecuritySettings? {
        val data = encryptedSettings.get(KEY_SECURITY_SETTINGS)
            ?: return null
        return try {
            json.decodeFromString<SecuritySettingsPayload>(data).toSecuritySettings()
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun updateSecuritySettings(securitySettings: SecuritySettings) {
        val payload = securitySettings.toSecuritySettingsPayload()
        val data = json.encodeToString(payload)
        encryptedSettings.put(KEY_SECURITY_SETTINGS, data)
    }

    override suspend fun clearSecuritySettings() {
        encryptedSettings.remove(KEY_SECURITY_SETTINGS)
    }

    companion object {
        private const val KEY_SECURITY_SETTINGS = "security_settings"
    }
}