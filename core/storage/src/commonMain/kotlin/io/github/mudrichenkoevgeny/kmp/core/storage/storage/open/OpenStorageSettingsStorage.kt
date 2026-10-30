package io.github.mudrichenkoevgeny.kmp.core.storage.storage.open

import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.OpenStorageSettings

/**
 * Storage interface for persisting open storage settings.
 */
interface OpenStorageSettingsStorage {
    suspend fun getOpenStorageSettings(): OpenStorageSettings?
    suspend fun updateOpenStorageSettings(openStorageSettings: OpenStorageSettings)
    suspend fun clearOpenStorageSettings()
}
