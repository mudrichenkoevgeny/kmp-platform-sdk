package io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.profile

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.feature.user.model.user.CurrentUser

sealed interface ProfileScreenState {
    data object Loading : ProfileScreenState

    data object Unauthorized : ProfileScreenState

    data class Content(val user: CurrentUser) : ProfileScreenState

    data class Error(val appError: AppError) : ProfileScreenState
}