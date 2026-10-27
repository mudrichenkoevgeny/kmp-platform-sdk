package io.github.mudrichenkoevgeny.kmp.core.storage.storage.storagesettings

import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.OpenStorageSettings

/**
 * Persistence port for [OpenStorageSettings] (encrypted or plain, depending on implementation).
 */
interface OpenStorageSettingsStorage {
    /**
     * @return Stored [OpenStorageSettings], or `null` if none are persisted yet.
     */
    suspend fun getOpenStorageSettings(): OpenStorageSettings?

    /**
     * @param storageSettings Snapshot to serialize and persist.
     */
    suspend fun updateOpenStorageSettings(storageSettings: OpenStorageSettings)

    /**
     * Removes persisted open storage settings, if any.
     */
    suspend fun clearOpenStorageSettings()
}
