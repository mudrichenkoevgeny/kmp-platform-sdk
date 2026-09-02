package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.totp

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.feature.user.utils.FieldValidator

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
     * @param mode toggle between [Mode.TOTP] and [Mode.RECOVERY_CODE].
     * @param actionLoading while true, shows overlay during sign-in.
     * @param actionError server error from the last login attempt.
     */
    data class Content(
        val mfaToken: String,
        val code: String = "",
        val mode: Mode = Mode.TOTP,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : LoginByTotpScreenState {

        /** Standard digit-length rule for TOTP; basic presence check for recovery codes. */
        val isCodeValid: Boolean = when (mode) {
            Mode.TOTP -> code.length == FieldValidator.TOTP_CODE_LENGTH && code.all { it.isDigit() }
            Mode.RECOVERY_CODE -> code.isNotBlank()
        }

        val canSubmit: Boolean = isCodeValid && !actionLoading
    }

    /** Defines which verification method is currently active on the UI. */
    enum class Mode {
        /** Time-based One-Time Password. */
        TOTP,
        /** Static backup recovery code. */
        RECOVERY_CODE
    }
}
