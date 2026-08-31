package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.settings.ManagementAuthSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.ManagementAuthSettings

/**
 * Pushes new auth settings to the remote server.
 *
 * @param managementAuthSettingsRepository Remote management auth settings API.
 */
class SaveRemoteAuthSettingsUseCase(
    private val managementAuthSettingsRepository: ManagementAuthSettingsRepository
) {
    /**
     * @param authSettings New configuration payload to apply.
     * @return Empty success indicator, or an error result when the remote update fails.
     */
    suspend operator fun invoke(authSettings: ManagementAuthSettings): AppResult<Unit> {
        return managementAuthSettingsRepository.saveRemoteAuthSettings(authSettings)
    }
}
