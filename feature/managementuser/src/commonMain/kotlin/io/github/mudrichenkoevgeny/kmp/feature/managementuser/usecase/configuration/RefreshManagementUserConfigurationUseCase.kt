package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.configuration

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.core.security.repository.OpenSecuritySettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.OpenGlobalSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.configuration.OpenUserConfigurationApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.configuration.OpenUserConfiguration
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.configuration.toOpenUserConfiguration

/**
 * Fetches the combined open user configuration bundle and, when the network call succeeds, writes each
 * slice into matching open repositories (global and security settings).
 *
 * @param openUserConfigurationApi Remote source for the bundled configuration DTO.
 * @param openGlobalSettingsRepository Persists open global settings from the bundle.
 * @param openSecuritySettingsRepository Persists open security settings from the bundle.
 */
class RefreshManagementUserConfigurationUseCase(
    private val openUserConfigurationApi: OpenUserConfigurationApi,
    private val openGlobalSettingsRepository: OpenGlobalSettingsRepository,
    private val openSecuritySettingsRepository: OpenSecuritySettingsRepository
) {
    /**
     * @return Mapped [OpenUserConfiguration] on success after repositories are updated; the same error
     * result as the API when the fetch fails—no repository writes occur on failure.
     */
    suspend operator fun invoke(): AppResult<OpenUserConfiguration> {
        val userConfigurationResult = openUserConfigurationApi.getOpenUserConfiguration()
            .mapSuccess { userConfigurationResponse ->
                userConfigurationResponse.toOpenUserConfiguration()
            }

        if (userConfigurationResult is AppResult.Success) {
            val userConfiguration = userConfigurationResult.data
            openGlobalSettingsRepository.updateOpenGlobalSettings(userConfiguration.openGlobalSettings)
            openSecuritySettingsRepository.updateOpenSecuritySettings(userConfiguration.openSecuritySettings)
        }

        return userConfigurationResult
    }
}
