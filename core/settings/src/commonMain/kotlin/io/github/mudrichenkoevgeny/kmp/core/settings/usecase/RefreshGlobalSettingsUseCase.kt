package io.github.mudrichenkoevgeny.kmp.core.settings.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.settings.model.globalsettings.GlobalSettings
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.globalsettings.GlobalSettingsRepository

/**
 * Use case that delegates to [GlobalSettingsRepository.refreshGlobalSettings].
 *
 * @param globalSettingsRepository Repository performing the network refresh and persistence.
 */
class RefreshGlobalSettingsUseCase(
    private val globalSettingsRepository: GlobalSettingsRepository
) {
    /**
     * @return [AppResult.Success] with refreshed [GlobalSettings], or [AppResult.Error] on failure.
     */
    suspend operator fun invoke(): AppResult<GlobalSettings> {
        return globalSettingsRepository.refreshGlobalSettings()
    }
}