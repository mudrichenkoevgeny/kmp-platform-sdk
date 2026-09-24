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
        val lockoutTypeInput: String = "NONE",
        val temporaryLockoutUntilInput: String = "",
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
                        !lockoutTypeInput.equals(user.lockoutType.serialName, ignoreCase = true)) ||
                    temporaryLockoutUntilInput != initialTempLockout
            }
    }
}
