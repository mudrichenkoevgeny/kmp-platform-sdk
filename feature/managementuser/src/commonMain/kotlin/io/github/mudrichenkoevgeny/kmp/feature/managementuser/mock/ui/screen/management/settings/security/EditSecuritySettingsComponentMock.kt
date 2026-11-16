package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.settings.security

import com.arkivanov.decompose.Child
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.dialog.SettingsDialogChild
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.dialog.SettingsDialogConfig
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.security.EditSecuritySettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.security.EditSecuritySettingsScreenState

/**
 * Mock implementation of [EditSecuritySettingsComponent] for testing.
 */
@InternalApi
public class EditSecuritySettingsComponentMock(
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
    public override val state: Value<EditSecuritySettingsScreenState> = MutableValue(initialState)

    public override val dialogSlotNavigation: SlotNavigation<SettingsDialogConfig> =
        SlotNavigation<SettingsDialogConfig>()
    private val _dialogSlot = MutableValue<ChildSlot<SettingsDialogConfig, SettingsDialogChild>>(ChildSlot())
    public override val dialogSlot: Value<ChildSlot<SettingsDialogConfig, SettingsDialogChild>> = _dialogSlot

    /** Sets the active dialog configuration. */
    public fun setDialog(config: SettingsDialogConfig?) {
        val child = when (config) {
            is SettingsDialogConfig.ConfirmReset -> SettingsDialogChild.ConfirmReset
            null -> null
        }
        _dialogSlot.value = ChildSlot(
            child = if (config != null && child != null) {
                Child.Created(
                    configuration = config,
                    instance = child
                )
            } else {
                null
            }
        )
    }

    public override fun onRetry(): Unit = Unit
    public override fun onRecentAuthenticationValidityForOpenUserChanged(value: String): Unit = Unit
    public override fun onRecentAuthenticationValidityForManagementUserChanged(value: String): Unit = Unit
    public override fun onMfaTokenExpirationSecondsChanged(value: String): Unit = Unit
    public override fun onPasswordMinLengthChanged(value: String): Unit = Unit
    public override fun onPasswordRequireLetterToggled(enabled: Boolean): Unit = Unit
    public override fun onPasswordRequireUpperCaseToggled(enabled: Boolean): Unit = Unit
    public override fun onPasswordRequireLowerCaseToggled(enabled: Boolean): Unit = Unit
    public override fun onPasswordRequireDigitToggled(enabled: Boolean): Unit = Unit
    public override fun onPasswordRequireSpecialCharToggled(enabled: Boolean): Unit = Unit
    public override fun onCommonPasswordsChanged(value: String): Unit = Unit
    public override fun onAccountLockoutMaxFailedPasswordAttemptsChanged(value: String): Unit = Unit
    public override fun onAccountLockoutMaxFailedOtpAttemptsChanged(value: String): Unit = Unit
    public override fun onAccountLockoutMaxFailedTotpAttemptsChanged(value: String): Unit = Unit
    public override fun onAccountLockoutFailedAttemptsWindowSecondsChanged(value: String): Unit = Unit
    public override fun onAccountLockoutDurationSecondsChanged(value: String): Unit = Unit
    public override fun onAccountLockoutIndefiniteLockoutThresholdChanged(value: String): Unit = Unit
    public override fun onAccountLockoutIsSelfServiceUnlockEnabledToggled(enabled: Boolean): Unit = Unit
    public override fun onAccountLockoutCheckIntervalSecondsChanged(value: String): Unit = Unit
    public override fun onRefreshTokenRotationGracePeriodSecondsChanged(value: String): Unit = Unit
    public override fun onOpenIpBlacklistEnabledToggled(enabled: Boolean): Unit = Unit
    public override fun onOpenIpBlacklistChanged(value: String): Unit = Unit
    public override fun onOpenIpWhitelistEnabledToggled(enabled: Boolean): Unit = Unit
    public override fun onOpenIpWhitelistChanged(value: String): Unit = Unit
    public override fun onManagementIpBlacklistEnabledToggled(enabled: Boolean): Unit = Unit
    public override fun onManagementIpBlacklistChanged(value: String): Unit = Unit
    public override fun onManagementIpWhitelistEnabledToggled(enabled: Boolean): Unit = Unit
    public override fun onManagementIpWhitelistChanged(value: String): Unit = Unit
    public override fun onOtpRetryAfterSecondsChanged(value: String): Unit = Unit
    public override fun onOtpNumberOfSymbolsChanged(value: String): Unit = Unit
    public override fun onOtpExpirationSecondsChanged(value: String): Unit = Unit
    public override fun onMaxRequestsPerPeriodChanged(value: String): Unit = Unit
    public override fun onRateLimitPeriodSecondsChanged(value: String): Unit = Unit
    public override fun onSaveClick(): Unit = Unit
    public override fun onResetClick(): Unit = Unit
    public override fun onConfirmReset(): Unit = Unit
    public override fun onDismissDialog(): Unit = Unit
    public override fun onBackClick(): Unit = Unit
}
