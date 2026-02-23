package io.github.mudrichenkoevgeny.kmp.core.security.storage.securitysettings

import io.github.mudrichenkoevgeny.kmp.core.security.model.securitysettings.SecuritySettings

interface SecuritySettingsStorage {
    suspend fun getSecuritySettings(): SecuritySettings?
    suspend fun updateSecuritySettings(securitySettings: SecuritySettings)
    suspend fun clearSecuritySettings()
}