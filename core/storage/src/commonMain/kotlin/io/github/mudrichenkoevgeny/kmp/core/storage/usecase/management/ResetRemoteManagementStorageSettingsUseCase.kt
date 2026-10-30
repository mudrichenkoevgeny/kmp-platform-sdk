package io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.management.ManagementStorageSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.ManagementStorageSettings

/**
 * Resets management storage settings to default values remotely and updates local state.
 *
 * @param managementStorageSettingsRepository Management storage settings repository.
 */
class ResetRemoteManagementStorageSettingsUseCase(
    private val managementStorageSettingsRepository: ManagementStorageSettingsRepository
) {
    /**
     * @return Default [ManagementStorageSettings] on success or [AppResult.Error].
     */
    suspend operator fun invoke(): AppResult<ManagementStorageSettings> {
        return managementStorageSettingsRepository.resetRemoteManagementStorageSettings()
    }
}
