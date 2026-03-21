package io.github.mudrichenkoevgeny.kmp.core.security.model.securitysettings

import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.model.PasswordPolicy
import kotlinx.serialization.Serializable

/**
 * Serializable security configuration snapshot exposed to the app, currently wrapping the server-defined
 * [PasswordPolicy] used for local password validation.
 *
 * @param passwordPolicy Rules and constraints applied when validating passwords in the security module.
 */
@Serializable
data class SecuritySettings(
    val passwordPolicy: PasswordPolicy
)