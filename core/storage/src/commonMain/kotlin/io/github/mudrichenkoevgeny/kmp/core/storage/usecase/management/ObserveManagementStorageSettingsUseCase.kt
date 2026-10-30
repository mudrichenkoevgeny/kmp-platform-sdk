package io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management

import io.github.mudrichenkoevgeny.kmp.core.storage.repository.management.ManagementStorageSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.ManagementStorageSettings
import kotlinx.coroutines.flow.Flow

/**
 * Returns a reactive stream of the latest management storage settings snapshot.
 *
 * @param managementStorageSettingsRepository Management storage settings repository.
 */
class ObserveManagementStorageSettingsUseCase(
    private val managementStorageSettingsRepository: ManagementStorageSettingsRepository
) {
    /**
     * @return Cold [Flow] emitting the latest [ManagementStorageSettings] or `null`.
     */
    operator fun invoke(): Flow<ManagementStorageSettings?> {
        return managementStorageSettingsRepository.observeManagementStorageSettings()
    }
}
