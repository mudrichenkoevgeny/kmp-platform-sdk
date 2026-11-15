package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.globalsettings.ManagementGlobalSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.ManagementGlobalSettings

/**
 * Pushes updated global settings to the remote server.
 *
 * @param managementGlobalSettingsRepository Management global settings repository.
 */
public class SaveRemoteGlobalSettingsUseCase(
    private val managementGlobalSettingsRepository: ManagementGlobalSettingsRepository
) {
    /** Saves remote management global settings. */
    public suspend operator fun invoke(globalSettings: ManagementGlobalSettings): AppResult<ManagementGlobalSettings> =
        managementGlobalSettingsRepository.saveRemoteManagementGlobalSettings(
            globalSettings
        )
}
