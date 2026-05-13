package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings.AuthSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.PublicAuthSettings

/**
 * Forces a network reload of auth-related settings and updates the repository’s observable state.
 *
 * @param authSettingsRepository Auth settings aggregate.
 */
class RefreshAuthSettingsUseCase(
    private val authSettingsRepository: AuthSettingsRepository
) {
    /**
     * @return Fresh [PublicAuthSettings] on success, or an error result when the refresh request fails.
     */
    suspend operator fun invoke(): AppResult<PublicAuthSettings> {
        return authSettingsRepository.refreshAuthSettings()
    }
}