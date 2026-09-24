package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.userlist

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier

/**
 * UI state for a specific user's identifiers management screen.
 */
sealed interface UserIdentifierListScreenState {

    /** Initial loading of identifiers. */
    data object Loading : UserIdentifierListScreenState

    /** Critical error during state initialization. */
    data class Error(val error: AppError) : UserIdentifierListScreenState

    /**
     * Identifiers list with pagination.
     *
     * @param paging cumulative state of the paginated list.
     * @param actionLoading true when an action is in progress.
     * @param actionError error from the last action attempt.
     */
    data class Content(
        val paging: PaginationState<UserIdentifier>,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : UserIdentifierListScreenState
}
