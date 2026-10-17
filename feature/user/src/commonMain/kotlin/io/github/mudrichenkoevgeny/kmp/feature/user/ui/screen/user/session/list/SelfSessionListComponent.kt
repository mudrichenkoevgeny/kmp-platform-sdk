package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list

import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary

/**
 * Manages the self active sessions lifecycle: listing, revoking specific sessions, and revoking all other sessions.
 */
interface SelfSessionListComponent : SessionListOwner {
    /** Reactive UI state. */
    val state: Value<SelfSessionListScreenState>

    /** Refreshes the session list. */
    fun onRefresh()

    /** Navigates to session detail screen for the specified session. */
    fun onSessionClick(session: UserSessionSummary)

    /** Revokes a specific session. */
    fun onRevokeSessionClick(sessionId: UserSessionId)

    /** Revokes all sessions except the current one. */
    fun onRevokeAllOtherSessionsClick()

    /** Requests the next page of sessions if available. */
    fun onLoadNextPage()

    /** Navigates back. */
    fun onBackClick()
}