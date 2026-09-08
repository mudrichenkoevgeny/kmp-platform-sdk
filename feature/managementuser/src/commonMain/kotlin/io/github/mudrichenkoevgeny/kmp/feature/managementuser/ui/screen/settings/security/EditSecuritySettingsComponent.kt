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

    /** Updates recent auth validity seconds field. */
    fun onRecentAuthenticationValiditySecondsChanged(value: String)

    /** Updates recent auth validity for management seconds field. */
    fun onRecentAuthenticationValidityForManagementChanged(value: String)

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
