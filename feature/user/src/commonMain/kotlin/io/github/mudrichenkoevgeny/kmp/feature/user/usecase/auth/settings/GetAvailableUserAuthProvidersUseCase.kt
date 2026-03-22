package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings.AuthSettingsRepository

/**
 * Loads auth settings and exposes which sign-in providers the backend allows for this user/session.
 *
 * @param authSettingsRepository Cached and remote auth settings access.
 */
class GetAvailableUserAuthProvidersUseCase(
    private val authSettingsRepository: AuthSettingsRepository
) {
    /**
     * @return [AvailableAuthProviders] on successful settings load, or an error result when settings
     * cannot be obtained.
     */
    suspend operator fun invoke(): AppResult<AvailableAuthProviders> {
        return authSettingsRepository.getAuthSettings().mapSuccess { authSettings ->
            authSettings.availableAuthProviders
        }
    }
}