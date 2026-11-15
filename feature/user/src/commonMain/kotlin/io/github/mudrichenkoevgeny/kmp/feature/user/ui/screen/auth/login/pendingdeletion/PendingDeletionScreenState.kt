package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.pendingdeletion

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError

/**
 * UI state for the pending account deletion screen shown during login.
 */
public data class PendingDeletionScreenState(
    val actionLoading: Boolean = false,
    val actionError: AppError? = null
)
