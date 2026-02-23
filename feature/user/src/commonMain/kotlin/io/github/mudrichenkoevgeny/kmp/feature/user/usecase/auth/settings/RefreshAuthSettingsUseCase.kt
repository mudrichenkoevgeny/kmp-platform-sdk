package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AuthSettings
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings.AuthSettingsRepository

class RefreshAuthSettingsUseCase(
    private val authSettingsRepository: AuthSettingsRepository
) {
    suspend operator fun invoke(): AppResult<AuthSettings> {
        return authSettingsRepository.refreshAuthSettings()
    }
}