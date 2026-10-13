package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.main

import kotlinx.serialization.Serializable

/**
 * Screen destination parameters for the main TOTP screen.
 *
 * @param token Optional MFA setup token if deep-linking directly into setup.
 */
@Serializable
data class TotpMainParams(
    val token: String? = null
)
