package io.github.mudrichenkoevgeny.kmp.core.storage.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.settings.OpenStorageSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.OpenStorageSettings

/**
 * Use case that delegates to [OpenStorageSettingsRepository.getOpenStorageSettings].
 *
 * @param openStorageSettingsRepository Source of truth for cached or network-backed settings.
 */
class GetOpenStorageSettingsUseCase(
    private val openStorageSettingsRepository: OpenStorageSettingsRepository
) {
    /**
     * @return [AppResult.Success] with [OpenStorageSettings], or [AppResult.Error] when none can be loaded.
     */
    suspend operator fun invoke(): AppResult<OpenStorageSettings> {
        return openStorageSettingsRepository.getOpenStorageSettings()
    }
}
