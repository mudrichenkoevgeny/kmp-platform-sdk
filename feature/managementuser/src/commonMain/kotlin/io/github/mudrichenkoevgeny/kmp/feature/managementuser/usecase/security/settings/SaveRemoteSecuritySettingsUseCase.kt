package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.security.settings.ManagementSecuritySettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.ManagementSecuritySettings

/**
 * Pushes updated security settings to the remote server.
 *
 * @param managementSecuritySettingsRepository Management security settings repository.
 */
class SaveRemoteSecuritySettingsUseCase(
    private val managementSecuritySettingsRepository: ManagementSecuritySettingsRepository
) {
    suspend operator fun invoke(securitySettings: ManagementSecuritySettings): AppResult<Unit> {
        return managementSecuritySettingsRepository.saveRemoteManagementSecuritySettings(securitySettings)
    }
}
