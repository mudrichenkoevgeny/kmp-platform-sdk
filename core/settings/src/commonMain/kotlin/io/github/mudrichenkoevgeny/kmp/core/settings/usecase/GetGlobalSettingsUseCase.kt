package io.github.mudrichenkoevgeny.kmp.core.settings.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.GlobalSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.GlobalSettings

/**
 * Use case that delegates to [GlobalSettingsRepository.getGlobalSettings].
 *
 * @param globalSettingsRepository Source of truth for cached or network-backed settings.
 */
class GetGlobalSettingsUseCase(
    private val globalSettingsRepository: GlobalSettingsRepository
) {
    /**
     * @return [AppResult.Success] with [GlobalSettings], or [AppResult.Error] when none can be loaded.
     */
    suspend operator fun invoke(): AppResult<GlobalSettings> {
        return globalSettingsRepository.getGlobalSettings()
    }
}