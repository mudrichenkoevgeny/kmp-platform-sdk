package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.security

import com.arkivanov.decompose.value.Value

/**
 * Decompose controller for editing security settings.
 */
interface EditSecuritySettingsComponent {
    /** Observable UI state. */
    val state: Value<EditSecuritySettingsScreenState>

    /** Retries loading settings. */
    fun onRetry()

    /** Updates recent auth validity seconds for open user field. */
    fun onRecentAuthenticationValidityForOpenUserChanged(value: String)

    /** Updates recent auth validity seconds for management user field. */
    fun onRecentAuthenticationValidityForManagementUserChanged(value: String)

    /** Updates MFA token expiration seconds field. */
    fun onMfaTokenExpirationSecondsChanged(value: String)

    /** Updates password policy min length. */
    fun onPasswordMinLengthChanged(value: String)

    /** Toggles password policy require letter. */
    fun onPasswordRequireLetterToggled(enabled: Boolean)

    /** Toggles password policy require uppercase. */
    fun onPasswordRequireUpperCaseToggled(enabled: Boolean)

    /** Toggles password policy require lowercase. */
    fun onPasswordRequireLowerCaseToggled(enabled: Boolean)

    /** Toggles password policy require digit. */
    fun onPasswordRequireDigitToggled(enabled: Boolean)

    /** Toggles password policy require special char. */
    fun onPasswordRequireSpecialCharToggled(enabled: Boolean)

    /** Updates common passwords list. */
    fun onCommonPasswordsChanged(value: String)

    /** Updates account lockout max failed password attempts. */
    fun onAccountLockoutMaxFailedPasswordAttemptsChanged(value: String)

    /** Updates account lockout max failed OTP attempts. */
    fun onAccountLockoutMaxFailedOtpAttemptsChanged(value: String)

    /** Updates account lockout max failed TOTP attempts. */
    fun onAccountLockoutMaxFailedTotpAttemptsChanged(value: String)

    /** Updates account lockout failed attempts window seconds. */
    fun onAccountLockoutFailedAttemptsWindowSecondsChanged(value: String)

    /** Updates account lockout duration seconds. */
    fun onAccountLockoutDurationSecondsChanged(value: String)

    /** Updates account lockout indefinite lockout threshold. */
    fun onAccountLockoutIndefiniteLockoutThresholdChanged(value: String)

    /** Toggles account lockout self service unlock enabled. */
    fun onAccountLockoutIsSelfServiceUnlockEnabledToggled(enabled: Boolean)

    /** Updates account lockout check interval seconds. */
    fun onAccountLockoutCheckIntervalSecondsChanged(value: String)

    /** Updates refresh token rotation grace period seconds. */
    fun onRefreshTokenRotationGracePeriodSecondsChanged(value: String)

    /** Toggles open IP blacklist. */
    fun onOpenIpBlacklistEnabledToggled(enabled: Boolean)

    /** Updates open IP blacklist value. */
    fun onOpenIpBlacklistChanged(value: String)

    /** Toggles open IP whitelist. */
    fun onOpenIpWhitelistEnabledToggled(enabled: Boolean)

    /** Updates open IP whitelist value. */
    fun onOpenIpWhitelistChanged(value: String)

    /** Toggles management IP blacklist. */
    fun onManagementIpBlacklistEnabledToggled(enabled: Boolean)

    /** Updates management IP blacklist value. */
    fun onManagementIpBlacklistChanged(value: String)

    /** Toggles management IP whitelist. */
    fun onManagementIpWhitelistEnabledToggled(enabled: Boolean)

    /** Updates management IP whitelist value. */
    fun onManagementIpWhitelistChanged(value: String)

    /** Updates OTP retry after seconds field. */
    fun onOtpRetryAfterSecondsChanged(value: String)

    /** Updates OTP number of symbols field. */
    fun onOtpNumberOfSymbolsChanged(value: String)

    /** Updates OTP expiration seconds field. */
    fun onOtpExpirationSecondsChanged(value: String)

    /** Updates max requests per period field. */
    fun onMaxRequestsPerPeriodChanged(value: String)

    /** Updates rate limit period seconds field. */
    fun onRateLimitPeriodSecondsChanged(value: String)

    /** Saves security settings to the backend. */
    fun onSaveClick()

    /** Navigates back. */
    fun onBackClick()
}
