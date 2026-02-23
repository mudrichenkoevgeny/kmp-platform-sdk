package io.github.mudrichenkoevgeny.kmp.core.security.model.securitysettings

import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.model.PasswordPolicy
import kotlinx.serialization.Serializable

@Serializable
data class SecuritySettings(
    val passwordPolicy: PasswordPolicy
)