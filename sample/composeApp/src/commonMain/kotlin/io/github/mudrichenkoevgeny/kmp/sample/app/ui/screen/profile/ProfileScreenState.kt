package io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.profile

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.feature.user.model.user.CurrentUser

/**
 * Profile tab UI states: loading user, signed out, signed in, or failure from the user stream.
 */
sealed interface ProfileScreenState {
    /** Waiting for the first user emission. */
    data object Loading : ProfileScreenState

    /** No authenticated user is available. */
    data object Unauthorized : ProfileScreenState

    /**
     * Authenticated user is available for display.
     *
     * @param user Active session snapshot.
     */
    data class Content(val user: CurrentUser) : ProfileScreenState

    /**
     * User stream failed; surface a generic error placeholder.
     *
     * @param appError Failure propagated from the repository flow.
     */
    data class Error(val appError: AppError) : ProfileScreenState
}