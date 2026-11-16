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
import androidx.compose.material3.AlertDialog
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
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewFontScales
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewScreenSizes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewThemes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.settings.security.EditSecuritySettingsComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.dialog.SettingsDialogChild
import io.github.mudrichenkoevgeny.kmp.feature.user.localization.LocalUserStrings

/**
 * Screen for editing security policies and settings.
 *
 * @param component Decompose controller for security settings editing.
 * @param modifier Modifier for the screen layout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun EditSecuritySettingsScreen(
    component: EditSecuritySettingsComponent,
    modifier: Modifier = Modifier
) {
    val state by component.state.subscribeAsState()
    val userUi = LocalUserStrings.current.ui

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    CoreScreenTitleText(
                        text = userUi.editSecuritySettingsTitle,
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
                    onRecentAuthenticationValidityForOpenUserChange =
                        component::onRecentAuthenticationValidityForOpenUserChanged,
                    onRecentAuthenticationValidityForManagementUserChange =
                        component::onRecentAuthenticationValidityForManagementUserChanged,
                    onMfaTokenExpirationSecondsChange =
                        component::onMfaTokenExpirationSecondsChanged,
                    onRefreshTokenRotationGracePeriodSecondsChange =
                        component::onRefreshTokenRotationGracePeriodSecondsChanged,
                    onMaxRequestsPerPeriodChange = component::onMaxRequestsPerPeriodChanged,
                    onRateLimitPeriodSecondsChange = component::onRateLimitPeriodSecondsChanged,
                    onPasswordMinLengthChange = component::onPasswordMinLengthChanged,
                    onPasswordRequireLetterToggle = component::onPasswordRequireLetterToggled,
                    onPasswordRequireUppercaseToggle = component::onPasswordRequireUpperCaseToggled,
                    onPasswordRequireLowercaseToggle = component::onPasswordRequireLowerCaseToggled,
                    onPasswordRequireDigitToggle = component::onPasswordRequireDigitToggled,
                    onPasswordRequireSpecialCharToggle = component::onPasswordRequireSpecialCharToggled,
                    onCommonPasswordsChange = component::onCommonPasswordsChanged,
                    onMaxFailedPasswordAttemptsChange = component::onAccountLockoutMaxFailedPasswordAttemptsChanged,
                    onMaxFailedOtpAttemptsChange = component::onAccountLockoutMaxFailedOtpAttemptsChanged,
                    onMaxFailedTotpAttemptsChange = component::onAccountLockoutMaxFailedTotpAttemptsChanged,
                    onFailedAttemptsWindowSecondsChange = component::onAccountLockoutFailedAttemptsWindowSecondsChanged,
                    onLockoutDurationSecondsChange = component::onAccountLockoutDurationSecondsChanged,
                    onIndefiniteLockoutThresholdChange = component::onAccountLockoutIndefiniteLockoutThresholdChanged,
                    onSelfServiceUnlockEnabledToggle = component::onAccountLockoutIsSelfServiceUnlockEnabledToggled,
                    onAccountLockoutCheckIntervalSecondsChange = component::onAccountLockoutCheckIntervalSecondsChanged,
                    onOpenIpBlacklistEnabledToggle = component::onOpenIpBlacklistEnabledToggled,
                    onOpenIpBlacklistChange = component::onOpenIpBlacklistChanged,
                    onOpenIpWhitelistEnabledToggle = component::onOpenIpWhitelistEnabledToggled,
                    onOpenIpWhitelistChange = component::onOpenIpWhitelistChanged,
                    onManagementIpBlacklistEnabledToggle = component::onManagementIpBlacklistEnabledToggled,
                    onManagementIpBlacklistChange = component::onManagementIpBlacklistChanged,
                    onManagementIpWhitelistEnabledToggle = component::onManagementIpWhitelistEnabledToggled,
                    onManagementIpWhitelistChange = component::onManagementIpWhitelistChanged,
                    onOtpRetryAfterSecondsChange = component::onOtpRetryAfterSecondsChanged,
                    onOtpExpirationSecondsChange = component::onOtpExpirationSecondsChanged,
                    onOtpCodeLengthChange = component::onOtpNumberOfSymbolsChanged,
                    onSaveClick = component::onSaveClick,
                    onResetClick = component::onResetClick,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(CoreTheme.dimens.paddingMedium)
                        .verticalScroll(rememberScrollState())
                )

                val dialogSlot by component.dialogSlot.subscribeAsState()
                val dialogChild = dialogSlot.child?.instance
                if (dialogChild != null) {
                    when (dialogChild) {
                        is SettingsDialogChild.ConfirmReset -> {
                            AlertDialog(
                                onDismissRequest = component::onDismissDialog,
                                title = { CoreTitleText(text = userUi.dialogConfirmTitle) },
                                text = { CoreBodyText(text = userUi.resetToDefaults) },
                                confirmButton = {
                                    CoreTextButton(
                                        text = userUi.dialogConfirm,
                                        onClick = component::onConfirmReset
                                    )
                                },
                                dismissButton = {
                                    CoreTextButton(
                                        text = userUi.dialogCancel,
                                        onClick = component::onDismissDialog
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EditSecuritySettingsForm(
    state: EditSecuritySettingsScreenState.Content,
    onRecentAuthenticationValidityForOpenUserChange: (String) -> Unit,
    onRecentAuthenticationValidityForManagementUserChange: (String) -> Unit,
    onMfaTokenExpirationSecondsChange: (String) -> Unit,
    onRefreshTokenRotationGracePeriodSecondsChange: (String) -> Unit,
    onMaxRequestsPerPeriodChange: (String) -> Unit,
    onRateLimitPeriodSecondsChange: (String) -> Unit,
    onPasswordMinLengthChange: (String) -> Unit,
    onPasswordRequireLetterToggle: (Boolean) -> Unit,
    onPasswordRequireUppercaseToggle: (Boolean) -> Unit,
    onPasswordRequireLowercaseToggle: (Boolean) -> Unit,
    onPasswordRequireDigitToggle: (Boolean) -> Unit,
    onPasswordRequireSpecialCharToggle: (Boolean) -> Unit,
    onCommonPasswordsChange: (String) -> Unit,
    onMaxFailedPasswordAttemptsChange: (String) -> Unit,
    onMaxFailedOtpAttemptsChange: (String) -> Unit,
    onMaxFailedTotpAttemptsChange: (String) -> Unit,
    onFailedAttemptsWindowSecondsChange: (String) -> Unit,
    onLockoutDurationSecondsChange: (String) -> Unit,
    onIndefiniteLockoutThresholdChange: (String) -> Unit,
    onSelfServiceUnlockEnabledToggle: (Boolean) -> Unit,
    onAccountLockoutCheckIntervalSecondsChange: (String) -> Unit,
    onOpenIpBlacklistEnabledToggle: (Boolean) -> Unit,
    onOpenIpBlacklistChange: (String) -> Unit,
    onOpenIpWhitelistEnabledToggle: (Boolean) -> Unit,
    onOpenIpWhitelistChange: (String) -> Unit,
    onManagementIpBlacklistEnabledToggle: (Boolean) -> Unit,
    onManagementIpBlacklistChange: (String) -> Unit,
    onManagementIpWhitelistEnabledToggle: (Boolean) -> Unit,
    onManagementIpWhitelistChange: (String) -> Unit,
    onOtpRetryAfterSecondsChange: (String) -> Unit,
    onOtpExpirationSecondsChange: (String) -> Unit,
    onOtpCodeLengthChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userUi = LocalUserStrings.current.ui

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingMedium)
    ) {
        GeneralSecuritySection(
            state = state,
            onRecentAuthenticationValidityForOpenUserChange =
                onRecentAuthenticationValidityForOpenUserChange,
            onRecentAuthenticationValidityForManagementUserChange =
                onRecentAuthenticationValidityForManagementUserChange,
            onMfaTokenExpirationSecondsChange =
                onMfaTokenExpirationSecondsChange,
            onRefreshTokenRotationGracePeriodSecondsChange =
                onRefreshTokenRotationGracePeriodSecondsChange
        )

        RateLimitingSection(
            state = state,
            onMaxRequestsPerPeriodChange = onMaxRequestsPerPeriodChange,
            onRateLimitPeriodSecondsChange = onRateLimitPeriodSecondsChange
        )

        PasswordPolicySection(
            state = state,
            onPasswordMinLengthChange = onPasswordMinLengthChange,
            onPasswordRequireLetterToggle = onPasswordRequireLetterToggle,
            onPasswordRequireUppercaseToggle = onPasswordRequireUppercaseToggle,
            onPasswordRequireLowercaseToggle = onPasswordRequireLowercaseToggle,
            onPasswordRequireDigitToggle = onPasswordRequireDigitToggle,
            onPasswordRequireSpecialCharToggle = onPasswordRequireSpecialCharToggle,
            onCommonPasswordsChange = onCommonPasswordsChange
        )

        AccountLockoutPolicySection(
            state = state,
            onMaxFailedPasswordAttemptsChange = onMaxFailedPasswordAttemptsChange,
            onMaxFailedOtpAttemptsChange = onMaxFailedOtpAttemptsChange,
            onMaxFailedTotpAttemptsChange = onMaxFailedTotpAttemptsChange,
            onFailedAttemptsWindowSecondsChange = onFailedAttemptsWindowSecondsChange,
            onLockoutDurationSecondsChange = onLockoutDurationSecondsChange,
            onIndefiniteLockoutThresholdChange = onIndefiniteLockoutThresholdChange,
            onSelfServiceUnlockEnabledToggle = onSelfServiceUnlockEnabledToggle,
            onAccountLockoutCheckIntervalSecondsChange = onAccountLockoutCheckIntervalSecondsChange
        )

        IpRestrictionPolicySection(
            state = state,
            onOpenIpBlacklistEnabledToggle = onOpenIpBlacklistEnabledToggle,
            onOpenIpBlacklistChange = onOpenIpBlacklistChange,
            onOpenIpWhitelistEnabledToggle = onOpenIpWhitelistEnabledToggle,
            onOpenIpWhitelistChange = onOpenIpWhitelistChange,
            onManagementIpBlacklistEnabledToggle = onManagementIpBlacklistEnabledToggle,
            onManagementIpBlacklistChange = onManagementIpBlacklistChange,
            onManagementIpWhitelistEnabledToggle = onManagementIpWhitelistEnabledToggle,
            onManagementIpWhitelistChange = onManagementIpWhitelistChange
        )

        OtpConfirmationSection(
            state = state,
            onOtpRetryAfterSecondsChange = onOtpRetryAfterSecondsChange,
            onOtpExpirationSecondsChange = onOtpExpirationSecondsChange,
            onOtpCodeLengthChange = onOtpCodeLengthChange
        )

        ErrorText(
            error = state.saveError,
            testTag = EditSecuritySettingsTestTags.SAVE_ERROR_TEXT
        )

        CoreButton(
            text = if (state.isSaving) userUi.saving else userUi.save,
            onClick = onSaveClick,
            enabled = !state.isSaving,
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.SAVE_BUTTON)
        )

        CoreTextButton(
            text = userUi.resetToDefaults,
            onClick = onResetClick,
            enabled = !state.isSaving,
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.RESET_BUTTON)
        )
    }
}

@Composable
private fun GeneralSecuritySection(
    state: EditSecuritySettingsScreenState.Content,
    onRecentAuthenticationValidityForOpenUserChange: (String) -> Unit,
    onRecentAuthenticationValidityForManagementUserChange: (String) -> Unit,
    onMfaTokenExpirationSecondsChange: (String) -> Unit,
    onRefreshTokenRotationGracePeriodSecondsChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val userUi = LocalUserStrings.current.ui

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingMedium)
    ) {
        CoreTitleText(
            text = userUi.generalSecurity,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.SECTION_GENERAL_TITLE)
        )

        CoreOutlinedTextField(
            value = state.recentAuthenticationValiditySecondsForOpenUser,
            onValueChange = onRecentAuthenticationValidityForOpenUserChange,
            label = { CoreBodyText(userUi.recentAuthenticationValiditySeconds) },
            placeholder = { CoreBodyText(userUi.recentAuthenticationValiditySeconds) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.RECENT_AUTH_VALIDITY_OPEN_INPUT)
        )

        CoreOutlinedTextField(
            value = state.recentAuthenticationValiditySecondsForManagementUser,
            onValueChange = onRecentAuthenticationValidityForManagementUserChange,
            label = { CoreBodyText(userUi.recentAuthenticationValidityForManagement) },
            placeholder = { CoreBodyText(userUi.recentAuthenticationValidityForManagement) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.RECENT_AUTH_VALIDITY_MANAGEMENT_INPUT)
        )

        CoreOutlinedTextField(
            value = state.mfaTokenExpirationSeconds,
            onValueChange = onMfaTokenExpirationSecondsChange,
            label = { CoreBodyText(userUi.mfaTokenExpirationSeconds) },
            placeholder = { CoreBodyText(userUi.mfaTokenExpirationSeconds) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.MFA_TOKEN_EXPIRATION_INPUT)
        )

        CoreOutlinedTextField(
            value = state.refreshTokenRotationGracePeriodSeconds,
            onValueChange = onRefreshTokenRotationGracePeriodSecondsChange,
            label = { CoreBodyText(userUi.refreshTokenRotationGracePeriodSeconds) },
            placeholder = { CoreBodyText(userUi.refreshTokenRotationGracePeriodSeconds) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.REFRESH_TOKEN_ROTATION_GRACE_PERIOD_INPUT)
        )
    }
}

@Composable
private fun RateLimitingSection(
    state: EditSecuritySettingsScreenState.Content,
    onMaxRequestsPerPeriodChange: (String) -> Unit,
    onRateLimitPeriodSecondsChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val userUi = LocalUserStrings.current.ui

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingMedium)
    ) {
        CoreTitleText(
            text = userUi.rateLimiting,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.SECTION_RATE_LIMITING_TITLE)
        )

        CoreOutlinedTextField(
            value = state.maxRequestsPerPeriod,
            onValueChange = onMaxRequestsPerPeriodChange,
            label = { CoreBodyText(userUi.maxRequestsPerPeriod) },
            placeholder = { CoreBodyText(userUi.maxRequestsPerPeriod) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.RATE_LIMIT_MAX_REQUESTS_INPUT)
        )

        CoreOutlinedTextField(
            value = state.rateLimitPeriodSeconds,
            onValueChange = onRateLimitPeriodSecondsChange,
            label = { CoreBodyText(userUi.rateLimitPeriodSeconds) },
            placeholder = { CoreBodyText(userUi.rateLimitPeriodSeconds) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.RATE_LIMIT_WINDOW_INPUT)
        )
    }
}

@Composable
private fun PasswordPolicySection(
    state: EditSecuritySettingsScreenState.Content,
    onPasswordMinLengthChange: (String) -> Unit,
    onPasswordRequireLetterToggle: (Boolean) -> Unit,
    onPasswordRequireUppercaseToggle: (Boolean) -> Unit,
    onPasswordRequireLowercaseToggle: (Boolean) -> Unit,
    onPasswordRequireDigitToggle: (Boolean) -> Unit,
    onPasswordRequireSpecialCharToggle: (Boolean) -> Unit,
    onCommonPasswordsChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val userUi = LocalUserStrings.current.ui

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingMedium)
    ) {
        CoreTitleText(
            text = userUi.passwordPolicy,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.SECTION_PASSWORD_POLICY_TITLE)
        )

        CoreOutlinedTextField(
            value = state.passwordMinLength,
            onValueChange = onPasswordMinLengthChange,
            label = { CoreBodyText(userUi.passwordMinLength) },
            placeholder = { CoreBodyText(userUi.passwordMinLength) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.PASSWORD_MIN_LENGTH_INPUT)
        )

        ToggleRow(
            label = userUi.passwordRequireLetter,
            checked = state.passwordRequireLetter,
            onCheckedChange = onPasswordRequireLetterToggle,
            testTag = EditSecuritySettingsTestTags.PASSWORD_REQUIRE_LETTER_CHECKBOX
        )

        ToggleRow(
            label = userUi.passwordRequireUppercase,
            checked = state.passwordRequireUpperCase,
            onCheckedChange = onPasswordRequireUppercaseToggle,
            testTag = EditSecuritySettingsTestTags.PASSWORD_REQUIRE_UPPERCASE_CHECKBOX
        )

        ToggleRow(
            label = userUi.passwordRequireLowercase,
            checked = state.passwordRequireLowerCase,
            onCheckedChange = onPasswordRequireLowercaseToggle,
            testTag = EditSecuritySettingsTestTags.PASSWORD_REQUIRE_LOWERCASE_CHECKBOX
        )

        ToggleRow(
            label = userUi.passwordRequireDigit,
            checked = state.passwordRequireDigit,
            onCheckedChange = onPasswordRequireDigitToggle,
            testTag = EditSecuritySettingsTestTags.PASSWORD_REQUIRE_DIGIT_CHECKBOX
        )

        ToggleRow(
            label = userUi.passwordRequireSpecialChar,
            checked = state.passwordRequireSpecialChar,
            onCheckedChange = onPasswordRequireSpecialCharToggle,
            testTag = EditSecuritySettingsTestTags.PASSWORD_REQUIRE_SPECIAL_CHAR_CHECKBOX
        )

        CoreOutlinedTextField(
            value = state.commonPasswords,
            onValueChange = onCommonPasswordsChange,
            label = { CoreBodyText(userUi.commonPasswordsPlaceholder) },
            placeholder = { CoreBodyText(userUi.commonPasswordsPlaceholder) }
        )
    }
}

@Composable
private fun AccountLockoutPolicySection(
    state: EditSecuritySettingsScreenState.Content,
    onMaxFailedPasswordAttemptsChange: (String) -> Unit,
    onMaxFailedOtpAttemptsChange: (String) -> Unit,
    onMaxFailedTotpAttemptsChange: (String) -> Unit,
    onFailedAttemptsWindowSecondsChange: (String) -> Unit,
    onLockoutDurationSecondsChange: (String) -> Unit,
    onIndefiniteLockoutThresholdChange: (String) -> Unit,
    onSelfServiceUnlockEnabledToggle: (Boolean) -> Unit,
    onAccountLockoutCheckIntervalSecondsChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val userUi = LocalUserStrings.current.ui

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingMedium)
    ) {
        CoreTitleText(
            text = userUi.accountLockoutPolicySection,
            style = MaterialTheme.typography.titleMedium
        )

        CoreOutlinedTextField(
            value = state.accountLockoutMaxFailedPasswordAttempts,
            onValueChange = onMaxFailedPasswordAttemptsChange,
            label = { CoreBodyText(userUi.maxFailedPasswordAttempts) },
            placeholder = { CoreBodyText(userUi.maxFailedPasswordAttempts) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        CoreOutlinedTextField(
            value = state.accountLockoutMaxFailedOtpAttempts,
            onValueChange = onMaxFailedOtpAttemptsChange,
            label = { CoreBodyText(userUi.maxFailedOtpAttempts) },
            placeholder = { CoreBodyText(userUi.maxFailedOtpAttempts) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        CoreOutlinedTextField(
            value = state.accountLockoutMaxFailedTotpAttempts,
            onValueChange = onMaxFailedTotpAttemptsChange,
            label = { CoreBodyText(userUi.maxFailedTotpAttempts) },
            placeholder = { CoreBodyText(userUi.maxFailedTotpAttempts) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        CoreOutlinedTextField(
            value = state.accountLockoutFailedAttemptsWindowSeconds,
            onValueChange = onFailedAttemptsWindowSecondsChange,
            label = { CoreBodyText(userUi.failedAttemptsWindowSeconds) },
            placeholder = { CoreBodyText(userUi.failedAttemptsWindowSeconds) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        CoreOutlinedTextField(
            value = state.accountLockoutDurationSeconds,
            onValueChange = onLockoutDurationSecondsChange,
            label = { CoreBodyText(userUi.lockoutDurationSeconds) },
            placeholder = { CoreBodyText(userUi.lockoutDurationSeconds) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        CoreOutlinedTextField(
            value = state.accountLockoutIndefiniteLockoutThreshold,
            onValueChange = onIndefiniteLockoutThresholdChange,
            label = { CoreBodyText(userUi.indefiniteLockoutThreshold) },
            placeholder = { CoreBodyText(userUi.indefiniteLockoutThreshold) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        ToggleRow(
            label = userUi.selfServiceUnlockEnabled,
            checked = state.accountLockoutIsSelfServiceUnlockEnabled,
            onCheckedChange = onSelfServiceUnlockEnabledToggle,
            testTag = ""
        )

        CoreOutlinedTextField(
            value = state.accountLockoutCheckIntervalSeconds,
            onValueChange = onAccountLockoutCheckIntervalSecondsChange,
            label = { CoreBodyText(userUi.accountLockoutCheckIntervalSeconds) },
            placeholder = { CoreBodyText(userUi.accountLockoutCheckIntervalSeconds) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

@Composable
private fun IpRestrictionPolicySection(
    state: EditSecuritySettingsScreenState.Content,
    onOpenIpBlacklistEnabledToggle: (Boolean) -> Unit,
    onOpenIpBlacklistChange: (String) -> Unit,
    onOpenIpWhitelistEnabledToggle: (Boolean) -> Unit,
    onOpenIpWhitelistChange: (String) -> Unit,
    onManagementIpBlacklistEnabledToggle: (Boolean) -> Unit,
    onManagementIpBlacklistChange: (String) -> Unit,
    onManagementIpWhitelistEnabledToggle: (Boolean) -> Unit,
    onManagementIpWhitelistChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val userUi = LocalUserStrings.current.ui

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingMedium)
    ) {
        CoreTitleText(
            text = userUi.openIpRestrictionPolicy,
            style = MaterialTheme.typography.titleMedium
        )

        ToggleRow(
            label = userUi.blacklistEnabled,
            checked = state.openIpBlacklistEnabled,
            onCheckedChange = onOpenIpBlacklistEnabledToggle,
            testTag = ""
        )

        CoreOutlinedTextField(
            value = state.openIpBlacklist,
            onValueChange = onOpenIpBlacklistChange,
            label = { CoreBodyText(userUi.blacklistPlaceholder) },
            placeholder = { CoreBodyText(userUi.blacklistPlaceholder) }
        )

        ToggleRow(
            label = userUi.whitelistEnabled,
            checked = state.openIpWhitelistEnabled,
            onCheckedChange = onOpenIpWhitelistEnabledToggle,
            testTag = ""
        )

        CoreOutlinedTextField(
            value = state.openIpWhitelist,
            onValueChange = onOpenIpWhitelistChange,
            label = { CoreBodyText(userUi.whitelistPlaceholder) },
            placeholder = { CoreBodyText(userUi.whitelistPlaceholder) }
        )

        CoreTitleText(
            text = userUi.managementIpRestrictionPolicy,
            style = MaterialTheme.typography.titleMedium
        )

        ToggleRow(
            label = userUi.blacklistEnabled,
            checked = state.managementIpBlacklistEnabled,
            onCheckedChange = onManagementIpBlacklistEnabledToggle,
            testTag = ""
        )

        CoreOutlinedTextField(
            value = state.managementIpBlacklist,
            onValueChange = onManagementIpBlacklistChange,
            label = { CoreBodyText(userUi.blacklistPlaceholder) },
            placeholder = { CoreBodyText(userUi.blacklistPlaceholder) }
        )

        ToggleRow(
            label = userUi.whitelistEnabled,
            checked = state.managementIpWhitelistEnabled,
            onCheckedChange = onManagementIpWhitelistEnabledToggle,
            testTag = ""
        )

        CoreOutlinedTextField(
            value = state.managementIpWhitelist,
            onValueChange = onManagementIpWhitelistChange,
            label = { CoreBodyText(userUi.whitelistPlaceholder) },
            placeholder = { CoreBodyText(userUi.whitelistPlaceholder) }
        )
    }
}

@Composable
private fun OtpConfirmationSection(
    state: EditSecuritySettingsScreenState.Content,
    onOtpRetryAfterSecondsChange: (String) -> Unit,
    onOtpExpirationSecondsChange: (String) -> Unit,
    onOtpCodeLengthChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val userUi = LocalUserStrings.current.ui

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingMedium)
    ) {
        CoreTitleText(
            text = userUi.otpConfirmation,
            style = MaterialTheme.typography.titleMedium
        )

        CoreOutlinedTextField(
            value = state.otpRetryAfterSeconds,
            onValueChange = onOtpRetryAfterSecondsChange,
            label = { CoreBodyText(userUi.otpRetryAfterSeconds) },
            placeholder = { CoreBodyText(userUi.otpRetryAfterSeconds) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        CoreOutlinedTextField(
            value = state.otpExpirationSeconds,
            onValueChange = onOtpExpirationSecondsChange,
            label = { CoreBodyText(userUi.otpExpirationSeconds) },
            placeholder = { CoreBodyText(userUi.otpExpirationSeconds) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        CoreOutlinedTextField(
            value = state.otpNumberOfSymbols,
            onValueChange = onOtpCodeLengthChange,
            label = { CoreBodyText(userUi.otpNumberOfSymbols) },
            placeholder = { CoreBodyText(userUi.otpNumberOfSymbols) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

@Composable
private fun ToggleRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = if (testTag.isNotEmpty()) Modifier.testTag(testTag) else Modifier
        )
        CoreBodyText(
            text = label,
            modifier = Modifier.padding(start = CoreTheme.dimens.paddingSmall)
        )
    }
}

@Composable
private fun ErrorText(
    error: AppError?,
    testTag: String,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = error != null,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
        modifier = modifier
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
        recentAuthenticationValiditySecondsForOpenUser = "900",
        recentAuthenticationValiditySecondsForManagementUser = "300",
        mfaTokenExpirationSeconds = "300",
        passwordMinLength = "8",
        passwordRequireLetter = true,
        passwordRequireUpperCase = true,
        passwordRequireLowerCase = true,
        passwordRequireDigit = true,
        passwordRequireSpecialChar = true,
        commonPasswords = "",
        accountLockoutMaxFailedPasswordAttempts = "5",
        accountLockoutMaxFailedOtpAttempts = "5",
        accountLockoutMaxFailedTotpAttempts = "5",
        accountLockoutFailedAttemptsWindowSeconds = "300",
        accountLockoutDurationSeconds = "900",
        accountLockoutIndefiniteLockoutThreshold = "10",
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
        otpExpirationSeconds = "300"
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
    recentAuthenticationValiditySecondsForOpenUser = "900",
    recentAuthenticationValiditySecondsForManagementUser = "300",
    mfaTokenExpirationSeconds = "300",
    passwordMinLength = "8",
    passwordRequireLetter = true,
    passwordRequireUpperCase = true,
    passwordRequireLowerCase = true,
    passwordRequireDigit = true,
    passwordRequireSpecialChar = true,
    commonPasswords = "",
    accountLockoutMaxFailedPasswordAttempts = "5",
    accountLockoutMaxFailedOtpAttempts = "5",
    accountLockoutMaxFailedTotpAttempts = "5",
    accountLockoutFailedAttemptsWindowSeconds = "300",
    accountLockoutDurationSeconds = "900",
    accountLockoutIndefiniteLockoutThreshold = "10",
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
    otpExpirationSeconds = "300"
)

@InternalApi
@PreviewScreenSizes
@Composable
private fun StatesPreview(
    @PreviewParameter(EditSecuritySettingsPreviewProvider::class) state: EditSecuritySettingsScreenState
) {
    ScreenPreviewContainer {
        EditSecuritySettingsScreenPreviewContent(state = state)
    }
}

@InternalApi
@PreviewScreenSizes
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        EditSecuritySettingsScreenPreviewContent(state = defaultEditSecuritySettingsPreviewState)
    }
}

@InternalApi
@PreviewThemes
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        EditSecuritySettingsScreenPreviewContent(state = defaultEditSecuritySettingsPreviewState)
    }
}

@InternalApi
@PreviewFontScales
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        EditSecuritySettingsScreenPreviewContent(state = defaultEditSecuritySettingsPreviewState)
    }
}

public object EditSecuritySettingsTestTags {
    public const val TITLE: String = "EditSecuritySettings_Title"
    public const val BACK_BUTTON: String = "EditSecuritySettings_BackButton"
    public const val GLOBAL_ERROR: String = "EditSecuritySettings_GlobalError"

    public const val SECTION_GENERAL_TITLE: String = "EditSecuritySettings_SectionGeneralTitle"
    public const val RECENT_AUTH_VALIDITY_OPEN_INPUT: String = "EditSecuritySettings_RecentAuthValidityOpenInput"
    public const val RECENT_AUTH_VALIDITY_MANAGEMENT_INPUT: String =
        "EditSecuritySettings_RecentAuthValidityManagementInput"
    public const val MFA_TOKEN_EXPIRATION_INPUT: String = "EditSecuritySettings_MfaTokenExpirationInput"
    public const val REFRESH_TOKEN_ROTATION_GRACE_PERIOD_INPUT: String =
        "EditSecuritySettings_RefreshTokenRotationGracePeriodInput"

    public const val SECTION_RATE_LIMITING_TITLE: String = "EditSecuritySettings_SectionRateLimitingTitle"
    public const val RATE_LIMIT_MAX_REQUESTS_INPUT: String = "EditSecuritySettings_RateLimitMaxRequestsInput"
    public const val RATE_LIMIT_WINDOW_INPUT: String = "EditSecuritySettings_RateLimitWindowInput"

    public const val SECTION_PASSWORD_POLICY_TITLE: String = "EditSecuritySettings_SectionPasswordPolicyTitle"
    public const val PASSWORD_MIN_LENGTH_INPUT: String = "EditSecuritySettings_PasswordMinLengthInput"
    public const val PASSWORD_REQUIRE_LETTER_CHECKBOX: String = "EditSecuritySettings_PasswordRequireLetterCheckbox"
    public const val PASSWORD_REQUIRE_UPPERCASE_CHECKBOX: String =
        "EditSecuritySettings_PasswordRequireUppercaseCheckbox"
    public const val PASSWORD_REQUIRE_LOWERCASE_CHECKBOX: String =
        "EditSecuritySettings_PasswordRequireLowercaseCheckbox"
    public const val PASSWORD_REQUIRE_DIGIT_CHECKBOX: String = "EditSecuritySettings_PasswordRequireDigitCheckbox"
    public const val PASSWORD_REQUIRE_SPECIAL_CHAR_CHECKBOX: String =
        "EditSecuritySettings_PasswordRequireSpecialCharCheckbox"

    public const val SAVE_ERROR_TEXT: String = "EditSecuritySettings_SaveErrorText"
    public const val SAVE_BUTTON: String = "EditSecuritySettings_SaveButton"
    public const val RESET_BUTTON: String = "EditSecuritySettings_ResetButton"
}
