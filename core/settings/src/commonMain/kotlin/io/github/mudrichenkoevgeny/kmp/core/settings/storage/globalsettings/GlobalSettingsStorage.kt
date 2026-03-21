package io.github.mudrichenkoevgeny.kmp.core.settings.storage.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.settings.model.globalsettings.GlobalSettings

/**
 * Persistence port for [GlobalSettings] (encrypted or plain, depending on implementation).
 */
interface GlobalSettingsStorage {
    /**
     * @return Stored [GlobalSettings], or `null` if none are persisted yet.
     */
    suspend fun getGlobalSettings(): GlobalSettings?

    /**
     * @param globalSettings Snapshot to serialize and persist.
     */
    suspend fun updateGlobalSettings(globalSettings: GlobalSettings)

    /**
     * Removes persisted global settings, if any.
     */
    suspend fun clearGlobalSettings()
}