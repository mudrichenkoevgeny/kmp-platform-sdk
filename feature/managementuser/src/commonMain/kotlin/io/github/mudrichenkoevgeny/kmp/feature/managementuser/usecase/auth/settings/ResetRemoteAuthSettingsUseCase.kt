package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.settings.ManagementAuthSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.ManagementAuthSettings

/**
 * Resets management authentication settings to default values remotely and updates local state.
 *
 * @param managementAuthSettingsRepository Remote management auth settings API.
 */
class ResetRemoteAuthSettingsUseCase(
    private val managementAuthSettingsRepository: ManagementAuthSettingsRepository
) {
    /**
     * Resets auth settings to defaults on backend.
     *
     * @return Updated default [ManagementAuthSettings] on success, or an error result when the remote reset fails.
     */
    suspend operator fun invoke(): AppResult<ManagementAuthSettings> {
        return managementAuthSettingsRepository.resetRemoteManagementAuthSettings()
    }
}
