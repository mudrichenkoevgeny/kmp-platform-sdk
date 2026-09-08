package io.github.mudrichenkoevgeny.kmp.core.security.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.repository.OpenSecuritySettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.OpenSecuritySettings

/**
 * Use case that delegates to [OpenSecuritySettingsRepository.refreshOpenSecuritySettings].
 *
 * @param openSecuritySettingsRepository Repository performing the network refresh and persistence.
 */
class RefreshOpenSecuritySettingsUseCase(
    private val openSecuritySettingsRepository: OpenSecuritySettingsRepository
) {
    /**
     * @return [AppResult.Success] with refreshed [OpenSecuritySettings], or [AppResult.Error] on failure.
     */
    suspend operator fun invoke(): AppResult<OpenSecuritySettings> {
        return openSecuritySettingsRepository.refreshOpenSecuritySettings()
    }
}
