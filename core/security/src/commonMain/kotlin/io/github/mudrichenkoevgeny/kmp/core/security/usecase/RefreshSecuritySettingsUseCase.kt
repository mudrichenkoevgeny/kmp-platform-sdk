package io.github.mudrichenkoevgeny.kmp.core.security.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.model.securitysettings.SecuritySettings
import io.github.mudrichenkoevgeny.kmp.core.security.repository.securitysettings.SecuritySettingsRepository

class RefreshSecuritySettingsUseCase(
    private val securitySettingsRepository: SecuritySettingsRepository
) {
    suspend operator fun invoke(): AppResult<SecuritySettings> {
        return securitySettingsRepository.refreshSecuritySettings()
    }
}