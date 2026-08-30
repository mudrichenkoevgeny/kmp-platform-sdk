package io.github.mudrichenkoevgeny.kmp.feature.clientuser.usecase.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings.OpenAuthSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.PublicAuthSettings

/**
 * Forces a network reload of auth-related settings and updates the repository’s observable state.
 *
 * @param openAuthSettingsRepository Auth settings aggregate.
 */
class RefreshAuthSettingsUseCase(
    private val openAuthSettingsRepository: OpenAuthSettingsRepository
) {
    /**
     * @return Fresh [PublicAuthSettings] on success, or an error result when the refresh request fails.
     */
    suspend operator fun invoke(): AppResult<PublicAuthSettings> {
        return openAuthSettingsRepository.refreshAuthSettings()
    }
}