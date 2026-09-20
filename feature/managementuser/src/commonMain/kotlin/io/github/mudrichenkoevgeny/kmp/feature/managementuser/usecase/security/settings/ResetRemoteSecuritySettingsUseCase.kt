package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.security.settings.ManagementSecuritySettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.ManagementSecuritySettings

/**
 * Resets management security settings to default values remotely and updates local state.
 *
 * @param managementSecuritySettingsRepository Remote management security settings API.
 */
class ResetRemoteSecuritySettingsUseCase(
    private val managementSecuritySettingsRepository: ManagementSecuritySettingsRepository
) {
    /**
     * Resets security settings to defaults on backend.
     *
     * @return Updated default [ManagementSecuritySettings] on success, or an error result when the remote reset fails.
     */
    suspend operator fun invoke(): AppResult<ManagementSecuritySettings> {
        return managementSecuritySettingsRepository.resetRemoteManagementSecuritySettings()
    }
}
