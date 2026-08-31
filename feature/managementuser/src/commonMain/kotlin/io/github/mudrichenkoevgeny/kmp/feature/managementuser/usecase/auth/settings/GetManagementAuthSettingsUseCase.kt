package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.settings.ManagementAuthSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.ManagementAuthSettings

/**
 * Returns cached management auth settings when already loaded or stored; otherwise loads from the network.
 *
 * @param managementAuthSettingsRepository Remote management auth settings API.
 */
class GetManagementAuthSettingsUseCase(
    private val managementAuthSettingsRepository: ManagementAuthSettingsRepository
) {
    /**
     * @return [ManagementAuthSettings] on success, or a mapped failure.
     */
    suspend operator fun invoke(): AppResult<ManagementAuthSettings> {
        return managementAuthSettingsRepository.getManagementAuthSettings()
    }
}
