package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.globalsettings.ManagementGlobalSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.ManagementGlobalSettings

/**
 * Returns cached global settings or fetches them from the server.
 *
 * @param managementGlobalSettingsRepository Management global settings API.
 */
public class GetManagementGlobalSettingsUseCase(
    private val managementGlobalSettingsRepository: ManagementGlobalSettingsRepository
) {
    /** Executes the use case to retrieve management global settings. */
    public suspend operator fun invoke(): AppResult<ManagementGlobalSettings> =
        managementGlobalSettingsRepository.getManagementGlobalSettings()
}
