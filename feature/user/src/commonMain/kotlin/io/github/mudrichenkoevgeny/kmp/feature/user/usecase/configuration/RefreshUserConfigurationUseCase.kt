package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.configuration

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.core.security.repository.securitysettings.SecuritySettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.globalsettings.GlobalSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.mapper.configuration.toUserConfiguration
import io.github.mudrichenkoevgeny.kmp.feature.user.model.configuration.UserConfiguration
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.configuration.UserConfigurationApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings.AuthSettingsRepository

/**
 * Fetches the combined user configuration bundle and, when the network call succeeds, writes each
 * slice into the matching core repositories (global, security, and auth settings).
 *
 * @param userConfigurationApi Remote source for the bundled configuration DTO.
 * @param globalSettingsRepository Persists global settings from the bundle.
 * @param securitySettingsRepository Persists security settings from the bundle.
 * @param authSettingsRepository Persists auth settings from the bundle.
 */
class RefreshUserConfigurationUseCase(
    private val userConfigurationApi: UserConfigurationApi,
    private val globalSettingsRepository: GlobalSettingsRepository,
    private val securitySettingsRepository: SecuritySettingsRepository,
    private val authSettingsRepository: AuthSettingsRepository
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
            authSettingsRepository.updateAuthSettings(userConfiguration.authSettings)
        }

        return userConfigurationResult
    }
}