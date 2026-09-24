package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.security

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreBackButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreTextButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.error.FullscreenError
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input.CoreOutlinedTextField
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreBodyText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreErrorText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreScreenTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.Res
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.account_lockout_check_interval_seconds
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.account_lockout_policy_section
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.blacklist_enabled
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.blacklist_placeholder
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.common_passwords_placeholder
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.edit_security_settings_title
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.failed_attempts_window_seconds
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.general_security
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.indefinite_lockout_threshold
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.lockout_duration_seconds
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.management_ip_restriction_policy
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.max_failed_otp_attempts
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.max_failed_password_attempts
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.max_failed_totp_attempts
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.max_requests_per_period
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mfa_token_expiration_seconds
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.settings.security.EditSecuritySettingsComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.open_ip_restriction_policy
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.otp_confirmation
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.otp_expiration_seconds
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.otp_number_of_symbols
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.otp_retry_after_seconds
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.password_min_length
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.password_policy
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.password_require_digit
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.password_require_letter
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.password_require_lowercase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.password_require_special_char
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.password_require_uppercase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.rate_limit_period_seconds
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.rate_limiting
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.recent_authentication_validity_for_management
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.recent_authentication_validity_seconds
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.refresh_token_rotation_grace_period_seconds
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.reset_to_defaults
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.save
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.saving
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.self_service_unlock_enabled
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.whitelist_enabled
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.whitelist_placeholder
import org.jetbrains.compose.resources.stringResource

