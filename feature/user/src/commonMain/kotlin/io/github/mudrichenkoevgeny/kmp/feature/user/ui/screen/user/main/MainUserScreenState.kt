package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.main

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserPrivate

/** Screen state representation for [MainUserComponent]. */
public sealed interface MainUserScreenState {
    /** Loading state when user data is being fetched. */
    public data object Loading : MainUserScreenState

    /** Shown when user is not authorized. */
    public data class Unauthorized(val actionError: AppError? = null) : MainUserScreenState

    /** Content state displaying active user data and actions. */
    public data class Content(
        val user: UserPrivate,
        val isDeleteConfirmationVisible: Boolean = false,
        val isLogoutConfirmationVisible: Boolean = false,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : MainUserScreenState

    /** Error state when loading user data fails. */
    public data class Error(val error: AppError) : MainUserScreenState
}
