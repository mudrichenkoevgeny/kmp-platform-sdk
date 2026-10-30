package io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.management.ManagementStorageSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.ManagementStorageSettings

/**
 * Forces a network reload of management storage settings and updates local state.
 *
 * @param managementStorageSettingsRepository Management storage settings repository.
 */
class RefreshManagementStorageSettingsUseCase(
    private val managementStorageSettingsRepository: ManagementStorageSettingsRepository
) {
    /**
     * @return Refreshed [ManagementStorageSettings] on success or [AppResult.Error].
     */
    suspend operator fun invoke(): AppResult<ManagementStorageSettings> {
        return managementStorageSettingsRepository.refreshManagementStorageSettings()
    }
}
