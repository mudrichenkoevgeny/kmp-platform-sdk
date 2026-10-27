package io.github.mudrichenkoevgeny.kmp.core.storage.storage.storagesettings

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.OpenStorageSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.mapper.storagesettings.toOpenStorageSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.mapper.storagesettings.toOpenStorageSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.model.storagesettings.OpenStorageSettingsPayload

/**
 * [OpenStorageSettingsStorage] backed by [EncryptedSettings], using shared [FoundationJson] for
 * serialization.
 *
 * @param encryptedSettings Key-value store used for the `storage_settings` entry.
 */
class EncryptedOpenStorageSettingsStorage(
    private val encryptedSettings: EncryptedSettings
) : OpenStorageSettingsStorage {

    private val json = FoundationJson

    override suspend fun getOpenStorageSettings(): OpenStorageSettings? {
        val data = encryptedSettings.get(KEY_STORAGE_SETTINGS)
            ?: return null
        return try {
            json.decodeFromString<OpenStorageSettingsPayload>(data).toOpenStorageSettings()
        } catch (_: Exception) {
            encryptedSettings.remove(KEY_STORAGE_SETTINGS)
            null
        }
    }

    override suspend fun updateOpenStorageSettings(storageSettings: OpenStorageSettings) {
        val payload = storageSettings.toOpenStorageSettingsPayload()
        val data = json.encodeToString(payload)
        encryptedSettings.put(KEY_STORAGE_SETTINGS, data)
    }

    override suspend fun clearOpenStorageSettings() {
        encryptedSettings.remove(KEY_STORAGE_SETTINGS)
    }

    companion object {
        private const val KEY_STORAGE_SETTINGS = "storage_settings"
    }
}
