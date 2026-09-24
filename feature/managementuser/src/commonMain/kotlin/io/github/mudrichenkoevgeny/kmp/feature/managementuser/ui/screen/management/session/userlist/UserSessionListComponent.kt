package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.userlist

import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.list.SessionListOwner
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession

/**
 * Manages a specific user's active sessions lifecycle: listing, revoking specific sessions, and revoking all user sessions.
 */
interface UserSessionListComponent : SessionListOwner {
    /** Reactive UI state. */
    val state: Value<UserSessionListScreenState>

    /** Refreshes the session list. */
    fun onRefresh()
    
    /** Requests the next page of sessions if available. */
    fun onLoadNextPage()
    
    /** Navigates back. */
    fun onBackClick()
    
    /** Navigates to session detail screen for the specified session. */
    fun onSessionClick(session: UserSession)
    
    /** Revokes a specific session. */
    fun onDeleteSessionClick(sessionId: String)
    
    /** Revokes all sessions for this user. */
    fun onDeleteAllSessionsClick()
}
