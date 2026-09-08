package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.globalsettings.ManagementGlobalSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.ManagementGlobalSettings

/**
 * Returns cached global settings or fetches them from the server.
 *
 * @param managementGlobalSettingsRepository Management global settings API.
 */
class GetManagementGlobalSettingsUseCase(
    private val managementGlobalSettingsRepository: ManagementGlobalSettingsRepository
) {
    suspend operator fun invoke(): AppResult<ManagementGlobalSettings> {
        return managementGlobalSettingsRepository.getManagementGlobalSettings()
    }
}
