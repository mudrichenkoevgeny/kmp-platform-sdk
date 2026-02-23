package io.github.mudrichenkoevgeny.kmp.core.security.repository.securitysettings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.model.securitysettings.SecuritySettings
import kotlinx.coroutines.flow.Flow

interface SecuritySettingsRepository {
    suspend fun getSecuritySettings(): AppResult<SecuritySettings>
    suspend fun refreshSecuritySettings(): AppResult<SecuritySettings>
    suspend fun updateSecuritySettings(securitySettings: SecuritySettings)
    fun observeSecuritySettings(): Flow<SecuritySettings?>
}