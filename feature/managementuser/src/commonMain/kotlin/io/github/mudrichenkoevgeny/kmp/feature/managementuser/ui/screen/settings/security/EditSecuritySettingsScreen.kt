package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.security

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.error.FullscreenError
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.Res
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.*
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
                    Text(
                        text = stringResource(Res.string.edit_security_settings_title),
                        modifier = Modifier.testTag(EditSecuritySettingsTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(EditSecuritySettingsTestTags.BACK_BUTTON)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.back)
                        )
                    }
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
                    onRecentAuthenticationValiditySecondsChanged = component::onRecentAuthenticationValiditySecondsChanged,
                    onRecentAuthenticationValidityForManagementChanged = component::onRecentAuthenticationValidityForManagementChanged,
                    onMfaTokenExpirationSecondsChanged = component::onMfaTokenExpirationSecondsChanged,
                    onPasswordMinLengthChanged = component::onPasswordMinLengthChanged,
                    onPasswordRequireLetterToggled = component::onPasswordRequireLetterToggled,
                    onPasswordRequireUpperCaseToggled = component::onPasswordRequireUpperCaseToggled,
                    onPasswordRequireLowerCaseToggled = component::onPasswordRequireLowerCaseToggled,
                    onPasswordRequireDigitToggled = component::onPasswordRequireDigitToggled,
                    onPasswordRequireSpecialCharToggled = component::onPasswordRequireSpecialCharToggled,
                    onOtpRetryAfterSecondsChanged = component::onOtpRetryAfterSecondsChanged,
                    onOtpNumberOfSymbolsChanged = component::onOtpNumberOfSymbolsChanged,
                    onOtpExpirationSecondsChanged = component::onOtpExpirationSecondsChanged,
                    onMaxRequestsPerPeriodChanged = component::onMaxRequestsPerPeriodChanged,
                    onRateLimitPeriodSecondsChanged = component::onRateLimitPeriodSecondsChanged,
                    onSaveClick = component::onSaveClick,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(Dimens.paddingMedium)
                        .verticalScroll(rememberScrollState())
                )
            }
        }
    }
}

