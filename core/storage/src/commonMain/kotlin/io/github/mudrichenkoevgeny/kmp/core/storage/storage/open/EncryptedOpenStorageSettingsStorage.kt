package io.github.mudrichenkoevgeny.kmp.core.storage.storage.open

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.OpenStorageSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.mapper.storagesettings.toOpenStorageSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.mapper.storagesettings.toOpenStorageSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.model.storagesettings.OpenStorageSettingsPayload

/**
 * [OpenStorageSettingsStorage] implementation using [EncryptedSettings] for persistence.
 *
 * @param encryptedSettings Host-provided encrypted settings.
 */
class EncryptedOpenStorageSettingsStorage(
    private val encryptedSettings: EncryptedSettings
) : OpenStorageSettingsStorage {

    private val json = FoundationJson

    override suspend fun getOpenStorageSettings(): OpenStorageSettings? {
        val data = encryptedSettings.get(KEY_OPEN_STORAGE_SETTINGS) ?: return null
        return try {
            json.decodeFromString<OpenStorageSettingsPayload>(data).toOpenStorageSettings()
        } catch (_: Exception) {
            encryptedSettings.remove(KEY_OPEN_STORAGE_SETTINGS)
            null
        }
    }

    override suspend fun updateOpenStorageSettings(openStorageSettings: OpenStorageSettings) {
        val payload = openStorageSettings.toOpenStorageSettingsPayload()
        val data = json.encodeToString(payload)
        encryptedSettings.put(KEY_OPEN_STORAGE_SETTINGS, data)
    }

    override suspend fun clearOpenStorageSettings() {
        encryptedSettings.remove(KEY_OPEN_STORAGE_SETTINGS)
    }

    companion object {
        private const val KEY_OPEN_STORAGE_SETTINGS = "storage_open_settings"
    }
}
