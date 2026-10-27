package io.github.mudrichenkoevgeny.kmp.core.storage.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.settings.OpenStorageSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.OpenStorageSettings

/**
 * Use case that delegates to [OpenStorageSettingsRepository.refreshOpenStorageSettings].
 *
 * @param openStorageSettingsRepository Repository performing the network refresh and persistence.
 */
class RefreshOpenStorageSettingsUseCase(
    private val openStorageSettingsRepository: OpenStorageSettingsRepository
) {
    /**
     * @return [AppResult.Success] with refreshed [OpenStorageSettings], or [AppResult.Error] on failure.
     */
    suspend operator fun invoke(): AppResult<OpenStorageSettings> {
        return openStorageSettingsRepository.refreshOpenStorageSettings()
    }
}
