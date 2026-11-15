package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.security

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError

/**
 * UI state for editing security settings.
 */
public sealed interface EditSecuritySettingsScreenState {
    /** Initial loading state. */
    public object Loading : EditSecuritySettingsScreenState

    /** Loading error state. */
    public data class Error(public val error: AppError) : EditSecuritySettingsScreenState

    /** Form content state. */
    public data class Content(
        public val recentAuthenticationValiditySecondsForOpenUser: String,
        public val recentAuthenticationValiditySecondsForManagementUser: String,
        public val mfaTokenExpirationSeconds: String,
        public val passwordMinLength: String,
        public val passwordRequireLetter: Boolean,
        public val passwordRequireUpperCase: Boolean,
        public val passwordRequireLowerCase: Boolean,
        public val passwordRequireDigit: Boolean,
        public val passwordRequireSpecialChar: Boolean,
        public val commonPasswords: String,
        public val accountLockoutMaxFailedPasswordAttempts: String,
        public val accountLockoutMaxFailedOtpAttempts: String,
        public val accountLockoutMaxFailedTotpAttempts: String,
        public val accountLockoutFailedAttemptsWindowSeconds: String,
        public val accountLockoutDurationSeconds: String,
        public val accountLockoutIndefiniteLockoutThreshold: String,
        public val accountLockoutIsSelfServiceUnlockEnabled: Boolean,
        public val accountLockoutCheckIntervalSeconds: String,
        public val refreshTokenRotationGracePeriodSeconds: String,
        public val openIpBlacklistEnabled: Boolean,
        public val openIpBlacklist: String,
        public val openIpWhitelistEnabled: Boolean,
        public val openIpWhitelist: String,
        public val managementIpBlacklistEnabled: Boolean,
        public val managementIpBlacklist: String,
        public val managementIpWhitelistEnabled: Boolean,
        public val managementIpWhitelist: String,
        public val otpRetryAfterSeconds: String,
        public val otpNumberOfSymbols: String,
        public val otpExpirationSeconds: String,
        public val maxRequestsPerPeriod: String = "100",
        public val rateLimitPeriodSeconds: String = "60",
        public val isSaving: Boolean = false,
        public val saveError: AppError? = null
    ) : EditSecuritySettingsScreenState
}
