package io.github.mudrichenkoevgeny.kmp.core.security.storage.securitysettings

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.OpenSecuritySettings
import io.github.mudrichenkoevgeny.shared.foundation.core.security.mapper.securitysettings.toOpenSecuritySettings
import io.github.mudrichenkoevgeny.shared.foundation.core.security.mapper.securitysettings.toOpenSecuritySettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.securitysettings.OpenSecuritySettingsPayload

/**
 * [OpenSecuritySettingsStorage] backed by [EncryptedSettings], using shared [FoundationJson] for
 * serialization.
 *
 * @param encryptedSettings Key-value store used for the `security_settings` entry.
 */
class EncryptedOpenSecuritySettingsStorage(
    private val encryptedSettings: EncryptedSettings
) : OpenSecuritySettingsStorage {

    private val json = FoundationJson

    override suspend fun getOpenSecuritySettings(): OpenSecuritySettings? {
        val data = encryptedSettings.get(KEY_SECURITY_SETTINGS)
            ?: return null
        return try {
            json.decodeFromString<OpenSecuritySettingsPayload>(data).toOpenSecuritySettings()
        } catch (_: Exception) {
            encryptedSettings.remove(KEY_SECURITY_SETTINGS)
            null
        }
    }

    override suspend fun updateOpenSecuritySettings(securitySettings: OpenSecuritySettings) {
        val payload = securitySettings.toOpenSecuritySettingsPayload()
        val data = json.encodeToString(payload)
        encryptedSettings.put(KEY_SECURITY_SETTINGS, data)
    }

    override suspend fun clearOpenSecuritySettings() {
        encryptedSettings.remove(KEY_SECURITY_SETTINGS)
    }

    companion object {
        private const val KEY_SECURITY_SETTINGS = "security_settings"
    }
}
