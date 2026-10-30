package io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.management.ManagementStorageSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.ManagementStorageSettings

/**
 * Returns cached management storage settings or fetches them from the server.
 *
 * @param managementStorageSettingsRepository Management storage settings repository.
 */
class GetManagementStorageSettingsUseCase(
    private val managementStorageSettingsRepository: ManagementStorageSettingsRepository
) {
    /**
     * @return [ManagementStorageSettings] on success or [AppResult.Error].
     */
    suspend operator fun invoke(): AppResult<ManagementStorageSettings> {
        return managementStorageSettingsRepository.getManagementStorageSettings()
    }
}
