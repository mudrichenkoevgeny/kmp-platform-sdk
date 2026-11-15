package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings

import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.globalsettings.ManagementGlobalSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.ManagementGlobalSettings
import kotlinx.coroutines.flow.Flow

/**
 * Observes live global settings updates.
 *
 * @param managementGlobalSettingsRepository Management global settings repository.
 */
public class ObserveManagementGlobalSettingsUseCase(
    private val managementGlobalSettingsRepository: ManagementGlobalSettingsRepository
) {
    /** Observes management global settings updates. */
    public operator fun invoke(): Flow<ManagementGlobalSettings?> =
        managementGlobalSettingsRepository.observeManagementGlobalSettings()
}
