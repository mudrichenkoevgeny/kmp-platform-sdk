package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.totp

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.feature.user.utils.FieldValidator
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.auth.login.totp.LoginByTotpMode

/**
 * UI state for TOTP login: supports either time-based code entry or a static recovery code.
 */
sealed interface LoginByTotpScreenState {

    /** Reserved for a dedicated loading phase. */
    data object Loading : LoginByTotpScreenState

    /**
     * MFA code entry form.
     *
     * @param mfaToken intermediate token required for the login call.
     * @param code current verification code text.
     * @param mode toggle between [LoginByTotpMode.TOTP] and [LoginByTotpMode.RECOVERY_CODE].
     * @param actionLoading while true, shows overlay during sign-in.
     * @param actionError server error from the last login attempt.
     */
    data class Content(
        val mfaToken: String,
        val code: String = "",
        val mode: LoginByTotpMode = LoginByTotpMode.TOTP,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : LoginByTotpScreenState {

        /** Standard digit-length rule for TOTP; basic presence check for recovery codes. */
        val isCodeValid: Boolean = when (mode) {
            LoginByTotpMode.TOTP -> code.length == FieldValidator.TOTP_CODE_LENGTH && code.all { it.isDigit() }
            LoginByTotpMode.RECOVERY_CODE -> code.isNotBlank()
        }

        val canSubmit: Boolean = isCodeValid && !actionLoading
    }
}
