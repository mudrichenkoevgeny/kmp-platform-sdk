package io.github.mudrichenkoevgeny.kmp.core.security.mapper.securitysettings

import io.github.mudrichenkoevgeny.kmp.core.security.model.securitysettings.SecuritySettings
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.response.settings.SecuritySettingsResponse
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.mapper.toPasswordPolicy

fun SecuritySettingsResponse.toSecuritySettings() = SecuritySettings(
    passwordPolicy = this.passwordPolicy.toPasswordPolicy()
)