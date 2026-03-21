package io.github.mudrichenkoevgeny.kmp.core.security.storage.securitysettings

import io.github.mudrichenkoevgeny.kmp.core.security.model.securitysettings.SecuritySettings

/**
 * Persistence port for [SecuritySettings] (encrypted or plain, depending on implementation).
 */
interface SecuritySettingsStorage {
    /**
     * @return Stored [SecuritySettings], or `null` if none are persisted yet.
     */
    suspend fun getSecuritySettings(): SecuritySettings?

    /**
     * @param securitySettings Snapshot to serialize and persist.
     */
    suspend fun updateSecuritySettings(securitySettings: SecuritySettings)

    /**
     * Removes persisted security settings, if any.
     */
    suspend fun clearSecuritySettings()
}