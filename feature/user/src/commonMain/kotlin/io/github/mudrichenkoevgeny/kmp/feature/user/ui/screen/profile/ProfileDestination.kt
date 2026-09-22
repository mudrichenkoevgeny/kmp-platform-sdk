package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile

import kotlinx.serialization.Serializable

/**
 * Serializable configurations for the profile management stack router.
 */
@Serializable
sealed interface ProfileDestination {
    /** Main profile screen with user info and navigation buttons. */
    @Serializable
    object Main : ProfileDestination

    /** TOTP main screen. */
    @Serializable
    object TotpMain : ProfileDestination

    /** TOTP recovery codes management. */
    @Serializable
    object TotpRecoveryCodes : ProfileDestination

    /** List of active sessions. */
    @Serializable
    object Sessions : ProfileDestination

    /** List of user identifiers (emails, phones). */
    @Serializable
    object Identifiers : ProfileDestination
}
