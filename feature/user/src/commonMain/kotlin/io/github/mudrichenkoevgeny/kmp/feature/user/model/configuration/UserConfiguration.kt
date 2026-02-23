package io.github.mudrichenkoevgeny.kmp.feature.user.model.configuration

import io.github.mudrichenkoevgeny.kmp.core.security.model.securitysettings.SecuritySettings
import io.github.mudrichenkoevgeny.kmp.core.settings.model.globalsettings.GlobalSettings
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AuthSettings

data class UserConfiguration(
    val globalSettings: GlobalSettings,
    val securitySettings: SecuritySettings,
    val authSettings: AuthSettings
)