/**
 * Screen for editing security policies and settings.
 *
 * @param component Decompose controller for security settings editing.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSecuritySettingsScreen(component: EditSecuritySettingsComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    CoreScreenTitleText(
                        text = stringResource(Res.string.edit_security_settings_title),
                        modifier = Modifier.testTag(EditSecuritySettingsTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    CoreBackButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(EditSecuritySettingsTestTags.BACK_BUTTON)
                    )
                }
            )
        }
    ) { padding ->
        val currentState = state
        when (currentState) {
            is EditSecuritySettingsScreenState.Loading -> {
                FullscreenLoading(modifier = Modifier.padding(padding))
            }
            is EditSecuritySettingsScreenState.Error -> {
                FullscreenError(
                    error = currentState.error,
                    onRetry = component::onRetry,
                    modifier = Modifier
                        .padding(padding)
                        .testTag(EditSecuritySettingsTestTags.GLOBAL_ERROR)
                )
            }
            is EditSecuritySettingsScreenState.Content -> {
                EditSecuritySettingsForm(
                    state = currentState,
                    onRecentAuthenticationValidityForOpenUserChanged = component::onRecentAuthenticationValidityForOpenUserChanged,
                    onRecentAuthenticationValidityForManagementUserChanged = component::onRecentAuthenticationValidityForManagementUserChanged,
                    onMfaTokenExpirationSecondsChanged = component::onMfaTokenExpirationSecondsChanged,
                    onPasswordMinLengthChanged = component::onPasswordMinLengthChanged,
                    onPasswordRequireLetterToggled = component::onPasswordRequireLetterToggled,
                    onPasswordRequireUpperCaseToggled = component::onPasswordRequireUpperCaseToggled,
                    onPasswordRequireLowerCaseToggled = component::onPasswordRequireLowerCaseToggled,
                    onPasswordRequireDigitToggled = component::onPasswordRequireDigitToggled,
                    onPasswordRequireSpecialCharToggled = component::onPasswordRequireSpecialCharToggled,
                    onCommonPasswordsChanged = component::onCommonPasswordsChanged,
                    onAccountLockoutMaxFailedPasswordAttemptsChanged = component::onAccountLockoutMaxFailedPasswordAttemptsChanged,
                    onAccountLockoutMaxFailedOtpAttemptsChanged = component::onAccountLockoutMaxFailedOtpAttemptsChanged,
                    onAccountLockoutMaxFailedTotpAttemptsChanged = component::onAccountLockoutMaxFailedTotpAttemptsChanged,
                    onAccountLockoutFailedAttemptsWindowSecondsChanged = component::onAccountLockoutFailedAttemptsWindowSecondsChanged,
                    onAccountLockoutDurationSecondsChanged = component::onAccountLockoutDurationSecondsChanged,
                    onAccountLockoutIndefiniteLockoutThresholdChanged = component::onAccountLockoutIndefiniteLockoutThresholdChanged,
                    onAccountLockoutIsSelfServiceUnlockEnabledToggled = component::onAccountLockoutIsSelfServiceUnlockEnabledToggled,
                    onAccountLockoutCheckIntervalSecondsChanged = component::onAccountLockoutCheckIntervalSecondsChanged,
                    onRefreshTokenRotationGracePeriodSecondsChanged = component::onRefreshTokenRotationGracePeriodSecondsChanged,
                    onOpenIpBlacklistEnabledToggled = component::onOpenIpBlacklistEnabledToggled,
                    onOpenIpBlacklistChanged = component::onOpenIpBlacklistChanged,
                    onOpenIpWhitelistEnabledToggled = component::onOpenIpWhitelistEnabledToggled,
                    onOpenIpWhitelistChanged = component::onOpenIpWhitelistChanged,
                    onManagementIpBlacklistEnabledToggled = component::onManagementIpBlacklistEnabledToggled,
                    onManagementIpBlacklistChanged = component::onManagementIpBlacklistChanged,
                    onManagementIpWhitelistEnabledToggled = component::onManagementIpWhitelistEnabledToggled,
                    onManagementIpWhitelistChanged = component::onManagementIpWhitelistChanged,
                    onOtpRetryAfterSecondsChanged = component::onOtpRetryAfterSecondsChanged,
                    onOtpNumberOfSymbolsChanged = component::onOtpNumberOfSymbolsChanged,
                    onOtpExpirationSecondsChanged = component::onOtpExpirationSecondsChanged,
                    onMaxRequestsPerPeriodChanged = component::onMaxRequestsPerPeriodChanged,
                    onRateLimitPeriodSecondsChanged = component::onRateLimitPeriodSecondsChanged,
                    onSaveClick = component::onSaveClick,
                    onResetClick = component::onResetClick,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(CoreTheme.dimens.paddingMedium)
                        .verticalScroll(rememberScrollState())
                )
            }
        }
    }
}

@Composable
private fun EditSecuritySettingsForm(
    state: EditSecuritySettingsScreenState.Content,
    onRecentAuthenticationValidityForOpenUserChanged: (String) -> Unit,
    onRecentAuthenticationValidityForManagementUserChanged: (String) -> Unit,
    onMfaTokenExpirationSecondsChanged: (String) -> Unit,
    onPasswordMinLengthChanged: (String) -> Unit,
    onPasswordRequireLetterToggled: (Boolean) -> Unit,
    onPasswordRequireUpperCaseToggled: (Boolean) -> Unit,
    onPasswordRequireLowerCaseToggled: (Boolean) -> Unit,
    onPasswordRequireDigitToggled: (Boolean) -> Unit,
    onPasswordRequireSpecialCharToggled: (Boolean) -> Unit,
    onCommonPasswordsChanged: (String) -> Unit,
    onAccountLockoutMaxFailedPasswordAttemptsChanged: (String) -> Unit,
    onAccountLockoutMaxFailedOtpAttemptsChanged: (String) -> Unit,
    onAccountLockoutMaxFailedTotpAttemptsChanged: (String) -> Unit,
    onAccountLockoutFailedAttemptsWindowSecondsChanged: (String) -> Unit,
    onAccountLockoutDurationSecondsChanged: (String) -> Unit,
    onAccountLockoutIndefiniteLockoutThresholdChanged: (String) -> Unit,
    onAccountLockoutIsSelfServiceUnlockEnabledToggled: (Boolean) -> Unit,
    onAccountLockoutCheckIntervalSecondsChanged: (String) -> Unit,
    onRefreshTokenRotationGracePeriodSecondsChanged: (String) -> Unit,
    onOpenIpBlacklistEnabledToggled: (Boolean) -> Unit,
    onOpenIpBlacklistChanged: (String) -> Unit,
    onOpenIpWhitelistEnabledToggled: (Boolean) -> Unit,
    onOpenIpWhitelistChanged: (String) -> Unit,
    onManagementIpBlacklistEnabledToggled: (Boolean) -> Unit,
    onManagementIpBlacklistChanged: (String) -> Unit,
    onManagementIpWhitelistEnabledToggled: (Boolean) -> Unit,
    onManagementIpWhitelistChanged: (String) -> Unit,
    onOtpRetryAfterSecondsChanged: (String) -> Unit,
    onOtpNumberOfSymbolsChanged: (String) -> Unit,
    onOtpExpirationSecondsChanged: (String) -> Unit,
    onMaxRequestsPerPeriodChanged: (String) -> Unit,
    onRateLimitPeriodSecondsChanged: (String) -> Unit,
    onSaveClick: () -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingMedium)
    ) {
        CoreTitleText(
            text = stringResource(Res.string.general_security),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.SECTION_GENERAL_TITLE)
        )

        CoreOutlinedTextField(
            value = state.recentAuthenticationValiditySecondsForOpenUser,
            onValueChange = onRecentAuthenticationValidityForOpenUserChanged,
            label = { CoreBodyText(stringResource(Res.string.recent_authentication_validity_seconds)) },
            placeholder = { CoreBodyText(stringResource(Res.string.recent_authentication_validity_seconds)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.RECENT_AUTH_VALIDITY_INPUT)
        )

        CoreOutlinedTextField(
            value = state.recentAuthenticationValiditySecondsForManagementUser,
            onValueChange = onRecentAuthenticationValidityForManagementUserChanged,
            label = { CoreBodyText(stringResource(Res.string.recent_authentication_validity_for_management)) },
            placeholder = { CoreBodyText(stringResource(Res.string.recent_authentication_validity_for_management)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.RECENT_MANAGEMENT_AUTH_VALIDITY_INPUT)
        )

        CoreOutlinedTextField(
            value = state.mfaTokenExpirationSeconds,
            onValueChange = onMfaTokenExpirationSecondsChanged,
            label = { CoreBodyText(stringResource(Res.string.mfa_token_expiration_seconds)) },
            placeholder = { CoreBodyText(stringResource(Res.string.mfa_token_expiration_seconds)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.MFA_TOKEN_EXPIRATION_INPUT)
        )

        CoreOutlinedTextField(
            value = state.refreshTokenRotationGracePeriodSeconds,
            onValueChange = onRefreshTokenRotationGracePeriodSecondsChanged,
            label = { CoreBodyText(stringResource(Res.string.refresh_token_rotation_grace_period_seconds)) },
            placeholder = { CoreBodyText(stringResource(Res.string.refresh_token_rotation_grace_period_seconds)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.REFRESH_TOKEN_ROTATION_GRACE_PERIOD_INPUT)
        )

        CoreTitleText(
            text = stringResource(Res.string.rate_limiting),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.SECTION_RATE_LIMITING_TITLE)
        )

        CoreOutlinedTextField(
            value = state.maxRequestsPerPeriod,
            onValueChange = onMaxRequestsPerPeriodChanged,
            label = { CoreBodyText(stringResource(Res.string.max_requests_per_period)) },
            placeholder = { CoreBodyText(stringResource(Res.string.max_requests_per_period)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.MAX_REQUESTS_PER_PERIOD_INPUT)
        )

        CoreOutlinedTextField(
            value = state.rateLimitPeriodSeconds,
            onValueChange = onRateLimitPeriodSecondsChanged,
            label = { CoreBodyText(stringResource(Res.string.rate_limit_period_seconds)) },
            placeholder = { CoreBodyText(stringResource(Res.string.rate_limit_period_seconds)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.RATE_LIMIT_PERIOD_INPUT)
        )

        CoreTitleText(
            text = stringResource(Res.string.password_policy),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.SECTION_PASSWORD_POLICY_TITLE)
        )

        CoreOutlinedTextField(
            value = state.passwordMinLength,
            onValueChange = onPasswordMinLengthChanged,
            label = { CoreBodyText(stringResource(Res.string.password_min_length)) },
            placeholder = { CoreBodyText(stringResource(Res.string.password_min_length)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.PASSWORD_MIN_LENGTH_INPUT)
        )

        PolicyCheckboxRow(
            label = stringResource(Res.string.password_require_letter),
            checked = state.passwordRequireLetter,
            onCheckedChange = onPasswordRequireLetterToggled,
            testTag = EditSecuritySettingsTestTags.PASSWORD_REQUIRE_LETTER_CHECKBOX
        )

        PolicyCheckboxRow(
            label = stringResource(Res.string.password_require_uppercase),
            checked = state.passwordRequireUpperCase,
            onCheckedChange = onPasswordRequireUpperCaseToggled,
            testTag = EditSecuritySettingsTestTags.PASSWORD_REQUIRE_UPPERCASE_CHECKBOX
        )

        PolicyCheckboxRow(
            label = stringResource(Res.string.password_require_lowercase),
            checked = state.passwordRequireLowerCase,
            onCheckedChange = onPasswordRequireLowerCaseToggled,
            testTag = EditSecuritySettingsTestTags.PASSWORD_REQUIRE_LOWERCASE_CHECKBOX
        )

        PolicyCheckboxRow(
            label = stringResource(Res.string.password_require_digit),
            checked = state.passwordRequireDigit,
            onCheckedChange = onPasswordRequireDigitToggled,
            testTag = EditSecuritySettingsTestTags.PASSWORD_REQUIRE_DIGIT_CHECKBOX
        )

        PolicyCheckboxRow(
            label = stringResource(Res.string.password_require_special_char),
            checked = state.passwordRequireSpecialChar,
            onCheckedChange = onPasswordRequireSpecialCharToggled,
            testTag = EditSecuritySettingsTestTags.PASSWORD_REQUIRE_SPECIAL_CHAR_CHECKBOX
        )

        CoreOutlinedTextField(
            value = state.commonPasswords,
            onValueChange = onCommonPasswordsChanged,
            label = { CoreBodyText(stringResource(Res.string.common_passwords_placeholder)) },
            placeholder = { CoreBodyText(stringResource(Res.string.common_passwords_placeholder)) },
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.COMMON_PASSWORDS_INPUT)
        )

        CoreTitleText(
            text = stringResource(Res.string.account_lockout_policy_section),
            style = MaterialTheme.typography.titleMedium
        )

        CoreOutlinedTextField(
            value = state.accountLockoutMaxFailedPasswordAttempts,
            onValueChange = onAccountLockoutMaxFailedPasswordAttemptsChanged,
            label = { CoreBodyText(stringResource(Res.string.max_failed_password_attempts)) },
            placeholder = { CoreBodyText(stringResource(Res.string.max_failed_password_attempts)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        CoreOutlinedTextField(
            value = state.accountLockoutMaxFailedOtpAttempts,
            onValueChange = onAccountLockoutMaxFailedOtpAttemptsChanged,
            label = { CoreBodyText(stringResource(Res.string.max_failed_otp_attempts)) },
            placeholder = { CoreBodyText(stringResource(Res.string.max_failed_otp_attempts)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        CoreOutlinedTextField(
            value = state.accountLockoutMaxFailedTotpAttempts,
            onValueChange = onAccountLockoutMaxFailedTotpAttemptsChanged,
            label = { CoreBodyText(stringResource(Res.string.max_failed_totp_attempts)) },
            placeholder = { CoreBodyText(stringResource(Res.string.max_failed_totp_attempts)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        CoreOutlinedTextField(
            value = state.accountLockoutFailedAttemptsWindowSeconds,
            onValueChange = onAccountLockoutFailedAttemptsWindowSecondsChanged,
            label = { CoreBodyText(stringResource(Res.string.failed_attempts_window_seconds)) },
            placeholder = { CoreBodyText(stringResource(Res.string.failed_attempts_window_seconds)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        CoreOutlinedTextField(
            value = state.accountLockoutDurationSeconds,
            onValueChange = onAccountLockoutDurationSecondsChanged,
            label = { CoreBodyText(stringResource(Res.string.lockout_duration_seconds)) },
            placeholder = { CoreBodyText(stringResource(Res.string.lockout_duration_seconds)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        CoreOutlinedTextField(
            value = state.accountLockoutIndefiniteLockoutThreshold,
            onValueChange = onAccountLockoutIndefiniteLockoutThresholdChanged,
            label = { CoreBodyText(stringResource(Res.string.indefinite_lockout_threshold)) },
            placeholder = { CoreBodyText(stringResource(Res.string.indefinite_lockout_threshold)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        PolicyCheckboxRow(
            label = stringResource(Res.string.self_service_unlock_enabled),
            checked = state.accountLockoutIsSelfServiceUnlockEnabled,
            onCheckedChange = onAccountLockoutIsSelfServiceUnlockEnabledToggled,
            testTag = EditSecuritySettingsTestTags.LOCKOUT_SELF_SERVICE_CHECKBOX
        )

        CoreOutlinedTextField(
            value = state.accountLockoutCheckIntervalSeconds,
            onValueChange = onAccountLockoutCheckIntervalSecondsChanged,
            label = { CoreBodyText(stringResource(Res.string.account_lockout_check_interval_seconds)) },
            placeholder = { CoreBodyText(stringResource(Res.string.account_lockout_check_interval_seconds)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        CoreTitleText(
            text = stringResource(Res.string.open_ip_restriction_policy),
            style = MaterialTheme.typography.titleMedium
        )

        PolicyCheckboxRow(
            label = stringResource(Res.string.blacklist_enabled),
            checked = state.openIpBlacklistEnabled,
            onCheckedChange = onOpenIpBlacklistEnabledToggled,
            testTag = EditSecuritySettingsTestTags.OPEN_IP_BLACKLIST_CHECKBOX
        )

        CoreOutlinedTextField(
            value = state.openIpBlacklist,
            onValueChange = onOpenIpBlacklistChanged,
            label = { CoreBodyText(stringResource(Res.string.blacklist_placeholder)) },
            placeholder = { CoreBodyText(stringResource(Res.string.blacklist_placeholder)) }
        )

        PolicyCheckboxRow(
            label = stringResource(Res.string.whitelist_enabled),
            checked = state.openIpWhitelistEnabled,
            onCheckedChange = onOpenIpWhitelistEnabledToggled,
            testTag = EditSecuritySettingsTestTags.OPEN_IP_WHITELIST_CHECKBOX
        )

        CoreOutlinedTextField(
            value = state.openIpWhitelist,
            onValueChange = onOpenIpWhitelistChanged,
            label = { CoreBodyText(stringResource(Res.string.whitelist_placeholder)) },
            placeholder = { CoreBodyText(stringResource(Res.string.whitelist_placeholder)) }
        )

        CoreTitleText(
            text = stringResource(Res.string.management_ip_restriction_policy),
            style = MaterialTheme.typography.titleMedium
        )

        PolicyCheckboxRow(
            label = stringResource(Res.string.blacklist_enabled),
            checked = state.managementIpBlacklistEnabled,
            onCheckedChange = onManagementIpBlacklistEnabledToggled,
            testTag = EditSecuritySettingsTestTags.MGMT_IP_BLACKLIST_CHECKBOX
        )

        CoreOutlinedTextField(
            value = state.managementIpBlacklist,
            onValueChange = onManagementIpBlacklistChanged,
            label = { CoreBodyText(stringResource(Res.string.blacklist_placeholder)) },
            placeholder = { CoreBodyText(stringResource(Res.string.blacklist_placeholder)) }
        )

        PolicyCheckboxRow(
            label = stringResource(Res.string.whitelist_enabled),
            checked = state.managementIpWhitelistEnabled,
            onCheckedChange = onManagementIpWhitelistEnabledToggled,
            testTag = EditSecuritySettingsTestTags.MGMT_IP_WHITELIST_CHECKBOX
        )

        CoreOutlinedTextField(
            value = state.managementIpWhitelist,
            onValueChange = onManagementIpWhitelistChanged,
            label = { CoreBodyText(stringResource(Res.string.whitelist_placeholder)) },
            placeholder = { CoreBodyText(stringResource(Res.string.whitelist_placeholder)) }
        )

        CoreTitleText(
            text = stringResource(Res.string.otp_confirmation),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.SECTION_OTP_TITLE)
        )

        CoreOutlinedTextField(
            value = state.otpRetryAfterSeconds,
            onValueChange = onOtpRetryAfterSecondsChanged,
            label = { CoreBodyText(stringResource(Res.string.otp_retry_after_seconds)) },
            placeholder = { CoreBodyText(stringResource(Res.string.otp_retry_after_seconds)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.OTP_RETRY_AFTER_INPUT)
        )

        CoreOutlinedTextField(
            value = state.otpNumberOfSymbols,
            onValueChange = onOtpNumberOfSymbolsChanged,
            label = { CoreBodyText(stringResource(Res.string.otp_number_of_symbols)) },
            placeholder = { CoreBodyText(stringResource(Res.string.otp_number_of_symbols)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.OTP_NUMBER_OF_SYMBOLS_INPUT)
        )

        CoreOutlinedTextField(
            value = state.otpExpirationSeconds,
            onValueChange = onOtpExpirationSecondsChanged,
            label = { CoreBodyText(stringResource(Res.string.otp_expiration_seconds)) },
            placeholder = { CoreBodyText(stringResource(Res.string.otp_expiration_seconds)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.OTP_EXPIRATION_INPUT)
        )

        ErrorText(
            error = state.saveError,
            testTag = EditSecuritySettingsTestTags.SAVE_ERROR_TEXT
        )

        CoreButton(
            text = stringResource(if (state.isSaving) Res.string.saving else Res.string.save),
            onClick = onSaveClick,
            enabled = !state.isSaving,
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.SAVE_BUTTON)
        )

        CoreTextButton(
            text = stringResource(Res.string.reset_to_defaults),
            onClick = onResetClick,
            enabled = !state.isSaving,
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.RESET_BUTTON)
        )
    }
}

@Composable
private fun PolicyCheckboxRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.testTag(testTag)
        )
        CoreBodyText(
            text = label,
            modifier = Modifier.padding(start = CoreTheme.dimens.paddingSmall)
        )
    }
}

@Composable
private fun ErrorText(error: AppError?, testTag: String) {
    AnimatedVisibility(
        visible = error != null,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        error?.let {
            CoreErrorText(
                text = it.toLocalizedMessage(),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = CoreTheme.dimens.paddingSmall)
                    .testTag(testTag)
            )
        }
    }
}

@InternalApi
internal class EditSecuritySettingsPreviewProvider : PreviewParameterProvider<EditSecuritySettingsScreenState> {
    private val sampleContent = EditSecuritySettingsScreenState.Content(
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

    private val items: List<Pair<String, EditSecuritySettingsScreenState>> = listOf(
        "Content" to sampleContent,
        "Saving" to sampleContent.copy(isSaving = true),
        "Save Error" to sampleContent.copy(saveError = CommonError.Unknown()),
        "Error" to EditSecuritySettingsScreenState.Error(error = CommonError.Unknown()),
        "Loading" to EditSecuritySettingsScreenState.Loading
    )

    override val values: Sequence<EditSecuritySettingsScreenState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun EditSecuritySettingsScreenPreviewContent(state: EditSecuritySettingsScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        EditSecuritySettingsScreen(
            component = EditSecuritySettingsComponentMock(initialState = state)
        )
    }
}

private val defaultEditSecuritySettingsPreviewState = EditSecuritySettingsScreenState.Content(
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

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StatesPreview(
    @PreviewParameter(EditSecuritySettingsPreviewProvider::class) state: EditSecuritySettingsScreenState
) {
    ScreenPreviewContainer {
        EditSecuritySettingsScreenPreviewContent(state = state)
    }
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        EditSecuritySettingsScreenPreviewContent(state = defaultEditSecuritySettingsPreviewState)
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        EditSecuritySettingsScreenPreviewContent(state = defaultEditSecuritySettingsPreviewState)
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        EditSecuritySettingsScreenPreviewContent(state = defaultEditSecuritySettingsPreviewState)
    }
}

object EditSecuritySettingsTestTags {
    const val TITLE = "EditSecuritySettings_Title"
    const val BACK_BUTTON = "EditSecuritySettings_BackButton"
    const val GLOBAL_ERROR = "EditSecuritySettings_GlobalError"

    const val SECTION_GENERAL_TITLE = "EditSecuritySettings_SectionGeneralTitle"
    const val RECENT_AUTH_VALIDITY_INPUT = "EditSecuritySettings_RecentAuthValidityInput"
    const val RECENT_MANAGEMENT_AUTH_VALIDITY_INPUT = "EditSecuritySettings_RecentManagementAuthValidityInput"
    const val MFA_TOKEN_EXPIRATION_INPUT = "EditSecuritySettings_MfaTokenExpirationInput"
    const val REFRESH_TOKEN_ROTATION_GRACE_PERIOD_INPUT = "EditSecuritySettings_RefreshTokenRotationGracePeriodInput"

    const val SECTION_RATE_LIMITING_TITLE = "EditSecuritySettings_SectionRateLimitingTitle"
    const val MAX_REQUESTS_PER_PERIOD_INPUT = "EditSecuritySettings_MaxRequestsPerPeriodInput"
    const val RATE_LIMIT_PERIOD_INPUT = "EditSecuritySettings_RateLimitPeriodInput"

    const val SECTION_PASSWORD_POLICY_TITLE = "EditSecuritySettings_SectionPasswordPolicyTitle"
    const val PASSWORD_MIN_LENGTH_INPUT = "EditSecuritySettings_PasswordMinLengthInput"
    const val PASSWORD_REQUIRE_LETTER_CHECKBOX = "EditSecuritySettings_PasswordRequireLetterCheckbox"
    const val PASSWORD_REQUIRE_UPPERCASE_CHECKBOX = "EditSecuritySettings_PasswordRequireUppercaseCheckbox"
    const val PASSWORD_REQUIRE_LOWERCASE_CHECKBOX = "EditSecuritySettings_PasswordRequireLowercaseCheckbox"
    const val PASSWORD_REQUIRE_DIGIT_CHECKBOX = "EditSecuritySettings_PasswordRequireDigitCheckbox"
    const val PASSWORD_REQUIRE_SPECIAL_CHAR_CHECKBOX = "EditSecuritySettings_PasswordRequireSpecialCharCheckbox"
    const val COMMON_PASSWORDS_INPUT = "EditSecuritySettings_CommonPasswordsInput"

    const val LOCKOUT_SELF_SERVICE_CHECKBOX = "EditSecuritySettings_LockoutSelfServiceCheckbox"
    const val OPEN_IP_BLACKLIST_CHECKBOX = "EditSecuritySettings_OpenIpBlacklistCheckbox"
    const val OPEN_IP_WHITELIST_CHECKBOX = "EditSecuritySettings_OpenIpWhitelistCheckbox"
    const val MGMT_IP_BLACKLIST_CHECKBOX = "EditSecuritySettings_MgmtIpBlacklistCheckbox"
    const val MGMT_IP_WHITELIST_CHECKBOX = "EditSecuritySettings_MgmtIpWhitelistCheckbox"

    const val SECTION_OTP_TITLE = "EditSecuritySettings_SectionOtpTitle"
    const val OTP_RETRY_AFTER_INPUT = "EditSecuritySettings_OtpRetryAfterInput"
    const val OTP_NUMBER_OF_SYMBOLS_INPUT = "EditSecuritySettings_OtpNumberOfSymbolsInput"
    const val OTP_EXPIRATION_INPUT = "EditSecuritySettings_OtpExpirationInput"

    const val SAVE_ERROR_TEXT = "EditSecuritySettings_SaveErrorText"
    const val SAVE_BUTTON = "EditSecuritySettings_SaveButton"
    const val RESET_BUTTON = "EditSecuritySettings_ResetButton"
}