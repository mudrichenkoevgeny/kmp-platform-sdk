package io.github.mudrichenkoevgeny.kmp.core.settings.storage.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.settings.model.globalsettings.GlobalSettings
import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson

/**
 * [GlobalSettingsStorage] backed by [EncryptedSettings], using shared [FoundationJson] for
 * serialization.
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
        return json.decodeFromString(data)
    }

    override suspend fun updateGlobalSettings(globalSettings: GlobalSettings) {
        val data = json.encodeToString(globalSettings)
        encryptedSettings.put(KEY_GLOBAL_SETTINGS, data)
    }

    override suspend fun clearGlobalSettings() {
        encryptedSettings.remove(KEY_GLOBAL_SETTINGS)
    }

    companion object {
        private const val KEY_GLOBAL_SETTINGS = "global_settings"
    }
}