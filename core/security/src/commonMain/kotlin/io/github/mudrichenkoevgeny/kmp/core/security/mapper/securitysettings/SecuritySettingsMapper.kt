package io.github.mudrichenkoevgeny.kmp.core.security.mapper.securitysettings

import io.github.mudrichenkoevgeny.kmp.core.security.model.securitysettings.SecuritySettings
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.response.settings.SecuritySettingsResponse
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.model.PasswordPolicy
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.mapper.toPasswordPolicy

/**
 * Maps a wire [SecuritySettingsResponse] from the foundation module into the SDK [SecuritySettings] model.
 *
 * @return Domain [SecuritySettings] including the mapped [PasswordPolicy].
 */
fun SecuritySettingsResponse.toSecuritySettings() = SecuritySettings(
    passwordPolicy = this.passwordPolicy.toPasswordPolicy()
)