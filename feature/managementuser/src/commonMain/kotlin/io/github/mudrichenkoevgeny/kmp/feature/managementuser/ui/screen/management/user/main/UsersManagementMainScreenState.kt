package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.main

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails

sealed interface UsersManagementMainScreenState {
    object Loading : UsersManagementMainScreenState
    data class Error(val error: AppError) : UsersManagementMainScreenState
    data class Content(
        val paging: PaginationState<UserDetails>,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : UsersManagementMainScreenState
}
