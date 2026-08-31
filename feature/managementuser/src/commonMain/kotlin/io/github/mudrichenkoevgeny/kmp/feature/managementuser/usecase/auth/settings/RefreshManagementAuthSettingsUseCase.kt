package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.settings.ManagementAuthSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.ManagementAuthSettings

/**
 * Forces a network reload of management auth settings.
 *
 * @param managementAuthSettingsRepository Remote management auth settings API.
 */
class RefreshManagementAuthSettingsUseCase(
    private val managementAuthSettingsRepository: ManagementAuthSettingsRepository
) {
    /**
     * @return Fresh [ManagementAuthSettings] on success, or an error result.
     */
    suspend operator fun invoke(): AppResult<ManagementAuthSettings> {
        return managementAuthSettingsRepository.refreshManagementAuthSettings()
    }
}
