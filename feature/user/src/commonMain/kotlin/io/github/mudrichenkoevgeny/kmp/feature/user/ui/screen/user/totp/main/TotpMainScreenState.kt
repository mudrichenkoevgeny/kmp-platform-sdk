package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.totp.main

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.feature.user.utils.FieldValidator

/**
 * Unified UI state for the TOTP main screen.
 *
 * @param isInitialized true when user security state is loaded.
 * @param isEnabled true if TOTP is active for this account.
 * @param code current verification code text for confirmation.
 * @param actionLoading true when API actions are in progress.
 * @param actionError error from the last API action.
 * @param initError critical error during state initialization.
 */
data class TotpMainScreenState(
    val isInitialized: Boolean = false,
    val isEnabled: Boolean = false,
    val code: String = "",
    val actionLoading: Boolean = false,
    val actionError: AppError? = null,
    val initError: AppError? = null
) {
    val canConfirm: Boolean = code.length == FieldValidator.TOTP_CODE_LENGTH && code.all { it.isDigit() } && !actionLoading
}
