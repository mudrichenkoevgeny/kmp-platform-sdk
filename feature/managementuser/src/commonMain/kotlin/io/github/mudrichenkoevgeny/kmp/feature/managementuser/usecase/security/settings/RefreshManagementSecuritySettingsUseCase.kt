package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.security.settings.ManagementSecuritySettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.ManagementSecuritySettings

/**
 * Use case that delegates to [ManagementSecuritySettingsRepository.refreshManagementSecuritySettings].
 *
 * @param managementSecuritySettingsRepository Repository performing the network refresh and persistence.
 */
class RefreshManagementSecuritySettingsUseCase(
    private val managementSecuritySettingsRepository: ManagementSecuritySettingsRepository
) {
    /**
     * @return [AppResult.Success] with refreshed [ManagementSecuritySettings], or [AppResult.Error] on failure.
     */
    suspend operator fun invoke(): AppResult<ManagementSecuritySettings> {
        return managementSecuritySettingsRepository.refreshManagementSecuritySettings()
    }
}
