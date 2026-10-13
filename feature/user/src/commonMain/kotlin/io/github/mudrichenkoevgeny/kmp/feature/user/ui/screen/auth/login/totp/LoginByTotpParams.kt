package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.totp

import kotlinx.serialization.Serializable

/**
 * Navigation arguments for TOTP / MFA login screen.
 */
@Serializable
data class LoginByTotpParams(
    val mfaToken: String
)
