package io.github.mudrichenkoevgeny.kmp.core.settings.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.OpenGlobalSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.OpenGlobalSettings

/**
 * Use case that delegates to [OpenGlobalSettingsRepository.refreshOpenGlobalSettings].
 *
 * @param openGlobalSettingsRepository Repository performing the network refresh and persistence.
 */
class RefreshOpenGlobalSettingsUseCase(
    private val openGlobalSettingsRepository: OpenGlobalSettingsRepository
) {
    /**
     * @return [AppResult.Success] with refreshed [OpenGlobalSettings], or [AppResult.Error] on failure.
     */
    suspend operator fun invoke(): AppResult<OpenGlobalSettings> {
        return openGlobalSettingsRepository.refreshOpenGlobalSettings()
    }
}
