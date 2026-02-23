package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.configuration

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.core.security.repository.securitysettings.SecuritySettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.globalsettings.GlobalSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.mapper.configuration.toUserConfiguration
import io.github.mudrichenkoevgeny.kmp.feature.user.model.configuration.UserConfiguration
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.configuration.UserConfigurationApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings.AuthSettingsRepository

class RefreshUserConfigurationUseCase(
    private val userConfigurationApi: UserConfigurationApi,
    private val globalSettingsRepository: GlobalSettingsRepository,
    private val securitySettingsRepository: SecuritySettingsRepository,
    private val authSettingsRepository: AuthSettingsRepository
) {
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