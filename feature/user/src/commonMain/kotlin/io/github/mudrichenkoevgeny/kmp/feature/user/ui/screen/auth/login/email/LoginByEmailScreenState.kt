package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError

/**
 * UI state for email login: optional transient loading shell or the main form with validation and submit feedback.
 */
public sealed interface LoginByEmailScreenState {

    /** Reserved for a dedicated loading phase; the implementation currently starts in [Content]. */
    public data object Loading : LoginByEmailScreenState

    /**
     * Email and password fields with visibility toggle, inline validation, and login action state.
     *
     * @param email current email text.
     * @param isEmailValid whether the email passes client-side format checks.
     * @param password current password text.
     * @param isPasswordValid whether the password passes minimum length (via security validation).
     * @param isPasswordVisible whether the password field shows plain text.
     * @param actionLoading while true, shows overlay during sign-in.
     * @param actionError server or validation error from the last login attempt.
     */
    public data class Content(
        val email: String = "",
        val isEmailValid: Boolean = false,
        val password: String = "",
        val isPasswordValid: Boolean = false,
        val isPasswordVisible: Boolean = false,
        val isRegistrationAvailable: Boolean = true,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : LoginByEmailScreenState {

        public val canLogin: Boolean = isEmailValid && isPasswordValid && !actionLoading
    }
}
