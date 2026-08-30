package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.settings.ManagementAuthSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.ManagementAuthSettings

/**
 * Forces a network reload of auth-related settings and updates the repository’s observable state.
 *
 * @param managementAuthSettingsRepository Auth settings aggregate.
 */
class RefreshAuthSettingsUseCase(
    private val managementAuthSettingsRepository: ManagementAuthSettingsRepository
) {
    /**
     * @return Fresh [ManagementAuthSettings] on success, or an error result when the refresh request fails.
     */
    suspend operator fun invoke(): AppResult<ManagementAuthSettings> {
        return managementAuthSettingsRepository.refreshManagementAuthSettings()
    }
}