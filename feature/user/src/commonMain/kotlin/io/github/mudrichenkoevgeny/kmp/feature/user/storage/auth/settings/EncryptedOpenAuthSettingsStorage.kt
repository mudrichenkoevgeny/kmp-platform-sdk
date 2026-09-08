package io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.OpenAuthSettings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.auth.settings.toOpenAuthSettings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.auth.settings.toOpenAuthSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.settings.OpenAuthSettingsPayload

/**
 * [OpenAuthSettingsStorage] implementation using [EncryptedSettings] for persistence.
 *
 * @param encryptedSettings Host-provided encrypted settings.
 */
class EncryptedOpenAuthSettingsStorage(
    private val encryptedSettings: EncryptedSettings
) : OpenAuthSettingsStorage {

    private val json = FoundationJson

    override suspend fun getOpenAuthSettings(): OpenAuthSettings? {
        val data = encryptedSettings.get(KEY_OPEN_AUTH_SETTINGS) ?: return null
        return try {
            json.decodeFromString<OpenAuthSettingsPayload>(data).toOpenAuthSettings()
        } catch (_: Exception) {
            encryptedSettings.remove(KEY_OPEN_AUTH_SETTINGS)
            null
        }
    }

    override suspend fun updateOpenAuthSettings(openAuthSettings: OpenAuthSettings) {
        val payload = openAuthSettings.toOpenAuthSettingsPayload()
        val data = json.encodeToString(payload)
        encryptedSettings.put(KEY_OPEN_AUTH_SETTINGS, data)
    }

    override suspend fun clearOpenAuthSettings() {
        encryptedSettings.remove(KEY_OPEN_AUTH_SETTINGS)
    }

    companion object {
        private const val KEY_OPEN_AUTH_SETTINGS = "auth_open_settings"
    }
}
