package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.globalsettings.ManagementGlobalSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.ManagementGlobalSettings

/**
 * Resets management global settings to default values remotely and updates local state.
 *
 * @param managementGlobalSettingsRepository Remote management global settings API.
 */
class ResetRemoteGlobalSettingsUseCase(
    private val managementGlobalSettingsRepository: ManagementGlobalSettingsRepository
) {
    /**
     * Resets global settings to defaults on backend.
     *
     * @return Updated default [ManagementGlobalSettings] on success, or an error result when the remote reset fails.
     */
    suspend operator fun invoke(): AppResult<ManagementGlobalSettings> {
        return managementGlobalSettingsRepository.resetRemoteManagementGlobalSettings()
    }
}
