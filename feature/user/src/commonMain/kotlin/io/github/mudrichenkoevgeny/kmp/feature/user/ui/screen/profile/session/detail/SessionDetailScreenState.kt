package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.detail

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession

/**
 * Represents the UI state for the session detail screen.
 */
sealed interface SessionDetailScreenState {
    /** Fullscreen loading state while session details are being retrieved. */
    object Loading : SessionDetailScreenState

    /** Fullscreen error state when fetching session details fails. */
    data class Error(val error: AppError) : SessionDetailScreenState

    /**
     * Content state displaying detailed session information.
     *
     * @property session The target session details.
     * @property isCurrentSession Indicates if this session represents the user's active session.
     * @property actionLoading Whether a revoke action is currently executing.
     * @property actionError Error from a failed revoke action.
     */
    data class Content(
        val session: UserSession,
        val isCurrentSession: Boolean = false,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : SessionDetailScreenState
}
