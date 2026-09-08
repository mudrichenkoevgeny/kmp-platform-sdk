package io.github.mudrichenkoevgeny.kmp.feature.clientuser.usecase.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings.OpenAuthSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.OpenAuthSettings

/**
 * Forces a network reload of auth-related settings and updates the repository’s observable state.
 *
 * @param openAuthSettingsRepository Auth settings aggregate.
 */
class RefreshOpenAuthSettingsUseCase(
    private val openAuthSettingsRepository: OpenAuthSettingsRepository
) {
    /**
     * @return Fresh [OpenAuthSettings] on success, or an error result when the refresh request fails.
     */
    suspend operator fun invoke(): AppResult<OpenAuthSettings> {
        return openAuthSettingsRepository.refreshOpenAuthSettings()
    }
}
