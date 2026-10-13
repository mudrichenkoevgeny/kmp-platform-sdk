package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile

import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.detail.IdentifierDetailParams
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.list.IdentifierListParams
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.detail.SessionDetailParams
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.main.TotpMainParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
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
    data class TotpMain(
        val params: TotpMainParams = TotpMainParams()
    ) : ProfileDestination

    /** TOTP recovery codes management. */
    @Serializable
    data object TotpRecoveryCodes : ProfileDestination

    /** List of active sessions. */
    @Serializable
    data object Sessions : ProfileDestination

    /** Active session detail screen. */
    @Serializable
    data class SessionDetail(
        val params: SessionDetailParams
    ) : ProfileDestination {
        val sessionId: UserSessionId get() = params.sessionId
    }

    /** List of user identifiers (emails, phones). */
    @Serializable
    data class Identifiers(
        val params: IdentifierListParams = IdentifierListParams()
    ) : ProfileDestination

    /** Identifier detail screen. */
    @Serializable
    data class IdentifierDetail(
        val params: IdentifierDetailParams
    ) : ProfileDestination {
        val identifierId: UserIdentifierId get() = params.identifierId
    }
}
