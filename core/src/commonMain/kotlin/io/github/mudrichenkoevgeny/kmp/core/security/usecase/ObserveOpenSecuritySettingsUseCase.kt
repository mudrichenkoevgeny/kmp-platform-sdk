package io.github.mudrichenkoevgeny.kmp.core.security.usecase

import io.github.mudrichenkoevgeny.kmp.core.security.repository.OpenSecuritySettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.OpenSecuritySettings
import kotlinx.coroutines.flow.Flow

/**
 * Use case that delegates to [OpenSecuritySettingsRepository.observeOpenSecuritySettings].
 *
 * @param openSecuritySettingsRepository Source of truth for cached or network-backed settings.
 */
public class ObserveOpenSecuritySettingsUseCase(
    private val openSecuritySettingsRepository: OpenSecuritySettingsRepository
) {
    /**
     * @return Cold [Flow] emitting the latest [OpenSecuritySettings] or `null`.
     */
    public operator fun invoke(): Flow<OpenSecuritySettings?> =
        openSecuritySettingsRepository.observeOpenSecuritySettings()
}
