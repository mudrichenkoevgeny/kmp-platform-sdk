package io.github.mudrichenkoevgeny.kmp.core.settings.storage.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.OpenGlobalSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.mapper.globalsettings.toOpenGlobalSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.mapper.globalsettings.toOpenGlobalSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.model.globalsettings.OpenGlobalSettingsPayload

/**
 * [OpenGlobalSettingsStorage] implementation that uses [EncryptedSettings] for persistence.
 *
 * Data is serialized using [FoundationJson] before being stored.
 *
 * @param encryptedSettings Key-value store used for the `global_settings` entry.
 */
class EncryptedOpenGlobalSettingsStorage(
    private val encryptedSettings: EncryptedSettings
) : OpenGlobalSettingsStorage {

    private val json = FoundationJson

    override suspend fun getOpenGlobalSettings(): OpenGlobalSettings? {
        val data = encryptedSettings.get(KEY_GLOBAL_SETTINGS)
            ?: return null
        return try {
            json.decodeFromString<OpenGlobalSettingsPayload>(data).toOpenGlobalSettings()
        } catch (_: Exception) {
            encryptedSettings.remove(KEY_GLOBAL_SETTINGS)
            null
        }
    }

    override suspend fun updateOpenGlobalSettings(globalSettings: OpenGlobalSettings) {
        val payload = globalSettings.toOpenGlobalSettingsPayload()
        val data = json.encodeToString(payload)
        encryptedSettings.put(KEY_GLOBAL_SETTINGS, data)
    }

    override suspend fun clearOpenGlobalSettings() {
        encryptedSettings.remove(KEY_GLOBAL_SETTINGS)
    }

    companion object {
        private const val KEY_GLOBAL_SETTINGS = "global_settings"
    }
}
