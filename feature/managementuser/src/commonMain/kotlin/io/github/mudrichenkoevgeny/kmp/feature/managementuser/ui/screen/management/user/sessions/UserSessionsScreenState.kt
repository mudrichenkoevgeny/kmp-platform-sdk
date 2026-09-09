package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.sessions

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession

sealed interface UserSessionsScreenState {
    object Loading : UserSessionsScreenState
    data class Error(val error: AppError) : UserSessionsScreenState
    data class Content(
        val paging: PaginationState<UserSession>,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : UserSessionsScreenState
}
