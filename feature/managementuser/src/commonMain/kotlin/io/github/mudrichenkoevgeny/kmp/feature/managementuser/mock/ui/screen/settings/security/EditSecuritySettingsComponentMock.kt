package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.settings.security

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.security.EditSecuritySettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.security.EditSecuritySettingsScreenState

@InternalApi
class EditSecuritySettingsComponentMock(
    initialState: EditSecuritySettingsScreenState = EditSecuritySettingsScreenState.Content(
        recentAuthenticationValiditySeconds = "300",
        recentAuthenticationValiditySecondsForManagement = "300",
        mfaTokenExpirationSeconds = "300",
        passwordMinLength = "8",
        passwordRequireLetter = true,
        passwordRequireUpperCase = false,
        passwordRequireLowerCase = false,
        passwordRequireDigit = true,
        passwordRequireSpecialChar = false,
        otpRetryAfterSeconds = "60",
        otpNumberOfSymbols = "6",
        otpExpirationSeconds = "300",
        maxRequestsPerPeriod = "100",
        rateLimitPeriodSeconds = "60"
    )
) : EditSecuritySettingsComponent {
    override val state: Value<EditSecuritySettingsScreenState> = MutableValue(initialState)

    override fun onRetry() {}
    override fun onRecentAuthenticationValiditySecondsChanged(value: String) {}
    override fun onRecentAuthenticationValidityForManagementChanged(value: String) {}
    override fun onMfaTokenExpirationSecondsChanged(value: String) {}
    override fun onPasswordMinLengthChanged(value: String) {}
    override fun onPasswordRequireLetterToggled(enabled: Boolean) {}
    override fun onPasswordRequireUpperCaseToggled(enabled: Boolean) {}
    override fun onPasswordRequireLowerCaseToggled(enabled: Boolean) {}
    override fun onPasswordRequireDigitToggled(enabled: Boolean) {}
    override fun onPasswordRequireSpecialCharToggled(enabled: Boolean) {}
    override fun onOtpRetryAfterSecondsChanged(value: String) {}
    override fun onOtpNumberOfSymbolsChanged(value: String) {}
    override fun onOtpExpirationSecondsChanged(value: String) {}
    override fun onMaxRequestsPerPeriodChanged(value: String) {}
    override fun onRateLimitPeriodSecondsChanged(value: String) {}
    override fun onSaveClick() {}
    override fun onBackClick() {}
}
