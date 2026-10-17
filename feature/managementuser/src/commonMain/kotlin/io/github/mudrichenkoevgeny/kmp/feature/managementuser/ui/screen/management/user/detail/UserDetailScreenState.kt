package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserPrivate

/**
 * UI state for administrative single user account detail screen.
 */
sealed interface UserDetailScreenState {

    /** Initial loading of user profile data. */
    object Loading : UserDetailScreenState

    /** Critical error during state initialization. */
    data class Error(val error: AppError) : UserDetailScreenState

    /**
     * User details and account management input form state.
     *
     * @param user The target user's private profile entity.
     * @param authorityLevelInput Current authority level text field value.
     * @param accountStatusInput Current account status dropdown selection.
     * @param lockoutTypeInput Current lockout type dropdown selection.
     * @param temporaryLockoutUntilInput Current temporary lockout expiry timestamp text value.
     * @param isSelf True if this detail screen belongs to the logged-in manager's own account.
     * @param isSaving True while update changes request is in progress.
     * @param saveError Server error from the last update attempt.
     * @param isDeleting True while account deletion request is in progress.
     * @param deleteError Server error from the last deletion attempt.
     * @param isDisablingTotp True while TOTP disable request is in progress.
     * @param disableTotpError Server error from the last TOTP disable attempt.
     * @param isDeleteConfirmationVisible True when the deletion confirmation dialog is shown.
     */
    data class Content(
        val user: UserPrivate,
        val authorityLevelInput: String,
        val accountStatusInput: String,
        val lockoutTypeInput: String = "NONE",
        val temporaryLockoutUntilInput: String = "",
        val isSelf: Boolean = false,
        val isSaving: Boolean = false,
        val saveError: AppError? = null,
        val isDeleting: Boolean = false,
        val deleteError: AppError? = null,
        val isDisablingTotp: Boolean = false,
        val disableTotpError: AppError? = null,
        val isDeleteConfirmationVisible: Boolean = false
    ) : UserDetailScreenState {
        val hasChanges: Boolean
            get() {
                val initialAuthLevel = user.authorityLevel.toString()
                val currentAuthLevel = authorityLevelInput.ifBlank { "0" }
                val initialAccountStatus = user.accountStatus.name
                val initialLockoutType = user.lockoutType.name
                val initialTempLockout = user.temporaryLockoutUntil?.toEpochMilliseconds()?.toString() ?: ""

                return currentAuthLevel != initialAuthLevel ||
                    !accountStatusInput.equals(initialAccountStatus, ignoreCase = true) ||
                    (!lockoutTypeInput.equals(initialLockoutType, ignoreCase = true) &&
                        !lockoutTypeInput.equals(user.lockoutType.value, ignoreCase = true)) ||
                    temporaryLockoutUntilInput != initialTempLockout
            }
    }
}