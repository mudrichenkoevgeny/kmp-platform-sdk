package io.github.mudrichenkoevgeny.kmp.core.storage.usecase.open

import io.github.mudrichenkoevgeny.kmp.core.storage.repository.open.OpenStorageSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.OpenStorageSettings
import kotlinx.coroutines.flow.Flow

/**
 * Returns a reactive stream of the latest open storage settings snapshot.
 *
 * @param openStorageSettingsRepository Open storage settings repository.
 */
public class ObserveOpenStorageSettingsUseCase(
    private val openStorageSettingsRepository: OpenStorageSettingsRepository
) {
    /**
     * @return Cold [Flow] emitting the latest [OpenStorageSettings] or `null`.
     */
    public operator fun invoke(): Flow<OpenStorageSettings?> =
        openStorageSettingsRepository.observeOpenStorageSettings()
}
