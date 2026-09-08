package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.security

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError

/**
 * UI state for editing security settings.
 */
sealed interface EditSecuritySettingsScreenState {
    /** Initial loading state. */
    object Loading : EditSecuritySettingsScreenState

    /** Loading error state. */
    data class Error(val error: AppError) : EditSecuritySettingsScreenState

    /** Form content state. */
    data class Content(
        val recentAuthenticationValiditySeconds: String,
        val recentAuthenticationValiditySecondsForManagement: String,
        val mfaTokenExpirationSeconds: String,
        val passwordMinLength: String,
        val passwordRequireLetter: Boolean,
        val passwordRequireUpperCase: Boolean,
        val passwordRequireLowerCase: Boolean,
        val passwordRequireDigit: Boolean,
        val passwordRequireSpecialChar: Boolean,
        val otpRetryAfterSeconds: String,
        val otpNumberOfSymbols: String,
        val otpExpirationSeconds: String,
        val maxRequestsPerPeriod: String = "100",
        val rateLimitPeriodSeconds: String = "60",
        val isSaving: Boolean = false,
        val saveError: AppError? = null
    ) : EditSecuritySettingsScreenState
}
