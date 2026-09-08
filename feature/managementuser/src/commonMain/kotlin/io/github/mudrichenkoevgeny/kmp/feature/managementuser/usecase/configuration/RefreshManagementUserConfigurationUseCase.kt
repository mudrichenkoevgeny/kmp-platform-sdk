package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.configuration

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.configuration.ManagementUserConfigurationApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.settings.ManagementAuthSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.globalsettings.ManagementGlobalSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.security.settings.ManagementSecuritySettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.configuration.ManagementUserConfiguration
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.configuration.toManagementUserConfiguration

/**
 * Fetches the combined management user configuration bundle and, when the network call succeeds, writes each
 * slice into the matching management repositories (global, security, and auth settings).
 *
 * @param userConfigurationApi Remote source for the bundled configuration DTO.
 * @param managementGlobalSettingsRepository Persists management global settings from the bundle.
 * @param managementSecuritySettingsRepository Persists management security settings from the bundle.
 * @param managementAuthSettingsRepository Management auth settings repository.
 */
class RefreshManagementUserConfigurationUseCase(
    private val userConfigurationApi: ManagementUserConfigurationApi,
    private val managementGlobalSettingsRepository: ManagementGlobalSettingsRepository,
    private val managementSecuritySettingsRepository: ManagementSecuritySettingsRepository,
    private val managementAuthSettingsRepository: ManagementAuthSettingsRepository
) {
    /**
     * @return Mapped [ManagementUserConfiguration] on success after repositories are updated; the same error
     * result as the API when the fetch fails—no repository writes occur on failure.
     */
    suspend operator fun invoke(): AppResult<ManagementUserConfiguration> {
        val userConfigurationResult = userConfigurationApi.getManagementUserConfiguration()
            .mapSuccess { userConfigurationResponse ->
                userConfigurationResponse.toManagementUserConfiguration()
            }

        if (userConfigurationResult is AppResult.Success) {
            val userConfiguration = userConfigurationResult.data
            managementGlobalSettingsRepository.updateManagementGlobalSettings(userConfiguration.managementGlobalSettings)
            managementSecuritySettingsRepository.updateManagementSecuritySettings(userConfiguration.managementSecuritySettings)
            managementAuthSettingsRepository.updateManagementAuthSettings(userConfiguration.managementAuthSettings)
        }

        return userConfigurationResult
    }
}
