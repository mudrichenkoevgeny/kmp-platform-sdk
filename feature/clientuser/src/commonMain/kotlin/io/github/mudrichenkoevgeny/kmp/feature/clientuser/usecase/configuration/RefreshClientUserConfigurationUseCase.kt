package io.github.mudrichenkoevgeny.kmp.feature.clientuser.usecase.configuration

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.core.security.repository.OpenSecuritySettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.OpenGlobalSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.configuration.OpenUserConfigurationApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings.OpenAuthSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.configuration.OpenUserConfiguration
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.configuration.toOpenUserConfiguration

/**
 * Fetches the combined user configuration bundle and, when the network call succeeds, writes each
 * slice into the matching core repositories (global, security, and auth settings).
 *
 * @param openUserConfigurationApi Remote source for the bundled configuration DTO.
 * @param openGlobalSettingsRepository Persists global settings from the bundle.
 * @param openSecuritySettingsRepository Persists security settings from the bundle.
 * @param openAuthSettingsRepository Persists auth settings from the bundle.
 */
class RefreshClientUserConfigurationUseCase(
    private val openUserConfigurationApi: OpenUserConfigurationApi,
    private val openGlobalSettingsRepository: OpenGlobalSettingsRepository,
    private val openSecuritySettingsRepository: OpenSecuritySettingsRepository,
    private val openAuthSettingsRepository: OpenAuthSettingsRepository
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
            openAuthSettingsRepository.updateOpenAuthSettings(userConfiguration.openAuthSettings)
        }

        return userConfigurationResult
    }
}
