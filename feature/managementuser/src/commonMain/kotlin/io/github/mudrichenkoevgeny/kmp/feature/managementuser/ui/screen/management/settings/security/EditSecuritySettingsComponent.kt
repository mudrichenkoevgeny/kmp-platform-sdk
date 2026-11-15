package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.security

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.SettingsDialogChild
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.SettingsDialogConfig

/**
 * Decompose controller for editing security settings.
 */
public interface EditSecuritySettingsComponent {
    /** Observable UI state. */
    public val state: Value<EditSecuritySettingsScreenState>

    /** Decompose child slot for settings confirmation dialogs. */
    public val dialogSlot: Value<ChildSlot<SettingsDialogConfig, SettingsDialogChild>>

    /** Slot navigator for settings dialogs. */
    public val dialogSlotNavigation: SlotNavigation<SettingsDialogConfig>

    /** Retries loading settings. */
    public fun onRetry()

    /** Updates recent auth validity seconds for open user field. */
    public fun onRecentAuthenticationValidityForOpenUserChanged(value: String)

    /** Updates recent auth validity seconds for management user field. */
    public fun onRecentAuthenticationValidityForManagementUserChanged(value: String)

    /** Updates MFA token expiration seconds field. */
    public fun onMfaTokenExpirationSecondsChanged(value: String)

    /** Updates password policy min length. */
    public fun onPasswordMinLengthChanged(value: String)

    /** Toggles password policy require letter. */
    public fun onPasswordRequireLetterToggled(enabled: Boolean)

    /** Toggles password policy require uppercase. */
    public fun onPasswordRequireUpperCaseToggled(enabled: Boolean)

    /** Toggles password policy require lowercase. */
    public fun onPasswordRequireLowerCaseToggled(enabled: Boolean)

    /** Toggles password policy require digit. */
    public fun onPasswordRequireDigitToggled(enabled: Boolean)

    /** Toggles password policy require special char. */
    public fun onPasswordRequireSpecialCharToggled(enabled: Boolean)

    /** Updates common passwords list. */
    public fun onCommonPasswordsChanged(value: String)

    /** Updates account lockout max failed password attempts. */
    public fun onAccountLockoutMaxFailedPasswordAttemptsChanged(value: String)

    /** Updates account lockout max failed OTP attempts. */
    public fun onAccountLockoutMaxFailedOtpAttemptsChanged(value: String)

    /** Updates account lockout max failed TOTP attempts. */
    public fun onAccountLockoutMaxFailedTotpAttemptsChanged(value: String)

    /** Updates account lockout failed attempts window seconds. */
    public fun onAccountLockoutFailedAttemptsWindowSecondsChanged(value: String)

    /** Updates account lockout duration seconds. */
    public fun onAccountLockoutDurationSecondsChanged(value: String)

    /** Updates account lockout indefinite lockout threshold. */
    public fun onAccountLockoutIndefiniteLockoutThresholdChanged(value: String)

    /** Toggles account lockout self service unlock enabled. */
    public fun onAccountLockoutIsSelfServiceUnlockEnabledToggled(enabled: Boolean)

    /** Updates account lockout check interval seconds. */
    public fun onAccountLockoutCheckIntervalSecondsChanged(value: String)

    /** Updates refresh token rotation grace period seconds. */
    public fun onRefreshTokenRotationGracePeriodSecondsChanged(value: String)

    /** Toggles open IP blacklist. */
    public fun onOpenIpBlacklistEnabledToggled(enabled: Boolean)

    /** Updates open IP blacklist value. */
    public fun onOpenIpBlacklistChanged(value: String)

    /** Toggles open IP whitelist. */
    public fun onOpenIpWhitelistEnabledToggled(enabled: Boolean)

    /** Updates open IP whitelist value. */
    public fun onOpenIpWhitelistChanged(value: String)

    /** Toggles management IP blacklist. */
    public fun onManagementIpBlacklistEnabledToggled(enabled: Boolean)

    /** Updates management IP blacklist value. */
    public fun onManagementIpBlacklistChanged(value: String)

    /** Toggles management IP whitelist. */
    public fun onManagementIpWhitelistEnabledToggled(enabled: Boolean)

    /** Updates management IP whitelist value. */
    public fun onManagementIpWhitelistChanged(value: String)

    /** Updates OTP retry after seconds field. */
    public fun onOtpRetryAfterSecondsChanged(value: String)

    /** Updates OTP number of symbols field. */
    public fun onOtpNumberOfSymbolsChanged(value: String)

    /** Updates OTP expiration seconds field. */
    public fun onOtpExpirationSecondsChanged(value: String)

    /** Updates max requests per period field. */
    public fun onMaxRequestsPerPeriodChanged(value: String)

    /** Updates rate limit period seconds field. */
    public fun onRateLimitPeriodSecondsChanged(value: String)

    /** Saves security settings to the backend. */
    public fun onSaveClick()

    /** Shows reset confirmation dialog. */
    public fun onResetClick()

    /** Confirms resetting security settings to defaults. */
    public fun onConfirmReset()

    /** Dismisses active confirmation dialogs. */
    public fun onDismissDialog()

    /** Navigates back. */
    public fun onBackClick()
}
