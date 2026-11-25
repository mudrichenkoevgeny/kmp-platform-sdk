package io.github.mudrichenkoevgeny.kmp.core.settings.storage.globalsettings

import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.OpenGlobalSettings

/**
 * Persistence port for [OpenGlobalSettings] (encrypted or plain, depending on implementation).
 */
public interface OpenGlobalSettingsStorage {
    /**
     * @return Stored [OpenGlobalSettings], or `null` if none are persisted yet.
     */
    public suspend fun getOpenGlobalSettings(): OpenGlobalSettings?

    /**
     * @param globalSettings Snapshot to serialize and persist.
     */
    public suspend fun updateOpenGlobalSettings(globalSettings: OpenGlobalSettings)

    /**
     * Removes persisted open global settings, if any.
     */
    public suspend fun clearOpenGlobalSettings()
}
