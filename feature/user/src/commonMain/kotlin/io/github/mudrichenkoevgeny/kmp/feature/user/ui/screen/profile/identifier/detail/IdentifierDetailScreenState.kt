package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.detail

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier

/**
 * Represents the UI state for the identifier detail screen.
 */
sealed interface IdentifierDetailScreenState {
    /** Fullscreen loading state while identifier details are being retrieved. */
    data object Loading : IdentifierDetailScreenState

    /** Fullscreen error state when fetching identifier details fails. */
    data class Error(val error: AppError) : IdentifierDetailScreenState

    /**
     * Content state displaying detailed identifier information.
     *
     * @property identifier The target user identifier.
     * @property isCurrentIdentifier Indicates if this identifier represents the user's current session identifier.
     * @property canChangePassword Whether change password action is supported for this identifier.
     * @property canDeletePassword Whether delete password action is supported for this identifier.
     * @property actionLoading Whether a delete or change password action is currently executing.
     * @property actionError Error from a failed action.
     * @property isChangePasswordDialogVisible Whether the change password dialog is currently visible.
     */
    data class Content(
        val identifier: UserIdentifier,
        val isCurrentIdentifier: Boolean = false,
        val canChangePassword: Boolean = false,
        val canDeletePassword: Boolean = false,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null,
        val isChangePasswordDialogVisible: Boolean = false,
        val isDeleteConfirmationVisible: Boolean = false
    ) : IdentifierDetailScreenState
}
