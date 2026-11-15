package io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.settings

import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.OpenAuthSettings

/**
 * Persists cached open auth settings.
 */
public interface OpenAuthSettingsStorage {
    /** @return Last known open auth settings snapshot, or null if never loaded. */
    public suspend fun getOpenAuthSettings(): OpenAuthSettings?

    /** @param openAuthSettings Replaces cached open provider/policy settings. */
    public suspend fun updateOpenAuthSettings(openAuthSettings: OpenAuthSettings)

    /** Clears cached open auth settings. */
    public suspend fun clearOpenAuthSettings()
}
