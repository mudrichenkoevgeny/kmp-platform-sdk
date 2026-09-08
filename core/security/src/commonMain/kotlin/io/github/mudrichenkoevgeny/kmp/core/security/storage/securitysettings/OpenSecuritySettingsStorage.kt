package io.github.mudrichenkoevgeny.kmp.core.security.storage.securitysettings

import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.OpenSecuritySettings

/**
 * Persistence port for [OpenSecuritySettings] (encrypted or plain, depending on implementation).
 */
interface OpenSecuritySettingsStorage {
    /**
     * @return Stored [OpenSecuritySettings], or `null` if none are persisted yet.
     */
    suspend fun getOpenSecuritySettings(): OpenSecuritySettings?

    /**
     * @param securitySettings Snapshot to serialize and persist.
     */
    suspend fun updateOpenSecuritySettings(securitySettings: OpenSecuritySettings)

    /**
     * Removes persisted open security settings, if any.
     */
    suspend fun clearOpenSecuritySettings()
}
