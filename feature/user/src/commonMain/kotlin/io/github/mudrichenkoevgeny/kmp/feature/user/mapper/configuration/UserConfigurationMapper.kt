package io.github.mudrichenkoevgeny.kmp.feature.user.mapper.configuration

import io.github.mudrichenkoevgeny.kmp.core.security.mapper.securitysettings.toSecuritySettings
import io.github.mudrichenkoevgeny.kmp.core.settings.mapper.globalsettings.toGlobalSettings
import io.github.mudrichenkoevgeny.kmp.feature.user.mapper.auth.settings.toAuthSettings
import io.github.mudrichenkoevgeny.kmp.feature.user.model.configuration.UserConfiguration
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.configuration.UserConfigurationResponse

/**
 * Maps aggregated user-configuration payload into [UserConfiguration] by delegating to core mappers
 * ([toGlobalSettings], [toSecuritySettings]) and user [toAuthSettings].
 */
fun UserConfigurationResponse.toUserConfiguration() = UserConfiguration(
    globalSettings = this.globalSettings.toGlobalSettings(),
    securitySettings = this.securitySettings.toSecuritySettings(),
    authSettings = this.authSettings.toAuthSettings()
)
