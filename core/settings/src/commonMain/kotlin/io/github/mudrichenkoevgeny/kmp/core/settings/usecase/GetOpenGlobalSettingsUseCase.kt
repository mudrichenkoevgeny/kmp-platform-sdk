package io.github.mudrichenkoevgeny.kmp.core.settings.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.OpenGlobalSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.OpenGlobalSettings

/**
 * Use case that delegates to [OpenGlobalSettingsRepository.getOpenGlobalSettings].
 *
 * @param openGlobalSettingsRepository Source of truth for cached or network-backed settings.
 */
class GetOpenGlobalSettingsUseCase(
    private val openGlobalSettingsRepository: OpenGlobalSettingsRepository
) {
    /**
     * @return [AppResult.Success] with [OpenGlobalSettings], or [AppResult.Error] when none can be loaded.
     */
    suspend operator fun invoke(): AppResult<OpenGlobalSettings> {
        return openGlobalSettingsRepository.getOpenGlobalSettings()
    }
}
