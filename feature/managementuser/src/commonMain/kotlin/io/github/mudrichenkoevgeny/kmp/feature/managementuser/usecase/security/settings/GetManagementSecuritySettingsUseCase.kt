package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.security.settings.ManagementSecuritySettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.ManagementSecuritySettings

/**
 * Returns cached security settings or fetches them from the server.
 *
 * @param managementSecuritySettingsRepository Management security settings API.
 */
class GetManagementSecuritySettingsUseCase(
    private val managementSecuritySettingsRepository: ManagementSecuritySettingsRepository
) {
    suspend operator fun invoke(): AppResult<ManagementSecuritySettings> {
        return managementSecuritySettingsRepository.getManagementSecuritySettings()
    }
}
