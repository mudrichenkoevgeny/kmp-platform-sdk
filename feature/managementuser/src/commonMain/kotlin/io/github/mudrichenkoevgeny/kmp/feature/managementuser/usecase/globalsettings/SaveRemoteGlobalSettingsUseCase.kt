package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.globalsettings.ManagementGlobalSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.ManagementGlobalSettings

/**
 * Pushes updated global settings to the remote server.
 *
 * @param managementGlobalSettingsRepository Management global settings repository.
 */
class SaveRemoteGlobalSettingsUseCase(
    private val managementGlobalSettingsRepository: ManagementGlobalSettingsRepository
) {
    suspend operator fun invoke(globalSettings: ManagementGlobalSettings): AppResult<Unit> {
        return managementGlobalSettingsRepository.saveRemoteManagementGlobalSettings(globalSettings)
    }
}
