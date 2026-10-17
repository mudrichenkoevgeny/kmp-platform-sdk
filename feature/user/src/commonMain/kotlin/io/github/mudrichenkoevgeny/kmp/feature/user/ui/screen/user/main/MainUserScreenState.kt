package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.main

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserPrivate

/** Screen state representation for [MainUserComponent]. */
sealed interface MainUserScreenState {
    /** Loading state when user data is being fetched. */
    object Loading : MainUserScreenState

    /** Shown when user is not authorized. */
    data class Unauthorized(val actionError: AppError? = null) : MainUserScreenState

    /** Content state displaying active user data and actions. */
    data class Content(
        val user: UserPrivate,
        val isDeleteConfirmationVisible: Boolean = false,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : MainUserScreenState

    /** Error state when loading user data fails. */
    data class Error(val error: AppError) : MainUserScreenState
}
