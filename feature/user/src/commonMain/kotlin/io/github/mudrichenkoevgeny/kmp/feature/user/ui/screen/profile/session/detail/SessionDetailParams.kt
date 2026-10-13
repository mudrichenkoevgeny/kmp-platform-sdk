package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.detail

import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.toUserSessionIdOrThrow
import kotlinx.serialization.Serializable

/**
 * Navigation arguments for session detail screen.
 */
@Serializable
data class SessionDetailParams(
    val sessionIdValue: String
) {
    val sessionId: UserSessionId get() = sessionIdValue.toUserSessionIdOrThrow()
}
