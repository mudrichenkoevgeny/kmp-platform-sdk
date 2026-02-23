package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings.AuthSettingsRepository

class GetAvailableUserAuthProvidersUseCase(
    private val authSettingsRepository: AuthSettingsRepository
) {
    suspend operator fun invoke(): AppResult<AvailableAuthProviders> {
        return authSettingsRepository.getAuthSettings().mapSuccess { authSettings ->
            authSettings.availableAuthProviders
        }
    }
}