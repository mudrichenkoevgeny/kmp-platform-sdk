package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AuthSettings
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings.AuthSettingsRepository

/**
 * Forces a network reload of auth-related settings and updates the repository’s observable state.
 *
 * @param authSettingsRepository Auth settings aggregate.
 */
class RefreshAuthSettingsUseCase(
    private val authSettingsRepository: AuthSettingsRepository
) {
    /**
     * @return Fresh [AuthSettings] on success, or an error result when the refresh request fails.
     */
    suspend operator fun invoke(): AppResult<AuthSettings> {
        return authSettingsRepository.refreshAuthSettings()
    }
}