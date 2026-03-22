package io.github.mudrichenkoevgeny.kmp.feature.user.model.configuration

import io.github.mudrichenkoevgeny.kmp.core.security.model.securitysettings.SecuritySettings
import io.github.mudrichenkoevgeny.kmp.core.settings.model.globalsettings.GlobalSettings
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AuthSettings
import kotlinx.serialization.Serializable

/** Aggregated bootstrap payload: global app settings, security policy, and user auth settings from one response. */
@Serializable
data class UserConfiguration(
    val globalSettings: GlobalSettings,
    val securitySettings: SecuritySettings,
    val authSettings: AuthSettings
)