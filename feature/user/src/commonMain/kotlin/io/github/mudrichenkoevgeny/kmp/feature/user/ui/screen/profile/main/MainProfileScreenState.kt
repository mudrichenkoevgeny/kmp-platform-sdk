package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.main

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails

/**
 * UI state for the main profile screen.
 */
sealed interface MainProfileScreenState {

    /** Loading user data or performing an action. */
    data object Loading : MainProfileScreenState

    /** User is not signed in. */
    data object Unauthorized : MainProfileScreenState

    /** User profile content with management options. */
    data class Content(
        val user: UserDetails,
        val isAccountDeletionAvailable: Boolean = true,
        val showDeleteConfirmation: Boolean = false,
        val showLogoutConfirmation: Boolean = false,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : MainProfileScreenState

    /** Unexpected error during state loading. */
    data class Error(val error: AppError) : MainProfileScreenState
}
