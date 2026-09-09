package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.identifiers

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier

sealed interface UserIdentifiersScreenState {
    object Loading : UserIdentifiersScreenState
    data class Error(val error: AppError) : UserIdentifiersScreenState
    data class Content(
        val paging: PaginationState<UserIdentifier>,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : UserIdentifiersScreenState
}
