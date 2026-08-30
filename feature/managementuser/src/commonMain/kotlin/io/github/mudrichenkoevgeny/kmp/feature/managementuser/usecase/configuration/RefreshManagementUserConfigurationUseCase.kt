package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.configuration

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.core.security.repository.SecuritySettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.GlobalSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.configuration.ManagementUserConfigurationApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.settings.ManagementAuthSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.configuration.UserConfiguration
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.configuration.toUserConfiguration

/**
 * Fetches the combined user configuration bundle and, when the network call succeeds, writes each
 * slice into the matching core repositories (global, security, and management auth settings).
 *
 * @param userConfigurationApi Remote source for the bundled configuration DTO.
 * @param globalSettingsRepository Persists global settings from the bundle.
 * @param securitySettingsRepository Persists security settings from the bundle.
 * @param managementAuthSettingsRepository Management auth settings repository.
 */
class RefreshManagementUserConfigurationUseCase(
    private val userConfigurationApi: ManagementUserConfigurationApi,
    private val globalSettingsRepository: GlobalSettingsRepository,
    private val securitySettingsRepository: SecuritySettingsRepository,
    private val managementAuthSettingsRepository: ManagementAuthSettingsRepository
) {
    /**
     * @return Mapped [UserConfiguration] on success after repositories are updated; the same error
     * result as the API when the fetch fails—no repository writes occur on failure.
     */
    suspend operator fun invoke(): AppResult<UserConfiguration> {
        val userConfigurationResult = userConfigurationApi.getUserConfiguration()
            .mapSuccess { userConfigurationResponse ->
                userConfigurationResponse.toUserConfiguration()
            }

        if (userConfigurationResult is AppResult.Success) {
            val userConfiguration = userConfigurationResult.data
            globalSettingsRepository.updateGlobalSettings(userConfiguration.globalSettings)
            securitySettingsRepository.updateSecuritySettings(userConfiguration.securitySettings)
            
            // Forces a network reload of full management-specific auth settings,
            // as the configuration bundle only contains a public subset of fields.
            managementAuthSettingsRepository.refreshManagementAuthSettings()
        }

        return userConfigurationResult
    }
}
