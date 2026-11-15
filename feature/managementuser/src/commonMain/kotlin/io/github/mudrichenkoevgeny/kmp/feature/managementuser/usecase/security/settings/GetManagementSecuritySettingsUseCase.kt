package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.security.settings.ManagementSecuritySettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.ManagementSecuritySettings

/**
 * Returns cached security settings or fetches them from the server.
 *
 * @param managementSecuritySettingsRepository Management security settings API.
 */
public class GetManagementSecuritySettingsUseCase(
    private val managementSecuritySettingsRepository: ManagementSecuritySettingsRepository
) {
    /** Retrieves management security settings. */
    public suspend operator fun invoke(): AppResult<ManagementSecuritySettings> =
        managementSecuritySettingsRepository.getManagementSecuritySettings()
}
