package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.global

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
 * Screen for editing global settings.
 *
 * @param component Decompose controller for global settings editing.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGlobalSettingsScreen(component: EditGlobalSettingsComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.edit_global_settings_title),
                        modifier = Modifier.testTag(EditGlobalSettingsTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(EditGlobalSettingsTestTags.BACK_BUTTON)
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
            is EditGlobalSettingsScreenState.Loading -> {
                FullscreenLoading(modifier = Modifier.padding(padding))
            }
            is EditGlobalSettingsScreenState.Error -> {
                FullscreenError(
                    error = currentState.error,
                    onRetry = component::onRetry,
                    modifier = Modifier
                        .padding(padding)
                        .testTag(EditGlobalSettingsTestTags.GLOBAL_ERROR)
                )
            }
            is EditGlobalSettingsScreenState.Content -> {
                EditGlobalSettingsForm(
                    state = currentState,
                    onPrivacyPolicyUrlChanged = component::onPrivacyPolicyUrlChanged,
                    onTermsOfServiceUrlChanged = component::onTermsOfServiceUrlChanged,
                    onContactSupportEmailChanged = component::onContactSupportEmailChanged,
                    onMinVersionAndroidChanged = component::onMinVersionAndroidChanged,
                    onMinVersionIosChanged = component::onMinVersionIosChanged,
                    onMinVersionWebChanged = component::onMinVersionWebChanged,
                    onMinVersionDesktopChanged = component::onMinVersionDesktopChanged,
                    onTracingEnabledToggled = component::onTracingEnabledToggled,
                    onMetricsEnabledToggled = component::onMetricsEnabledToggled,
                    onVerboseLoggingEnabledToggled = component::onVerboseLoggingEnabledToggled,
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
private fun EditGlobalSettingsForm(
    state: EditGlobalSettingsScreenState.Content,
    onPrivacyPolicyUrlChanged: (String) -> Unit,
    onTermsOfServiceUrlChanged: (String) -> Unit,
    onContactSupportEmailChanged: (String) -> Unit,
    onMinVersionAndroidChanged: (String) -> Unit,
    onMinVersionIosChanged: (String) -> Unit,
    onMinVersionWebChanged: (String) -> Unit,
    onMinVersionDesktopChanged: (String) -> Unit,
    onTracingEnabledToggled: (Boolean) -> Unit,
    onMetricsEnabledToggled: (Boolean) -> Unit,
    onVerboseLoggingEnabledToggled: (Boolean) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.paddingMedium)
    ) {
        OutlinedTextField(
            value = state.privacyPolicyUrl,
            onValueChange = onPrivacyPolicyUrlChanged,
            label = { Text(text = stringResource(Res.string.privacy_policy_url)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditGlobalSettingsTestTags.PRIVACY_POLICY_URL_INPUT)
        )

        OutlinedTextField(
            value = state.termsOfServiceUrl,
            onValueChange = onTermsOfServiceUrlChanged,
            label = { Text(text = stringResource(Res.string.terms_of_service_url)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditGlobalSettingsTestTags.TERMS_OF_SERVICE_URL_INPUT)
        )

        OutlinedTextField(
            value = state.contactSupportEmail,
            onValueChange = onContactSupportEmailChanged,
            label = { Text(text = stringResource(Res.string.contact_support_email)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditGlobalSettingsTestTags.CONTACT_SUPPORT_EMAIL_INPUT)
        )

        Text(
            text = stringResource(Res.string.telemetry_and_logging),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.testTag(EditGlobalSettingsTestTags.SECTION_TELEMETRY_TITLE)
        )

        ToggleRow(
            label = stringResource(Res.string.is_tracing_enabled),
            checked = state.isTracingEnabled,
            onCheckedChange = onTracingEnabledToggled,
            testTag = EditGlobalSettingsTestTags.IS_TRACING_ENABLED_CHECKBOX
        )

        ToggleRow(
            label = stringResource(Res.string.is_metrics_enabled),
            checked = state.isMetricsEnabled,
            onCheckedChange = onMetricsEnabledToggled,
            testTag = EditGlobalSettingsTestTags.IS_METRICS_ENABLED_CHECKBOX
        )

        ToggleRow(
            label = stringResource(Res.string.is_verbose_logging_enabled),
            checked = state.isVerboseLoggingEnabled,
            onCheckedChange = onVerboseLoggingEnabledToggled,
            testTag = EditGlobalSettingsTestTags.IS_VERBOSE_LOGGING_ENABLED_CHECKBOX
        )

        Text(
            text = stringResource(Res.string.min_supported_app_versions),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.testTag(EditGlobalSettingsTestTags.SECTION_MIN_VERSIONS_TITLE)
        )

        OutlinedTextField(
            value = state.minVersionAndroid,
            onValueChange = onMinVersionAndroidChanged,
            label = { Text(text = stringResource(Res.string.min_version_android)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditGlobalSettingsTestTags.MIN_VERSION_ANDROID_INPUT)
        )

        OutlinedTextField(
            value = state.minVersionIos,
            onValueChange = onMinVersionIosChanged,
            label = { Text(text = stringResource(Res.string.min_version_ios)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditGlobalSettingsTestTags.MIN_VERSION_IOS_INPUT)
        )

        OutlinedTextField(
            value = state.minVersionWeb,
            onValueChange = onMinVersionWebChanged,
            label = { Text(text = stringResource(Res.string.min_version_web)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditGlobalSettingsTestTags.MIN_VERSION_WEB_INPUT)
        )

        OutlinedTextField(
            value = state.minVersionDesktop,
            onValueChange = onMinVersionDesktopChanged,
            label = { Text(text = stringResource(Res.string.min_version_desktop)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditGlobalSettingsTestTags.MIN_VERSION_DESKTOP_INPUT)
        )

        ErrorText(
            error = state.saveError,
            testTag = EditGlobalSettingsTestTags.SAVE_ERROR_TEXT
        )

        Button(
            onClick = onSaveClick,
            enabled = !state.isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditGlobalSettingsTestTags.SAVE_BUTTON)
        ) {
            Text(text = stringResource(if (state.isSaving) Res.string.saving else Res.string.save))
        }
    }
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
private fun EditGlobalSettingsContentPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
            Surface {
                EditGlobalSettingsForm(
                    state = EditGlobalSettingsScreenState.Content(
                        privacyPolicyUrl = "https://example.com/privacy",
                        termsOfServiceUrl = "https://example.com/terms",
                        contactSupportEmail = "support@example.com",
                        minVersionAndroid = "1.0.0",
                        minVersionIos = "1.0.0",
                        minVersionWeb = "1.0.0",
                        minVersionDesktop = "1.0.0",
                        isTracingEnabled = true,
                        isMetricsEnabled = true,
                        isVerboseLoggingEnabled = false
                    ),
                    onPrivacyPolicyUrlChanged = {},
                    onTermsOfServiceUrlChanged = {},
                    onContactSupportEmailChanged = {},
                    onMinVersionAndroidChanged = {},
                    onMinVersionIosChanged = {},
                    onMinVersionWebChanged = {},
                    onMinVersionDesktopChanged = {},
                    onTracingEnabledToggled = {},
                    onMetricsEnabledToggled = {},
                    onVerboseLoggingEnabledToggled = {},
                    onSaveClick = {}
                )
            }
        }
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun EditGlobalSettingsErrorPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
            Surface {
                EditGlobalSettingsForm(
                    state = EditGlobalSettingsScreenState.Content(
                        privacyPolicyUrl = "https://example.com/privacy",
                        termsOfServiceUrl = "https://example.com/terms",
                        contactSupportEmail = "support@example.com",
                        minVersionAndroid = "1.0.0",
                        minVersionIos = "1.0.0",
                        minVersionWeb = "1.0.0",
                        minVersionDesktop = "1.0.0",
                        isTracingEnabled = true,
                        isMetricsEnabled = true,
                        isVerboseLoggingEnabled = false,
                        saveError = CommonError.Unknown()
                    ),
                    onPrivacyPolicyUrlChanged = {},
                    onTermsOfServiceUrlChanged = {},
                    onContactSupportEmailChanged = {},
                    onMinVersionAndroidChanged = {},
                    onMinVersionIosChanged = {},
                    onMinVersionWebChanged = {},
                    onMinVersionDesktopChanged = {},
                    onTracingEnabledToggled = {},
                    onMetricsEnabledToggled = {},
                    onVerboseLoggingEnabledToggled = {},
                    onSaveClick = {}
                )
            }
        }
    }
}

object EditGlobalSettingsTestTags {
    const val TITLE = "EditGlobalSettings_Title"
    const val BACK_BUTTON = "EditGlobalSettings_BackButton"
    const val GLOBAL_ERROR = "EditGlobalSettings_GlobalError"

    const val PRIVACY_POLICY_URL_INPUT = "EditGlobalSettings_PrivacyPolicyUrlInput"
    const val TERMS_OF_SERVICE_URL_INPUT = "EditGlobalSettings_TermsOfServiceUrlInput"
    const val CONTACT_SUPPORT_EMAIL_INPUT = "EditGlobalSettings_ContactSupportEmailInput"

    const val SECTION_TELEMETRY_TITLE = "EditGlobalSettings_SectionTelemetryTitle"
    const val IS_TRACING_ENABLED_CHECKBOX = "EditGlobalSettings_IsTracingEnabledCheckbox"
    const val IS_METRICS_ENABLED_CHECKBOX = "EditGlobalSettings_IsMetricsEnabledCheckbox"
    const val IS_VERBOSE_LOGGING_ENABLED_CHECKBOX = "EditGlobalSettings_IsVerboseLoggingEnabledCheckbox"

    const val SECTION_MIN_VERSIONS_TITLE = "EditGlobalSettings_SectionMinVersionsTitle"
    const val MIN_VERSION_ANDROID_INPUT = "EditGlobalSettings_MinVersionAndroidInput"
    const val MIN_VERSION_IOS_INPUT = "EditGlobalSettings_MinVersionIosInput"
    const val MIN_VERSION_WEB_INPUT = "EditGlobalSettings_MinVersionWebInput"
    const val MIN_VERSION_DESKTOP_INPUT = "EditGlobalSettings_MinVersionDesktopInput"

    const val SAVE_ERROR_TEXT = "EditGlobalSettings_SaveErrorText"
    const val SAVE_BUTTON = "EditGlobalSettings_SaveButton"
}
