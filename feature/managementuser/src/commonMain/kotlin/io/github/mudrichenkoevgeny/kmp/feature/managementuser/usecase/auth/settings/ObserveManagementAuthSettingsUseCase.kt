package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings

import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.settings.ManagementAuthSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.ManagementAuthSettings
import kotlinx.coroutines.flow.Flow

/**
 * Observes the in-memory management auth settings snapshot.
 *
 * @param managementAuthSettingsRepository Remote management auth settings API.
 */
class ObserveManagementAuthSettingsUseCase(
    private val managementAuthSettingsRepository: ManagementAuthSettingsRepository
) {
    /**
     * @return [Flow] of the current [ManagementAuthSettings] or `null`.
     */
    operator fun invoke(): Flow<ManagementAuthSettings?> {
        return managementAuthSettingsRepository.observeManagementAuthSettings()
    }
}
