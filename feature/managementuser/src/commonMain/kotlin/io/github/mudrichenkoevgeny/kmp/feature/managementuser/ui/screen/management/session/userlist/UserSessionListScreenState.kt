package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.userlist

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession

sealed interface UserSessionListScreenState {
    object Loading : UserSessionListScreenState
    data class Error(val error: AppError) : UserSessionListScreenState
    data class Content(
        val paging: PaginationState<UserSession>,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : UserSessionListScreenState
}
