package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails

sealed interface UserDetailScreenState {
    object Loading : UserDetailScreenState
    data class Error(val error: AppError) : UserDetailScreenState
    data class Content(
        val user: UserDetails,
        val authorityLevelInput: String,
        val accountStatusInput: String,
        val isSaving: Boolean = false,
        val saveError: AppError? = null,
        val isDeleting: Boolean = false,
        val deleteError: AppError? = null
    ) : UserDetailScreenState
}
