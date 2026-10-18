package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.global

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
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.settings.global.EditGlobalSettingsComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.SettingsDialogChild
import io.github.mudrichenkoevgeny.kmp.feature.user.localization.LocalUserStrings

/**
 * Screen for editing global settings.
 *
 * @param component Decompose controller for global settings editing.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGlobalSettingsScreen(component: EditGlobalSettingsComponent) {
    val state by component.state.subscribeAsState()
    val userUi = LocalUserStrings.current.ui

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    CoreScreenTitleText(
                        text = userUi.editGlobalSettingsTitle,
                        modifier = Modifier.testTag(EditGlobalSettingsTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    CoreBackButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(EditGlobalSettingsTestTags.BACK_BUTTON)
                    )
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
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userUi = LocalUserStrings.current.ui

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingMedium)
    ) {
        CoreOutlinedTextField(
            value = state.privacyPolicyUrl,
            onValueChange = onPrivacyPolicyUrlChanged,
            label = { CoreBodyText(userUi.privacyPolicyUrl) },
            placeholder = { CoreBodyText(userUi.privacyPolicyUrl) },
            modifier = Modifier.testTag(EditGlobalSettingsTestTags.PRIVACY_POLICY_URL_INPUT)
        )

        CoreOutlinedTextField(
            value = state.termsOfServiceUrl,
            onValueChange = onTermsOfServiceUrlChanged,
            label = { CoreBodyText(userUi.termsOfServiceUrl) },
            placeholder = { CoreBodyText(userUi.termsOfServiceUrl) },
            modifier = Modifier.testTag(EditGlobalSettingsTestTags.TERMS_OF_SERVICE_URL_INPUT)
        )

        CoreOutlinedTextField(
            value = state.contactSupportEmail,
            onValueChange = onContactSupportEmailChanged,
            label = { CoreBodyText(userUi.contactSupportEmail) },
            placeholder = { CoreBodyText(userUi.contactSupportEmail) },
            modifier = Modifier.testTag(EditGlobalSettingsTestTags.CONTACT_SUPPORT_EMAIL_INPUT)
        )

        CoreTitleText(
            text = userUi.telemetryAndLogging,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.testTag(EditGlobalSettingsTestTags.SECTION_TELEMETRY_TITLE)
        )

        ToggleRow(
            label = userUi.isTracingEnabled,
            checked = state.isTracingEnabled,
            onCheckedChange = onTracingEnabledToggled,
            testTag = EditGlobalSettingsTestTags.IS_TRACING_ENABLED_CHECKBOX
        )

        ToggleRow(
            label = userUi.isMetricsEnabled,
            checked = state.isMetricsEnabled,
            onCheckedChange = onMetricsEnabledToggled,
            testTag = EditGlobalSettingsTestTags.IS_METRICS_ENABLED_CHECKBOX
        )

        ToggleRow(
            label = userUi.isVerboseLoggingEnabled,
            checked = state.isVerboseLoggingEnabled,
            onCheckedChange = onVerboseLoggingEnabledToggled,
            testTag = EditGlobalSettingsTestTags.IS_VERBOSE_LOGGING_ENABLED_CHECKBOX
        )

        CoreTitleText(
            text = userUi.minSupportedAppVersions,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.testTag(EditGlobalSettingsTestTags.SECTION_MIN_VERSIONS_TITLE)
        )

        CoreOutlinedTextField(
            value = state.minVersionAndroid,
            onValueChange = onMinVersionAndroidChanged,
            label = { CoreBodyText(userUi.minVersionAndroid) },
            placeholder = { CoreBodyText(userUi.minVersionAndroid) },
            modifier = Modifier.testTag(EditGlobalSettingsTestTags.MIN_VERSION_ANDROID_INPUT)
        )

        CoreOutlinedTextField(
            value = state.minVersionIos,
            onValueChange = onMinVersionIosChanged,
            label = { CoreBodyText(userUi.minVersionIos) },
            placeholder = { CoreBodyText(userUi.minVersionIos) },
            modifier = Modifier.testTag(EditGlobalSettingsTestTags.MIN_VERSION_IOS_INPUT)
        )

        CoreOutlinedTextField(
            value = state.minVersionWeb,
            onValueChange = onMinVersionWebChanged,
            label = { CoreBodyText(userUi.minVersionWeb) },
            placeholder = { CoreBodyText(userUi.minVersionWeb) },
            modifier = Modifier.testTag(EditGlobalSettingsTestTags.MIN_VERSION_WEB_INPUT)
        )

        CoreOutlinedTextField(
            value = state.minVersionDesktop,
            onValueChange = onMinVersionDesktopChanged,
            label = { CoreBodyText(userUi.minVersionDesktop) },
            placeholder = { CoreBodyText(userUi.minVersionDesktop) },
            modifier = Modifier.testTag(EditGlobalSettingsTestTags.MIN_VERSION_DESKTOP_INPUT)
        )

        ErrorText(
            error = state.saveError,
            testTag = EditGlobalSettingsTestTags.SAVE_ERROR_TEXT
        )

        CoreButton(
            text = if (state.isSaving) userUi.saving else userUi.save,
            onClick = onSaveClick,
            enabled = !state.isSaving,
            modifier = Modifier.testTag(EditGlobalSettingsTestTags.SAVE_BUTTON)
        )

        CoreTextButton(
            text = userUi.resetToDefaults,
            onClick = onResetClick,
            enabled = !state.isSaving,
            modifier = Modifier.testTag(EditGlobalSettingsTestTags.RESET_BUTTON)
        )
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
internal class EditGlobalSettingsPreviewProvider : PreviewParameterProvider<EditGlobalSettingsScreenState> {
    private val sampleContent = EditGlobalSettingsScreenState.Content(
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
    )

    private val items: List<Pair<String, EditGlobalSettingsScreenState>> = listOf(
        "Content" to sampleContent,
        "Saving" to sampleContent.copy(isSaving = true),
        "Save Error" to sampleContent.copy(saveError = CommonError.Unknown()),
        "Error" to EditGlobalSettingsScreenState.Error(error = CommonError.Unknown()),
        "Loading" to EditGlobalSettingsScreenState.Loading
    )

    override val values: Sequence<EditGlobalSettingsScreenState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun EditGlobalSettingsScreenPreviewContent(state: EditGlobalSettingsScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        EditGlobalSettingsScreen(
            component = EditGlobalSettingsComponentMock(initialState = state)
        )
    }
}

private val defaultEditGlobalSettingsPreviewState = EditGlobalSettingsScreenState.Content(
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
)

@InternalApi
@ScreenSizePreviews
@Composable
private fun StatesPreview(
    @PreviewParameter(EditGlobalSettingsPreviewProvider::class) state: EditGlobalSettingsScreenState
) {
    ScreenPreviewContainer {
        EditGlobalSettingsScreenPreviewContent(state = state)
    }
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        EditGlobalSettingsScreenPreviewContent(state = defaultEditGlobalSettingsPreviewState)
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        EditGlobalSettingsScreenPreviewContent(state = defaultEditGlobalSettingsPreviewState)
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        EditGlobalSettingsScreenPreviewContent(state = defaultEditGlobalSettingsPreviewState)
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
    const val RESET_BUTTON = "EditGlobalSettings_ResetButton"
}
