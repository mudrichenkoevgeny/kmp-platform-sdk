package io.github.mudrichenkoevgeny.kmp.core.security.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.repository.OpenSecuritySettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.OpenSecuritySettings

/**
 * Use case that delegates to [OpenSecuritySettingsRepository.getOpenSecuritySettings].
 *
 * @param openSecuritySettingsRepository Source of truth for cached or network-backed settings.
 */
class GetOpenSecuritySettingsUseCase(
    private val openSecuritySettingsRepository: OpenSecuritySettingsRepository
) {
    /**
     * @return [AppResult.Success] with [OpenSecuritySettings], or [AppResult.Error] when none can be loaded.
     */
    suspend operator fun invoke(): AppResult<OpenSecuritySettings> {
        return openSecuritySettingsRepository.getOpenSecuritySettings()
    }
}
