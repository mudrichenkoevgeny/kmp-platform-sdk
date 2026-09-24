package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.settings.security

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.security.EditSecuritySettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.security.EditSecuritySettingsScreenState

@InternalApi
class EditSecuritySettingsComponentMock(
    initialState: EditSecuritySettingsScreenState = EditSecuritySettingsScreenState.Content(
        recentAuthenticationValiditySecondsForOpenUser = "300",
        recentAuthenticationValiditySecondsForManagementUser = "300",
        mfaTokenExpirationSeconds = "300",
        passwordMinLength = "8",
        passwordRequireLetter = true,
        passwordRequireUpperCase = false,
        passwordRequireLowerCase = false,
        passwordRequireDigit = true,
        passwordRequireSpecialChar = false,
        commonPasswords = "password,123456",
        accountLockoutMaxFailedPasswordAttempts = "5",
        accountLockoutMaxFailedOtpAttempts = "5",
        accountLockoutMaxFailedTotpAttempts = "5",
        accountLockoutFailedAttemptsWindowSeconds = "300",
        accountLockoutDurationSeconds = "300",
        accountLockoutIndefiniteLockoutThreshold = "3",
        accountLockoutIsSelfServiceUnlockEnabled = true,
        accountLockoutCheckIntervalSeconds = "60",
        refreshTokenRotationGracePeriodSeconds = "30",
        openIpBlacklistEnabled = false,
        openIpBlacklist = "",
        openIpWhitelistEnabled = false,
        openIpWhitelist = "",
        managementIpBlacklistEnabled = false,
        managementIpBlacklist = "",
        managementIpWhitelistEnabled = false,
        managementIpWhitelist = "",
        otpRetryAfterSeconds = "60",
        otpNumberOfSymbols = "6",
        otpExpirationSeconds = "300",
        maxRequestsPerPeriod = "100",
        rateLimitPeriodSeconds = "60"
    )
) : EditSecuritySettingsComponent {
    override val state: Value<EditSecuritySettingsScreenState> = MutableValue(initialState)

    override fun onRetry() {}
    override fun onRecentAuthenticationValidityForOpenUserChanged(value: String) {}
    override fun onRecentAuthenticationValidityForManagementUserChanged(value: String) {}
    override fun onMfaTokenExpirationSecondsChanged(value: String) {}
    override fun onPasswordMinLengthChanged(value: String) {}
    override fun onPasswordRequireLetterToggled(enabled: Boolean) {}
    override fun onPasswordRequireUpperCaseToggled(enabled: Boolean) {}
    override fun onPasswordRequireLowerCaseToggled(enabled: Boolean) {}
    override fun onPasswordRequireDigitToggled(enabled: Boolean) {}
    override fun onPasswordRequireSpecialCharToggled(enabled: Boolean) {}
    override fun onCommonPasswordsChanged(value: String) {}
    override fun onAccountLockoutMaxFailedPasswordAttemptsChanged(value: String) {}
    override fun onAccountLockoutMaxFailedOtpAttemptsChanged(value: String) {}
    override fun onAccountLockoutMaxFailedTotpAttemptsChanged(value: String) {}
    override fun onAccountLockoutFailedAttemptsWindowSecondsChanged(value: String) {}
    override fun onAccountLockoutDurationSecondsChanged(value: String) {}
    override fun onAccountLockoutIndefiniteLockoutThresholdChanged(value: String) {}
    override fun onAccountLockoutIsSelfServiceUnlockEnabledToggled(enabled: Boolean) {}
    override fun onAccountLockoutCheckIntervalSecondsChanged(value: String) {}
    override fun onRefreshTokenRotationGracePeriodSecondsChanged(value: String) {}
    override fun onOpenIpBlacklistEnabledToggled(enabled: Boolean) {}
    override fun onOpenIpBlacklistChanged(value: String) {}
    override fun onOpenIpWhitelistEnabledToggled(enabled: Boolean) {}
    override fun onOpenIpWhitelistChanged(value: String) {}
    override fun onManagementIpBlacklistEnabledToggled(enabled: Boolean) {}
    override fun onManagementIpBlacklistChanged(value: String) {}
    override fun onManagementIpWhitelistEnabledToggled(enabled: Boolean) {}
    override fun onManagementIpWhitelistChanged(value: String) {}
    override fun onOtpRetryAfterSecondsChanged(value: String) {}
    override fun onOtpNumberOfSymbolsChanged(value: String) {}
    override fun onOtpExpirationSecondsChanged(value: String) {}
    override fun onMaxRequestsPerPeriodChanged(value: String) {}
    override fun onRateLimitPeriodSecondsChanged(value: String) {}
    override fun onSaveClick() {}
    override fun onResetClick() {}
    override fun onBackClick() {}
}