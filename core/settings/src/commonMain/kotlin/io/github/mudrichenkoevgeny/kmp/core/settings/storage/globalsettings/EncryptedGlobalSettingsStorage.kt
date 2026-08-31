package io.github.mudrichenkoevgeny.kmp.core.settings.storage.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.GlobalSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.mapper.globalsettings.toGlobalSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.mapper.globalsettings.toGlobalSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.model.globalsettings.GlobalSettingsPayload

/**
 * [GlobalSettingsStorage] implementation that uses [EncryptedSettings] for persistence.
 *
 * Data is serialized using [FoundationJson] before being stored.
 *
 * @param encryptedSettings Key-value store used for the `global_settings` entry.
 */
class EncryptedGlobalSettingsStorage(
    private val encryptedSettings: EncryptedSettings
) : GlobalSettingsStorage {

    private val json = FoundationJson

    override suspend fun getGlobalSettings(): GlobalSettings? {
        val data = encryptedSettings.get(KEY_GLOBAL_SETTINGS)
            ?: return null
        return try {
            json.decodeFromString<GlobalSettingsPayload>(data).toGlobalSettings()
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun updateGlobalSettings(globalSettings: GlobalSettings) {
        val payload = globalSettings.toGlobalSettingsPayload()
        val data = json.encodeToString(payload)
        encryptedSettings.put(KEY_GLOBAL_SETTINGS, data)
    }

    override suspend fun clearGlobalSettings() {
        encryptedSettings.remove(KEY_GLOBAL_SETTINGS)
    }

    companion object {
        private const val KEY_GLOBAL_SETTINGS = "global_settings"
    }
}