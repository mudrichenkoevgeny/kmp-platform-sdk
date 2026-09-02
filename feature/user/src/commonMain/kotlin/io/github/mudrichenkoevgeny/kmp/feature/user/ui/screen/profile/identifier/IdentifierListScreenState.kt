package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier

/**
 * UI state for the user identifiers management.
 */
sealed interface IdentifierListScreenState {

    /** Initial loading of identifiers. */
    data object Loading : IdentifierListScreenState

    /**
     * Identifiers list and add flows with pagination.
     *
     * @param paging cumulative state of the paginated list.
     * @param actionLoading true when an action (delete, send code, add, change password) is in progress.
     * @param actionError error from the last action attempt.
     * @param addEmailState current state of the add email flow.
     * @param addPhoneState current state of the add phone flow.
     * @param changePasswordEmail target email for password change dialog, or null if hidden.
     */
    data class Content(
        val paging: PaginationState<UserIdentifier>,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null,
        val addEmailState: AddIdentifierState = AddIdentifierState.Idle,
        val addPhoneState: AddIdentifierState = AddIdentifierState.Idle,
        val changePasswordEmail: String? = null
    ) : IdentifierListScreenState

    /** Critical error during state initialization. */
    data class Error(val error: AppError) : IdentifierListScreenState

    /**
     * Sub-state for adding a new identifier.
     */
    sealed interface AddIdentifierState {
        data object Idle : AddIdentifierState
        data class EnteringCode(val value: String, val code: String = "") : AddIdentifierState
    }
}
