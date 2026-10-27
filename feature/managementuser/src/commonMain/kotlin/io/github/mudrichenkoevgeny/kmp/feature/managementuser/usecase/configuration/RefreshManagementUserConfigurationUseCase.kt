package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.configuration

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.core.security.repository.OpenSecuritySettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.OpenGlobalSettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.settings.OpenStorageSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.configuration.ManagementUserConfigurationApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings.OpenAuthSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.configuration.OpenUserConfiguration
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.configuration.toOpenUserConfiguration

/**
 * Fetches the combined open user configuration bundle for unauthenticated or authenticated management users
 * and, when the network call succeeds, writes each slice into matching open repositories.
 *
 * @param managementUserConfigurationApi Remote source for the bundled open configuration DTO.
 * @param openGlobalSettingsRepository Persists open global settings from the bundle.
 * @param openSecuritySettingsRepository Persists open security settings from the bundle.
 * @param openStorageSettingsRepository Persists open storage settings from the bundle.
 * @param openAuthSettingsRepository Persists open auth settings from the bundle.
 */
class RefreshManagementUserConfigurationUseCase(
    private val managementUserConfigurationApi: ManagementUserConfigurationApi,
    private val openGlobalSettingsRepository: OpenGlobalSettingsRepository,
    private val openSecuritySettingsRepository: OpenSecuritySettingsRepository,
    private val openStorageSettingsRepository: OpenStorageSettingsRepository,
    private val openAuthSettingsRepository: OpenAuthSettingsRepository
) {
    /**
     * @return Mapped [OpenUserConfiguration] on success after open repositories are updated; the same error
     * result as the API when the fetch fails—no repository writes occur on failure.
     */
    suspend operator fun invoke(): AppResult<OpenUserConfiguration> {
        val userConfigurationResult = managementUserConfigurationApi.getOpenUserConfiguration()
            .mapSuccess { userConfigurationResponse ->
                userConfigurationResponse.toOpenUserConfiguration()
            }

        if (userConfigurationResult is AppResult.Success) {
            val userConfiguration = userConfigurationResult.data
            openGlobalSettingsRepository.updateOpenGlobalSettings(userConfiguration.openGlobalSettings)
            openSecuritySettingsRepository.updateOpenSecuritySettings(userConfiguration.openSecuritySettings)
            openStorageSettingsRepository.updateOpenStorageSettings(userConfiguration.openStorageSettings)
            openAuthSettingsRepository.updateOpenAuthSettings(userConfiguration.openAuthSettings)
        }

        return userConfigurationResult
    }
}
