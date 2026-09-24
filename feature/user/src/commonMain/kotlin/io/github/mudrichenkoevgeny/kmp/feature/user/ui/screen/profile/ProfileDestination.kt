package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile

import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.toUserIdentifierIdOrThrow
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.toUserSessionIdOrThrow
import kotlinx.serialization.Serializable

/**
 * Serializable configurations for the profile management stack router.
 */
@Serializable
sealed interface ProfileDestination {
    /** Main profile screen with user info and navigation buttons. */
    @Serializable
    data object Main : ProfileDestination

    /** TOTP main screen. */
    @Serializable
    data object TotpMain : ProfileDestination

    /** TOTP recovery codes management. */
    @Serializable
    data object TotpRecoveryCodes : ProfileDestination

    /** List of active sessions. */
    @Serializable
    data object Sessions : ProfileDestination

    /** Active session detail screen. */
    @Serializable
    data class SessionDetail(
        val sessionIdValue: String
    ) : ProfileDestination {
        val sessionId: UserSessionId get() = sessionIdValue.toUserSessionIdOrThrow()
    }

    /** List of user identifiers (emails, phones). */
    @Serializable
    data object Identifiers : ProfileDestination

    /** Identifier detail screen. */
    @Serializable
    data class IdentifierDetail(
        val identifierIdValue: String
    ) : ProfileDestination {
        val identifierId: UserIdentifierId get() = identifierIdValue.toUserIdentifierIdOrThrow()
    }
}
