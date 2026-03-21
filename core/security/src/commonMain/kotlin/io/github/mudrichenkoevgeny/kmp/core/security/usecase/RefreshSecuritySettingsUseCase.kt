package io.github.mudrichenkoevgeny.kmp.core.security.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.model.securitysettings.SecuritySettings
import io.github.mudrichenkoevgeny.kmp.core.security.repository.securitysettings.SecuritySettingsRepository

/**
 * Use case that delegates to [SecuritySettingsRepository.refreshSecuritySettings].
 *
 * @param securitySettingsRepository Repository performing the network refresh and persistence.
 */
class RefreshSecuritySettingsUseCase(
    private val securitySettingsRepository: SecuritySettingsRepository
) {
    /**
     * @return [AppResult.Success] with refreshed [SecuritySettings], or [AppResult.Error] on failure.
     */
    suspend operator fun invoke(): AppResult<SecuritySettings> {
        return securitySettingsRepository.refreshSecuritySettings()
    }
}