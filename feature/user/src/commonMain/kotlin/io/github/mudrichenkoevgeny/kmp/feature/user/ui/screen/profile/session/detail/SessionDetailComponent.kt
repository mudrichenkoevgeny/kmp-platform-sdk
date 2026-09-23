package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.detail

import com.arkivanov.decompose.value.Value

/**
 * Manages the single user session detail screen lifecycle: loading session info and revoking session.
 */
interface SessionDetailComponent {
    /** Reactive UI state. */
    val state: Value<SessionDetailScreenState>

    /** Revokes the current displayed session if permitted. */
    fun onRevokeSessionClick()

    /** Retries loading the session details on error. */
    fun onRetry()

    /** Navigates back. */
    fun onBackClick()
}
