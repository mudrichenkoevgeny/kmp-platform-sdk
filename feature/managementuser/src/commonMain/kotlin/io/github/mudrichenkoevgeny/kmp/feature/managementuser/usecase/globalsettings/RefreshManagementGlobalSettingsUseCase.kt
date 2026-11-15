package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.globalsettings.ManagementGlobalSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.ManagementGlobalSettings

/**
 * Forces a network reload of global settings.
 *
 * @param managementGlobalSettingsRepository Management global settings repository.
 */
public class RefreshManagementGlobalSettingsUseCase(
    private val managementGlobalSettingsRepository: ManagementGlobalSettingsRepository
) {
    /** Forces a network refresh of management global settings. */
    public suspend operator fun invoke(): AppResult<ManagementGlobalSettings> =
        managementGlobalSettingsRepository.refreshManagementGlobalSettings()
}
