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
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.SettingsDialogChild
import io.github.mudrichenkoevgeny.kmp.feature.user.localization.LocalUserStrings

/**
 * Screen for editing security policies and settings.
 *
 * @param component Decompose controller for security settings editing.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun EditSecuritySettingsScreen(component: EditSecuritySettingsComponent) {
    val state by component.state.subscribeAsState()
    val userUi = LocalUserStrings.current.ui

    Scaffold(
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
                    onRecentAuthenticationValidityForOpenUserChanged = component::onRecentAuthenticationValidityForOpenUserChanged,
                    onRecentAuthenticationValidityForManagementUserChanged = component::onRecentAuthenticationValidityForManagementUserChanged,
                    onMfaTokenExpirationSecondsChanged = component::onMfaTokenExpirationSecondsChanged,
                    onRefreshTokenRotationGracePeriodSecondsChanged = component::onRefreshTokenRotationGracePeriodSecondsChanged,
                    onMaxRequestsPerPeriodChanged = component::onMaxRequestsPerPeriodChanged,
                    onRateLimitPeriodSecondsChanged = component::onRateLimitPeriodSecondsChanged,
                    onPasswordMinLengthChanged = component::onPasswordMinLengthChanged,
                    onPasswordRequireLetterToggled = component::onPasswordRequireLetterToggled,
                    onPasswordRequireUppercaseToggled = component::onPasswordRequireUpperCaseToggled,
                    onPasswordRequireLowercaseToggled = component::onPasswordRequireLowerCaseToggled,
                    onPasswordRequireDigitToggled = component::onPasswordRequireDigitToggled,
                    onPasswordRequireSpecialCharToggled = component::onPasswordRequireSpecialCharToggled,
                    onCommonPasswordsChanged = component::onCommonPasswordsChanged,
                    onMaxFailedPasswordAttemptsChanged = component::onAccountLockoutMaxFailedPasswordAttemptsChanged,
                    onMaxFailedOtpAttemptsChanged = component::onAccountLockoutMaxFailedOtpAttemptsChanged,
                    onMaxFailedTotpAttemptsChanged = component::onAccountLockoutMaxFailedTotpAttemptsChanged,
                    onFailedAttemptsWindowSecondsChanged = component::onAccountLockoutFailedAttemptsWindowSecondsChanged,
                    onLockoutDurationSecondsChanged = component::onAccountLockoutDurationSecondsChanged,
                    onIndefiniteLockoutThresholdChanged = component::onAccountLockoutIndefiniteLockoutThresholdChanged,
                    onSelfServiceUnlockEnabledToggled = component::onAccountLockoutIsSelfServiceUnlockEnabledToggled,
                    onAccountLockoutCheckIntervalSecondsChanged = component::onAccountLockoutCheckIntervalSecondsChanged,
                    onOpenIpBlacklistEnabledToggled = component::onOpenIpBlacklistEnabledToggled,
                    onOpenIpBlacklistChanged = component::onOpenIpBlacklistChanged,
                    onOpenIpWhitelistEnabledToggled = component::onOpenIpWhitelistEnabledToggled,
                    onOpenIpWhitelistChanged = component::onOpenIpWhitelistChanged,
                    onManagementIpBlacklistEnabledToggled = component::onManagementIpBlacklistEnabledToggled,
                    onManagementIpBlacklistChanged = component::onManagementIpBlacklistChanged,
                    onManagementIpWhitelistEnabledToggled = component::onManagementIpWhitelistEnabledToggled,
                    onManagementIpWhitelistChanged = component::onManagementIpWhitelistChanged,
                    onOtpRetryAfterSecondsChanged = component::onOtpRetryAfterSecondsChanged,
                    onOtpExpirationSecondsChanged = component::onOtpExpirationSecondsChanged,
                    onOtpCodeLengthChanged = component::onOtpNumberOfSymbolsChanged,
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
    onRecentAuthenticationValidityForOpenUserChanged: (String) -> Unit,
    onRecentAuthenticationValidityForManagementUserChanged: (String) -> Unit,
    onMfaTokenExpirationSecondsChanged: (String) -> Unit,
    onRefreshTokenRotationGracePeriodSecondsChanged: (String) -> Unit,
    onMaxRequestsPerPeriodChanged: (String) -> Unit,
    onRateLimitPeriodSecondsChanged: (String) -> Unit,
    onPasswordMinLengthChanged: (String) -> Unit,
    onPasswordRequireLetterToggled: (Boolean) -> Unit,
    onPasswordRequireUppercaseToggled: (Boolean) -> Unit,
    onPasswordRequireLowercaseToggled: (Boolean) -> Unit,
    onPasswordRequireDigitToggled: (Boolean) -> Unit,
    onPasswordRequireSpecialCharToggled: (Boolean) -> Unit,
    onCommonPasswordsChanged: (String) -> Unit,
    onMaxFailedPasswordAttemptsChanged: (String) -> Unit,
    onMaxFailedOtpAttemptsChanged: (String) -> Unit,
    onMaxFailedTotpAttemptsChanged: (String) -> Unit,
    onFailedAttemptsWindowSecondsChanged: (String) -> Unit,
    onLockoutDurationSecondsChanged: (String) -> Unit,
    onIndefiniteLockoutThresholdChanged: (String) -> Unit,
    onSelfServiceUnlockEnabledToggled: (Boolean) -> Unit,
    onAccountLockoutCheckIntervalSecondsChanged: (String) -> Unit,
    onOpenIpBlacklistEnabledToggled: (Boolean) -> Unit,
    onOpenIpBlacklistChanged: (String) -> Unit,
    onOpenIpWhitelistEnabledToggled: (Boolean) -> Unit,
    onOpenIpWhitelistChanged: (String) -> Unit,
    onManagementIpBlacklistEnabledToggled: (Boolean) -> Unit,
    onManagementIpBlacklistChanged: (String) -> Unit,
    onManagementIpWhitelistEnabledToggled: (Boolean) -> Unit,
    onManagementIpWhitelistChanged: (String) -> Unit,
    onOtpRetryAfterSecondsChanged: (String) -> Unit,
    onOtpExpirationSecondsChanged: (String) -> Unit,
    onOtpCodeLengthChanged: (String) -> Unit,
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
            onRecentAuthenticationValidityForOpenUserChanged = onRecentAuthenticationValidityForOpenUserChanged,
            onRecentAuthenticationValidityForManagementUserChanged = onRecentAuthenticationValidityForManagementUserChanged,
            onMfaTokenExpirationSecondsChanged = onMfaTokenExpirationSecondsChanged,
            onRefreshTokenRotationGracePeriodSecondsChanged = onRefreshTokenRotationGracePeriodSecondsChanged
        )

        RateLimitingSection(
            state = state,
            onMaxRequestsPerPeriodChanged = onMaxRequestsPerPeriodChanged,
            onRateLimitPeriodSecondsChanged = onRateLimitPeriodSecondsChanged
        )

        PasswordPolicySection(
            state = state,
            onPasswordMinLengthChanged = onPasswordMinLengthChanged,
            onPasswordRequireLetterToggled = onPasswordRequireLetterToggled,
            onPasswordRequireUppercaseToggled = onPasswordRequireUppercaseToggled,
            onPasswordRequireLowercaseToggled = onPasswordRequireLowercaseToggled,
            onPasswordRequireDigitToggled = onPasswordRequireDigitToggled,
            onPasswordRequireSpecialCharToggled = onPasswordRequireSpecialCharToggled,
            onCommonPasswordsChanged = onCommonPasswordsChanged
        )

        AccountLockoutPolicySection(
            state = state,
            onMaxFailedPasswordAttemptsChanged = onMaxFailedPasswordAttemptsChanged,
            onMaxFailedOtpAttemptsChanged = onMaxFailedOtpAttemptsChanged,
            onMaxFailedTotpAttemptsChanged = onMaxFailedTotpAttemptsChanged,
            onFailedAttemptsWindowSecondsChanged = onFailedAttemptsWindowSecondsChanged,
            onLockoutDurationSecondsChanged = onLockoutDurationSecondsChanged,
            onIndefiniteLockoutThresholdChanged = onIndefiniteLockoutThresholdChanged,
            onSelfServiceUnlockEnabledToggled = onSelfServiceUnlockEnabledToggled,
            onAccountLockoutCheckIntervalSecondsChanged = onAccountLockoutCheckIntervalSecondsChanged
        )

        IpRestrictionPolicySection(
            state = state,
            onOpenIpBlacklistEnabledToggled = onOpenIpBlacklistEnabledToggled,
            onOpenIpBlacklistChanged = onOpenIpBlacklistChanged,
            onOpenIpWhitelistEnabledToggled = onOpenIpWhitelistEnabledToggled,
            onOpenIpWhitelistChanged = onOpenIpWhitelistChanged,
            onManagementIpBlacklistEnabledToggled = onManagementIpBlacklistEnabledToggled,
            onManagementIpBlacklistChanged = onManagementIpBlacklistChanged,
            onManagementIpWhitelistEnabledToggled = onManagementIpWhitelistEnabledToggled,
            onManagementIpWhitelistChanged = onManagementIpWhitelistChanged
        )

        OtpConfirmationSection(
            state = state,
            onOtpRetryAfterSecondsChanged = onOtpRetryAfterSecondsChanged,
            onOtpExpirationSecondsChanged = onOtpExpirationSecondsChanged,
            onOtpCodeLengthChanged = onOtpCodeLengthChanged
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
    onRecentAuthenticationValidityForOpenUserChanged: (String) -> Unit,
    onRecentAuthenticationValidityForManagementUserChanged: (String) -> Unit,
    onMfaTokenExpirationSecondsChanged: (String) -> Unit,
    onRefreshTokenRotationGracePeriodSecondsChanged: (String) -> Unit
) {
    val userUi = LocalUserStrings.current.ui

    CoreTitleText(
        text = userUi.generalSecurity,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.testTag(EditSecuritySettingsTestTags.SECTION_GENERAL_TITLE)
    )

    CoreOutlinedTextField(
        value = state.recentAuthenticationValiditySecondsForOpenUser,
        onValueChange = onRecentAuthenticationValidityForOpenUserChanged,
        label = { CoreBodyText(userUi.recentAuthenticationValiditySeconds) },
        placeholder = { CoreBodyText(userUi.recentAuthenticationValiditySeconds) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.testTag(EditSecuritySettingsTestTags.RECENT_AUTH_VALIDITY_OPEN_INPUT)
    )

    CoreOutlinedTextField(
        value = state.recentAuthenticationValiditySecondsForManagementUser,
        onValueChange = onRecentAuthenticationValidityForManagementUserChanged,
        label = { CoreBodyText(userUi.recentAuthenticationValidityForManagement) },
        placeholder = { CoreBodyText(userUi.recentAuthenticationValidityForManagement) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.testTag(EditSecuritySettingsTestTags.RECENT_AUTH_VALIDITY_MANAGEMENT_INPUT)
    )

    CoreOutlinedTextField(
        value = state.mfaTokenExpirationSeconds,
        onValueChange = onMfaTokenExpirationSecondsChanged,
        label = { CoreBodyText(userUi.mfaTokenExpirationSeconds) },
        placeholder = { CoreBodyText(userUi.mfaTokenExpirationSeconds) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.testTag(EditSecuritySettingsTestTags.MFA_TOKEN_EXPIRATION_INPUT)
    )

    CoreOutlinedTextField(
        value = state.refreshTokenRotationGracePeriodSeconds,
        onValueChange = onRefreshTokenRotationGracePeriodSecondsChanged,
        label = { CoreBodyText(userUi.refreshTokenRotationGracePeriodSeconds) },
        placeholder = { CoreBodyText(userUi.refreshTokenRotationGracePeriodSeconds) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.testTag(EditSecuritySettingsTestTags.REFRESH_TOKEN_ROTATION_GRACE_PERIOD_INPUT)
    )
}

@Composable
private fun RateLimitingSection(
    state: EditSecuritySettingsScreenState.Content,
    onMaxRequestsPerPeriodChanged: (String) -> Unit,
    onRateLimitPeriodSecondsChanged: (String) -> Unit
) {
    val userUi = LocalUserStrings.current.ui

    CoreTitleText(
        text = userUi.rateLimiting,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.testTag(EditSecuritySettingsTestTags.SECTION_RATE_LIMITING_TITLE)
    )

    CoreOutlinedTextField(
        value = state.maxRequestsPerPeriod,
        onValueChange = onMaxRequestsPerPeriodChanged,
        label = { CoreBodyText(userUi.maxRequestsPerPeriod) },
        placeholder = { CoreBodyText(userUi.maxRequestsPerPeriod) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.testTag(EditSecuritySettingsTestTags.RATE_LIMIT_MAX_REQUESTS_INPUT)
    )

    CoreOutlinedTextField(
        value = state.rateLimitPeriodSeconds,
        onValueChange = onRateLimitPeriodSecondsChanged,
        label = { CoreBodyText(userUi.rateLimitPeriodSeconds) },
        placeholder = { CoreBodyText(userUi.rateLimitPeriodSeconds) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.testTag(EditSecuritySettingsTestTags.RATE_LIMIT_WINDOW_INPUT)
    )
}

@Composable
private fun PasswordPolicySection(
    state: EditSecuritySettingsScreenState.Content,
    onPasswordMinLengthChanged: (String) -> Unit,
    onPasswordRequireLetterToggled: (Boolean) -> Unit,
    onPasswordRequireUppercaseToggled: (Boolean) -> Unit,
    onPasswordRequireLowercaseToggled: (Boolean) -> Unit,
    onPasswordRequireDigitToggled: (Boolean) -> Unit,
    onPasswordRequireSpecialCharToggled: (Boolean) -> Unit,
    onCommonPasswordsChanged: (String) -> Unit
) {
    val userUi = LocalUserStrings.current.ui

    CoreTitleText(
        text = userUi.passwordPolicy,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.testTag(EditSecuritySettingsTestTags.SECTION_PASSWORD_POLICY_TITLE)
    )

    CoreOutlinedTextField(
        value = state.passwordMinLength,
        onValueChange = onPasswordMinLengthChanged,
        label = { CoreBodyText(userUi.passwordMinLength) },
        placeholder = { CoreBodyText(userUi.passwordMinLength) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.testTag(EditSecuritySettingsTestTags.PASSWORD_MIN_LENGTH_INPUT)
    )

    ToggleRow(
        label = userUi.passwordRequireLetter,
        checked = state.passwordRequireLetter,
        onCheckedChange = onPasswordRequireLetterToggled,
        testTag = EditSecuritySettingsTestTags.PASSWORD_REQUIRE_LETTER_CHECKBOX
    )

    ToggleRow(
        label = userUi.passwordRequireUppercase,
        checked = state.passwordRequireUpperCase,
        onCheckedChange = onPasswordRequireUppercaseToggled,
        testTag = EditSecuritySettingsTestTags.PASSWORD_REQUIRE_UPPERCASE_CHECKBOX
    )

    ToggleRow(
        label = userUi.passwordRequireLowercase,
        checked = state.passwordRequireLowerCase,
        onCheckedChange = onPasswordRequireLowercaseToggled,
        testTag = EditSecuritySettingsTestTags.PASSWORD_REQUIRE_LOWERCASE_CHECKBOX
    )

    ToggleRow(
        label = userUi.passwordRequireDigit,
        checked = state.passwordRequireDigit,
        onCheckedChange = onPasswordRequireDigitToggled,
        testTag = EditSecuritySettingsTestTags.PASSWORD_REQUIRE_DIGIT_CHECKBOX
    )

    ToggleRow(
        label = userUi.passwordRequireSpecialChar,
        checked = state.passwordRequireSpecialChar,
        onCheckedChange = onPasswordRequireSpecialCharToggled,
        testTag = EditSecuritySettingsTestTags.PASSWORD_REQUIRE_SPECIAL_CHAR_CHECKBOX
    )

    CoreOutlinedTextField(
        value = state.commonPasswords,
        onValueChange = onCommonPasswordsChanged,
        label = { CoreBodyText(userUi.commonPasswordsPlaceholder) },
        placeholder = { CoreBodyText(userUi.commonPasswordsPlaceholder) }
    )
}

@Composable
private fun AccountLockoutPolicySection(
    state: EditSecuritySettingsScreenState.Content,
    onMaxFailedPasswordAttemptsChanged: (String) -> Unit,
    onMaxFailedOtpAttemptsChanged: (String) -> Unit,
    onMaxFailedTotpAttemptsChanged: (String) -> Unit,
    onFailedAttemptsWindowSecondsChanged: (String) -> Unit,
    onLockoutDurationSecondsChanged: (String) -> Unit,
    onIndefiniteLockoutThresholdChanged: (String) -> Unit,
    onSelfServiceUnlockEnabledToggled: (Boolean) -> Unit,
    onAccountLockoutCheckIntervalSecondsChanged: (String) -> Unit
) {
    val userUi = LocalUserStrings.current.ui

    CoreTitleText(
        text = userUi.accountLockoutPolicySection,
        style = MaterialTheme.typography.titleMedium
    )

    CoreOutlinedTextField(
        value = state.accountLockoutMaxFailedPasswordAttempts,
        onValueChange = onMaxFailedPasswordAttemptsChanged,
        label = { CoreBodyText(userUi.maxFailedPasswordAttempts) },
        placeholder = { CoreBodyText(userUi.maxFailedPasswordAttempts) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )

    CoreOutlinedTextField(
        value = state.accountLockoutMaxFailedOtpAttempts,
        onValueChange = onMaxFailedOtpAttemptsChanged,
        label = { CoreBodyText(userUi.maxFailedOtpAttempts) },
        placeholder = { CoreBodyText(userUi.maxFailedOtpAttempts) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )

    CoreOutlinedTextField(
        value = state.accountLockoutMaxFailedTotpAttempts,
        onValueChange = onMaxFailedTotpAttemptsChanged,
        label = { CoreBodyText(userUi.maxFailedTotpAttempts) },
        placeholder = { CoreBodyText(userUi.maxFailedTotpAttempts) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )

    CoreOutlinedTextField(
        value = state.accountLockoutFailedAttemptsWindowSeconds,
        onValueChange = onFailedAttemptsWindowSecondsChanged,
        label = { CoreBodyText(userUi.failedAttemptsWindowSeconds) },
        placeholder = { CoreBodyText(userUi.failedAttemptsWindowSeconds) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )

    CoreOutlinedTextField(
        value = state.accountLockoutDurationSeconds,
        onValueChange = onLockoutDurationSecondsChanged,
        label = { CoreBodyText(userUi.lockoutDurationSeconds) },
        placeholder = { CoreBodyText(userUi.lockoutDurationSeconds) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )

    CoreOutlinedTextField(
        value = state.accountLockoutIndefiniteLockoutThreshold,
        onValueChange = onIndefiniteLockoutThresholdChanged,
        label = { CoreBodyText(userUi.indefiniteLockoutThreshold) },
        placeholder = { CoreBodyText(userUi.indefiniteLockoutThreshold) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )

    ToggleRow(
        label = userUi.selfServiceUnlockEnabled,
        checked = state.accountLockoutIsSelfServiceUnlockEnabled,
        onCheckedChange = onSelfServiceUnlockEnabledToggled,
        testTag = ""
    )

    CoreOutlinedTextField(
        value = state.accountLockoutCheckIntervalSeconds,
        onValueChange = onAccountLockoutCheckIntervalSecondsChanged,
        label = { CoreBodyText(userUi.accountLockoutCheckIntervalSeconds) },
        placeholder = { CoreBodyText(userUi.accountLockoutCheckIntervalSeconds) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
private fun IpRestrictionPolicySection(
    state: EditSecuritySettingsScreenState.Content,
    onOpenIpBlacklistEnabledToggled: (Boolean) -> Unit,
    onOpenIpBlacklistChanged: (String) -> Unit,
    onOpenIpWhitelistEnabledToggled: (Boolean) -> Unit,
    onOpenIpWhitelistChanged: (String) -> Unit,
    onManagementIpBlacklistEnabledToggled: (Boolean) -> Unit,
    onManagementIpBlacklistChanged: (String) -> Unit,
    onManagementIpWhitelistEnabledToggled: (Boolean) -> Unit,
    onManagementIpWhitelistChanged: (String) -> Unit
) {
    val userUi = LocalUserStrings.current.ui

    CoreTitleText(
        text = userUi.openIpRestrictionPolicy,
        style = MaterialTheme.typography.titleMedium
    )

    ToggleRow(
        label = userUi.blacklistEnabled,
        checked = state.openIpBlacklistEnabled,
        onCheckedChange = onOpenIpBlacklistEnabledToggled,
        testTag = ""
    )

    CoreOutlinedTextField(
        value = state.openIpBlacklist,
        onValueChange = onOpenIpBlacklistChanged,
        label = { CoreBodyText(userUi.blacklistPlaceholder) },
        placeholder = { CoreBodyText(userUi.blacklistPlaceholder) }
    )

    ToggleRow(
        label = userUi.whitelistEnabled,
        checked = state.openIpWhitelistEnabled,
        onCheckedChange = onOpenIpWhitelistEnabledToggled,
        testTag = ""
    )

    CoreOutlinedTextField(
        value = state.openIpWhitelist,
        onValueChange = onOpenIpWhitelistChanged,
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
        onCheckedChange = onManagementIpBlacklistEnabledToggled,
        testTag = ""
    )

    CoreOutlinedTextField(
        value = state.managementIpBlacklist,
        onValueChange = onManagementIpBlacklistChanged,
        label = { CoreBodyText(userUi.blacklistPlaceholder) },
        placeholder = { CoreBodyText(userUi.blacklistPlaceholder) }
    )

    ToggleRow(
        label = userUi.whitelistEnabled,
        checked = state.managementIpWhitelistEnabled,
        onCheckedChange = onManagementIpWhitelistEnabledToggled,
        testTag = ""
    )

    CoreOutlinedTextField(
        value = state.managementIpWhitelist,
        onValueChange = onManagementIpWhitelistChanged,
        label = { CoreBodyText(userUi.whitelistPlaceholder) },
        placeholder = { CoreBodyText(userUi.whitelistPlaceholder) }
    )
}

@Composable
private fun OtpConfirmationSection(
    state: EditSecuritySettingsScreenState.Content,
    onOtpRetryAfterSecondsChanged: (String) -> Unit,
    onOtpExpirationSecondsChanged: (String) -> Unit,
    onOtpCodeLengthChanged: (String) -> Unit
) {
    val userUi = LocalUserStrings.current.ui

    CoreTitleText(
        text = userUi.otpConfirmation,
        style = MaterialTheme.typography.titleMedium
    )

    CoreOutlinedTextField(
        value = state.otpRetryAfterSeconds,
        onValueChange = onOtpRetryAfterSecondsChanged,
        label = { CoreBodyText(userUi.otpRetryAfterSeconds) },
        placeholder = { CoreBodyText(userUi.otpRetryAfterSeconds) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )

    CoreOutlinedTextField(
        value = state.otpExpirationSeconds,
        onValueChange = onOtpExpirationSecondsChanged,
        label = { CoreBodyText(userUi.otpExpirationSeconds) },
        placeholder = { CoreBodyText(userUi.otpExpirationSeconds) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )

    CoreOutlinedTextField(
        value = state.otpNumberOfSymbols,
        onValueChange = onOtpCodeLengthChanged,
        label = { CoreBodyText(userUi.otpNumberOfSymbols) },
        placeholder = { CoreBodyText(userUi.otpNumberOfSymbols) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
private fun ToggleRow(
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
    testTag: String
) {
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
