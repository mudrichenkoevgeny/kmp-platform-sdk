package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings

import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.security.settings.ManagementSecuritySettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.ManagementSecuritySettings
import kotlinx.coroutines.flow.Flow

/**
 * Observes live security settings updates.
 *
 * @param managementSecuritySettingsRepository Management security settings repository.
 */
class ObserveManagementSecuritySettingsUseCase(
    private val managementSecuritySettingsRepository: ManagementSecuritySettingsRepository
) {
    operator fun invoke(): Flow<ManagementSecuritySettings?> {
        return managementSecuritySettingsRepository.observeManagementSecuritySettings()
    }
}
