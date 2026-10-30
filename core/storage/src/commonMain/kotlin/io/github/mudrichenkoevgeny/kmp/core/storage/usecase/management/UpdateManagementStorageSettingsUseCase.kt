package io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.management.ManagementStorageSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.ManagementStorageSettings

/**
 * Pushes updated storage settings to the remote server via [ManagementStorageSettingsRepository].
 *
 * @param managementStorageSettingsRepository Management storage settings repository.
 */
class UpdateManagementStorageSettingsUseCase(
    private val managementStorageSettingsRepository: ManagementStorageSettingsRepository
) {
    /**
     * Pushes updated storage settings and updates local storage on success.
     *
     * @param storageSettings New settings to apply.
     * @return Updated [ManagementStorageSettings] on success, or an [AppResult.Error].
     */
    suspend operator fun invoke(storageSettings: ManagementStorageSettings): AppResult<ManagementStorageSettings> {
        return managementStorageSettingsRepository.saveRemoteManagementStorageSettings(storageSettings)
    }
}
