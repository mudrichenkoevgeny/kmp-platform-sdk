package io.github.mudrichenkoevgeny.kmp.core.storage.storage.open

import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.OpenStorageSettings

/**
 * Storage interface for persisting open storage settings.
 */
public interface OpenStorageSettingsStorage {
    /** Retrieves the persisted open storage settings, or null if none exist. */
    public suspend fun getOpenStorageSettings(): OpenStorageSettings?

    /** Updates the persisted open storage settings with [openStorageSettings]. */
    public suspend fun updateOpenStorageSettings(openStorageSettings: OpenStorageSettings)

    /** Clears any persisted open storage settings. */
    public suspend fun clearOpenStorageSettings()
}