@Composable
private fun EditSecuritySettingsForm(
    state: EditSecuritySettingsScreenState.Content,
    onRecentAuthenticationValiditySecondsChanged: (String) -> Unit,
    onRecentAuthenticationValidityForManagementChanged: (String) -> Unit,
    onMfaTokenExpirationSecondsChanged: (String) -> Unit,
    onPasswordMinLengthChanged: (String) -> Unit,
    onPasswordRequireLetterToggled: (Boolean) -> Unit,
    onPasswordRequireUpperCaseToggled: (Boolean) -> Unit,
    onPasswordRequireLowerCaseToggled: (Boolean) -> Unit,
    onPasswordRequireDigitToggled: (Boolean) -> Unit,
    onPasswordRequireSpecialCharToggled: (Boolean) -> Unit,
    onOtpRetryAfterSecondsChanged: (String) -> Unit,
    onOtpNumberOfSymbolsChanged: (String) -> Unit,
    onOtpExpirationSecondsChanged: (String) -> Unit,
    onMaxRequestsPerPeriodChanged: (String) -> Unit,
    onRateLimitPeriodSecondsChanged: (String) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.paddingMedium)
    ) {
        Text(
            text = stringResource(Res.string.general_security),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.SECTION_GENERAL_TITLE)
        )

        OutlinedTextField(
            value = state.recentAuthenticationValiditySeconds,
            onValueChange = onRecentAuthenticationValiditySecondsChanged,
            label = { Text(text = stringResource(Res.string.recent_authentication_validity_seconds)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditSecuritySettingsTestTags.RECENT_AUTH_VALIDITY_INPUT)
        )

        OutlinedTextField(
            value = state.recentAuthenticationValiditySecondsForManagement,
            onValueChange = onRecentAuthenticationValidityForManagementChanged,
            label = { Text(text = stringResource(Res.string.recent_authentication_validity_for_management)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditSecuritySettingsTestTags.RECENT_MANAGEMENT_AUTH_VALIDITY_INPUT)
        )

        OutlinedTextField(
            value = state.mfaTokenExpirationSeconds,
            onValueChange = onMfaTokenExpirationSecondsChanged,
            label = { Text(text = stringResource(Res.string.mfa_token_expiration_seconds)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditSecuritySettingsTestTags.MFA_TOKEN_EXPIRATION_INPUT)
        )

        Text(
            text = stringResource(Res.string.rate_limiting),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.SECTION_RATE_LIMITING_TITLE)
        )

        OutlinedTextField(
            value = state.maxRequestsPerPeriod,
            onValueChange = onMaxRequestsPerPeriodChanged,
            label = { Text(text = stringResource(Res.string.max_requests_per_period)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditSecuritySettingsTestTags.MAX_REQUESTS_PER_PERIOD_INPUT)
        )

        OutlinedTextField(
            value = state.rateLimitPeriodSeconds,
            onValueChange = onRateLimitPeriodSecondsChanged,
            label = { Text(text = stringResource(Res.string.rate_limit_period_seconds)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditSecuritySettingsTestTags.RATE_LIMIT_PERIOD_INPUT)
        )

        Text(
            text = stringResource(Res.string.password_policy),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.SECTION_PASSWORD_POLICY_TITLE)
        )

        OutlinedTextField(
            value = state.passwordMinLength,
            onValueChange = onPasswordMinLengthChanged,
            label = { Text(text = stringResource(Res.string.password_min_length)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditSecuritySettingsTestTags.PASSWORD_MIN_LENGTH_INPUT)
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

        Text(
            text = stringResource(Res.string.otp_confirmation),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.testTag(EditSecuritySettingsTestTags.SECTION_OTP_TITLE)
        )

        OutlinedTextField(
            value = state.otpRetryAfterSeconds,
            onValueChange = onOtpRetryAfterSecondsChanged,
            label = { Text(text = stringResource(Res.string.otp_retry_after_seconds)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditSecuritySettingsTestTags.OTP_RETRY_AFTER_INPUT)
        )

        OutlinedTextField(
            value = state.otpNumberOfSymbols,
            onValueChange = onOtpNumberOfSymbolsChanged,
            label = { Text(text = stringResource(Res.string.otp_number_of_symbols)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditSecuritySettingsTestTags.OTP_NUMBER_OF_SYMBOLS_INPUT)
        )

        OutlinedTextField(
            value = state.otpExpirationSeconds,
            onValueChange = onOtpExpirationSecondsChanged,
            label = { Text(text = stringResource(Res.string.otp_expiration_seconds)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditSecuritySettingsTestTags.OTP_EXPIRATION_INPUT)
        )

        ErrorText(
            error = state.saveError,
            testTag = EditSecuritySettingsTestTags.SAVE_ERROR_TEXT
        )

        Button(
            onClick = onSaveClick,
            enabled = !state.isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditSecuritySettingsTestTags.SAVE_BUTTON)
        ) {
            Text(text = stringResource(if (state.isSaving) Res.string.saving else Res.string.save))
        }
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
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = Dimens.paddingSmall)
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
            Text(
                text = it.toLocalizedMessage(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = Dimens.paddingSmall)
                    .testTag(testTag)
            )
        }
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun EditSecuritySettingsContentPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
            Surface {
                EditSecuritySettingsForm(
                    state = EditSecuritySettingsScreenState.Content(
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
                    ),
                    onRecentAuthenticationValiditySecondsChanged = {},
                    onRecentAuthenticationValidityForManagementChanged = {},
                    onMfaTokenExpirationSecondsChanged = {},
                    onPasswordMinLengthChanged = {},
                    onPasswordRequireLetterToggled = {},
                    onPasswordRequireUpperCaseToggled = {},
                    onPasswordRequireLowerCaseToggled = {},
                    onPasswordRequireDigitToggled = {},
                    onPasswordRequireSpecialCharToggled = {},
                    onOtpRetryAfterSecondsChanged = {},
                    onOtpNumberOfSymbolsChanged = {},
                    onOtpExpirationSecondsChanged = {},
                    onMaxRequestsPerPeriodChanged = {},
                    onRateLimitPeriodSecondsChanged = {},
                    onSaveClick = {}
                )
            }
        }
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun EditSecuritySettingsErrorPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
            Surface {
                EditSecuritySettingsForm(
                    state = EditSecuritySettingsScreenState.Content(
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
                        rateLimitPeriodSeconds = "60",
                        saveError = CommonError.Unknown()
                    ),
                    onRecentAuthenticationValiditySecondsChanged = {},
                    onRecentAuthenticationValidityForManagementChanged = {},
                    onMfaTokenExpirationSecondsChanged = {},
                    onPasswordMinLengthChanged = {},
                    onPasswordRequireLetterToggled = {},
                    onPasswordRequireUpperCaseToggled = {},
                    onPasswordRequireLowerCaseToggled = {},
                    onPasswordRequireDigitToggled = {},
                    onPasswordRequireSpecialCharToggled = {},
                    onOtpRetryAfterSecondsChanged = {},
                    onOtpNumberOfSymbolsChanged = {},
                    onOtpExpirationSecondsChanged = {},
                    onMaxRequestsPerPeriodChanged = {},
                    onRateLimitPeriodSecondsChanged = {},
                    onSaveClick = {}
                )
            }
        }
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

    const val SECTION_OTP_TITLE = "EditSecuritySettings_SectionOtpTitle"
    const val OTP_RETRY_AFTER_INPUT = "EditSecuritySettings_OtpRetryAfterInput"
    const val OTP_NUMBER_OF_SYMBOLS_INPUT = "EditSecuritySettings_OtpNumberOfSymbolsInput"
    const val OTP_EXPIRATION_INPUT = "EditSecuritySettings_OtpExpirationInput"

    const val SAVE_ERROR_TEXT = "EditSecuritySettings_SaveErrorText"
    const val SAVE_BUTTON = "EditSecuritySettings_SaveButton"
}
