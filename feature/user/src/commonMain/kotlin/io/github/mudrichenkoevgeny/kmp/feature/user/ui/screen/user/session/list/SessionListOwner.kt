package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list

import com.arkivanov.decompose.router.stack.ChildStack
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserSessionId

/**
 * Interface for components or child wrappers that manage an active list of user sessions.
 */
public interface SessionListOwner {
    /**
     * Removes the revoked session from the active list state.
     *
     * @param sessionId The [UserSessionId] of the revoked session.
     */
    public fun onSessionRevoked(sessionId: UserSessionId)
}

/**
 * Finds active or previous [SessionListOwner] instances in the [ChildStack] and notifies them of session revocation.
 *
 * @param sessionId The [UserSessionId] of the revoked session.
 */
public fun ChildStack<*, *>.notifySessionRevoked(sessionId: UserSessionId) {
    items
        .map { it.instance }
        .filterIsInstance<SessionListOwner>()
        .forEach { it.onSessionRevoked(sessionId) }
}
