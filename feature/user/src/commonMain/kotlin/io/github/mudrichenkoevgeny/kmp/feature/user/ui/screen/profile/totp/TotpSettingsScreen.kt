package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import io.github.mudrichenkoevgeny.kmp.core.common.Res as CommonRes
import io.github.mudrichenkoevgeny.kmp.core.common.*
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreBackButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreTextButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input.CoreCodeTextField
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
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.*
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.totp.TotpSettingsComponentMock
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totprecoverycodes.TotpRecoveryCodes
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totpsetup.TotpSetup
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TotpSettingsScreen(component: TotpSettingsComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    CoreScreenTitleText(
                        text = stringResource(Res.string.totp_settings),
                        modifier = Modifier.testTag(TotpSettingsTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    CoreBackButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(TotpSettingsTestTags.BACK_BUTTON)
                    )
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (val currentState = state) {
                is TotpSettingsScreenState.Loading -> FullscreenLoading()
                is TotpSettingsScreenState.Disabled -> DisabledContent(
                    state = currentState,
                    onSetupClick = component::onSetupClick
                )
                is TotpSettingsScreenState.SetupInProgress -> SetupInProgressContent(
                    state = currentState,
                    onCodeChanged = component::onCodeChanged,
                    onConfirmClick = component::onConfirmSetupClick
                )
                is TotpSettingsScreenState.Enabled -> EnabledContent(
                    state = currentState,
                    onDisableClick = component::onDisableClick,
                    onConfirmDisable = component::onConfirmDisable,
                    onRegenerateClick = component::onRegenerateRecoveryCodesClick,
                    onConfirmRegenerate = component::onConfirmRegenerateRecoveryCodes,
                    onDismissDialogs = component::onDismissDialogs
                )
                is TotpSettingsScreenState.Error -> {
                    CoreErrorText(
                        text = currentState.error.toLocalizedMessage(),
                        modifier = Modifier.testTag(TotpSettingsTestTags.GLOBAL_ERROR_TEXT)
                    )
                }
            }
        }
    }
}

@Composable
private fun DisabledContent(
    state: TotpSettingsScreenState.Disabled,
    onSetupClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(CoreTheme.dimens.paddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CoreBodyText(
            text = stringResource(Res.string.totp_disabled_desc),
            textAlign = TextAlign.Center,
            modifier = Modifier.testTag(TotpSettingsTestTags.DISABLED_DESC_TEXT)
        )
        Spacer(Modifier.height(CoreTheme.dimens.paddingLarge))
        CoreButton(
            text = stringResource(Res.string.setup_totp),
            onClick = onSetupClick,
            modifier = Modifier.testTag(TotpSettingsTestTags.SETUP_TOTP_BUTTON),
            enabled = !state.actionLoading
        )
        ErrorText(
            error = state.actionError,
            testTag = TotpSettingsTestTags.DISABLED_ACTION_ERROR_TEXT
        )
    }
}

@Composable
private fun SetupInProgressContent(
    state: TotpSettingsScreenState.SetupInProgress,
    onCodeChanged: (String) -> Unit,
    onConfirmClick: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(CoreTheme.dimens.paddingLarge)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CoreTitleText(
            text = stringResource(Res.string.totp_setup_step1),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.testTag(TotpSettingsTestTags.STEP1_TITLE)
        )
        CoreBodyText(
            text = stringResource(Res.string.totp_setup_step1_desc),
            textAlign = TextAlign.Center,
            modifier = Modifier.testTag(TotpSettingsTestTags.STEP1_DESC)
        )

        Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))

        Box(
            modifier = Modifier
                .padding(CoreTheme.dimens.paddingMedium)
                .size(CoreTheme.dimens.qrCodeSize)
                .testTag(TotpSettingsTestTags.QR_CODE_BOX),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberQrCodePainter(state.setup.otpAuthUrl),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))

        CoreBodyText(
            text = stringResource(Res.string.totp_manual_key),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.testTag(TotpSettingsTestTags.MANUAL_KEY_LABEL)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CoreTitleText(
                text = state.setup.secretKey,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.testTag(TotpSettingsTestTags.SECRET_KEY_TEXT)
            )
            IconButton(
                onClick = { clipboardManager.setText(AnnotatedString(state.setup.secretKey)) },
                modifier = Modifier.testTag(TotpSettingsTestTags.COPY_SECRET_KEY_BUTTON)
            ) {
                Icon(
                    painter = painterResource(CommonRes.drawable.ic_copy),
                    contentDescription = null,
                    modifier = Modifier.padding(CoreTheme.dimens.paddingExtraSmall)
                )
            }
        }

        Spacer(Modifier.height(CoreTheme.dimens.paddingLarge))

        CoreTitleText(
            text = stringResource(Res.string.totp_setup_step2),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.testTag(TotpSettingsTestTags.STEP2_TITLE)
        )
        CoreBodyText(
            text = stringResource(Res.string.totp_setup_step2_desc),
            textAlign = TextAlign.Center,
            modifier = Modifier.testTag(TotpSettingsTestTags.STEP2_DESC)
        )

        Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))

        CoreCodeTextField(
            value = state.code,
            onValueChange = onCodeChanged,
            label = { CoreBodyText(stringResource(Res.string.totp_code)) },
            placeholder = { CoreBodyText(stringResource(Res.string.totp_code)) },
            enabled = !state.actionLoading,
            isError = state.actionError != null,
            modifier = Modifier.testTag(TotpSettingsTestTags.CODE_INPUT)
        )

        ErrorText(
            error = state.actionError,
            testTag = TotpSettingsTestTags.SETUP_ACTION_ERROR_TEXT
        )

        Spacer(Modifier.height(CoreTheme.dimens.paddingLarge))

        CoreButton(
            text = stringResource(Res.string.confirm),
            onClick = onConfirmClick,
            modifier = Modifier.testTag(TotpSettingsTestTags.CONFIRM_SETUP_BUTTON),
            enabled = state.canConfirm
        )
    }
}

@Composable
private fun EnabledContent(
    state: TotpSettingsScreenState.Enabled,
    onDisableClick: () -> Unit,
    onConfirmDisable: () -> Unit,
    onRegenerateClick: () -> Unit,
    onConfirmRegenerate: () -> Unit,
    onDismissDialogs: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(CoreTheme.dimens.paddingLarge)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CoreTitleText(
                text = stringResource(Res.string.totp_enabled_title),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.testTag(TotpSettingsTestTags.ENABLED_TITLE)
            )

            Spacer(Modifier.height(CoreTheme.dimens.paddingLarge))

            CoreTitleText(
                text = stringResource(Res.string.recovery_codes_title),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.testTag(TotpSettingsTestTags.RECOVERY_CODES_TITLE)
            )
            CoreBodyText(
                text = stringResource(Res.string.recovery_codes_desc),
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag(TotpSettingsTestTags.RECOVERY_CODES_DESC)
            )

            Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(TotpSettingsTestTags.RECOVERY_CODES_CONTAINER),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                state.recoveryCodes.codes.forEach { code ->
                    CoreBodyText(
                        text = code,
                        style = MaterialTheme.typography.bodyLarge.copy(fontFamily = FontFamily.Monospace)
                    )
                }
            }

            Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))

            CoreTextButton(
                text = stringResource(Res.string.copy_all),
                onClick = {
                    val allCodes = state.recoveryCodes.codes.joinToString("\n")
                    clipboardManager.setText(AnnotatedString(allCodes))
                },
                modifier = Modifier.testTag(TotpSettingsTestTags.COPY_ALL_RECOVERY_CODES_BUTTON)
            )

            Spacer(Modifier.height(CoreTheme.dimens.paddingLarge))

            CoreButton(
                text = stringResource(Res.string.regenerate_recovery_codes),
                onClick = onRegenerateClick,
                modifier = Modifier.testTag(TotpSettingsTestTags.REGENERATE_RECOVERY_CODES_BUTTON),
                enabled = !state.actionLoading
            )

            Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))

            CoreButton(
                text = stringResource(Res.string.disable_totp),
                onClick = onDisableClick,
                modifier = Modifier.testTag(TotpSettingsTestTags.DISABLE_TOTP_BUTTON),
                enabled = !state.actionLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            )

            ErrorText(
                error = state.actionError,
                testTag = TotpSettingsTestTags.ENABLED_ACTION_ERROR_TEXT
            )
        }

        if (state.showDisableConfirmation) {
            AlertDialog(
                onDismissRequest = onDismissDialogs,
                title = { CoreTitleText(text = stringResource(Res.string.dialog_confirm_title)) },
                text = { CoreBodyText(text = stringResource(Res.string.disable_totp_confirm_msg)) },
                confirmButton = {
                    CoreTextButton(
                        text = stringResource(Res.string.dialog_confirm),
                        onClick = onConfirmDisable
                    )
                },
                dismissButton = {
                    CoreTextButton(
                        text = stringResource(Res.string.dialog_cancel),
                        onClick = onDismissDialogs
                    )
                }
            )
        }

        if (state.showRegenerateConfirmation) {
            AlertDialog(
                onDismissRequest = onDismissDialogs,
                title = { CoreTitleText(text = stringResource(Res.string.dialog_confirm_title)) },
                text = { CoreBodyText(text = stringResource(Res.string.regenerate_codes_confirm_msg)) },
                confirmButton = {
                    CoreTextButton(
                        text = stringResource(Res.string.dialog_confirm),
                        onClick = onConfirmRegenerate
                    )
                },
                dismissButton = {
                    CoreTextButton(
                        text = stringResource(Res.string.dialog_cancel),
                        onClick = onDismissDialogs
                    )
                }
            )
        }
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
internal class TotpSettingsPreviewProvider : PreviewParameterProvider<TotpSettingsScreenState> {
    private val items: List<Pair<String, TotpSettingsScreenState>> = listOf(
        "Disabled" to TotpSettingsScreenState.Disabled(),
        "Setup In Progress" to TotpSettingsScreenState.SetupInProgress(
            setup = TotpSetup(
                secretKey = "JBSWY3DPEHPK3PXP",
                otpAuthUrl = "otpauth://totp/Example:user@example.com?secret=JBSWY3DPEHPK3PXP&issuer=Example",
                mfaToken = "mfa_token_sample"
            ),
            code = "123456"
        ),
        "Enabled" to TotpSettingsScreenState.Enabled(
            recoveryCodes = TotpRecoveryCodes(
                codes = listOf("1111-2222", "3333-4444", "5555-6666", "7777-8888")
            )
        ),
        "Action Loading" to TotpSettingsScreenState.Disabled(actionLoading = true),
        "Global Error" to TotpSettingsScreenState.Error(error = CommonError.Unknown()),
        "Fullscreen Loading" to TotpSettingsScreenState.Loading
    )

    override val values: Sequence<TotpSettingsScreenState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun TotpSettingsScreenPreviewContent(state: TotpSettingsScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        TotpSettingsScreen(
            component = TotpSettingsComponentMock(initialState = state)
        )
    }
}

private val defaultTotpSettingsPreviewState = TotpSettingsScreenState.Disabled()

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StatesPreview(
    @PreviewParameter(TotpSettingsPreviewProvider::class) state: TotpSettingsScreenState
) {
    ScreenPreviewContainer {
        TotpSettingsScreenPreviewContent(state = state)
    }
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        TotpSettingsScreenPreviewContent(state = defaultTotpSettingsPreviewState)
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        TotpSettingsScreenPreviewContent(state = defaultTotpSettingsPreviewState)
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        TotpSettingsScreenPreviewContent(state = defaultTotpSettingsPreviewState)
    }
}

object TotpSettingsTestTags {
    const val TITLE = "TotpSettings_Title"
    const val BACK_BUTTON = "TotpSettings_BackButton"
    const val GLOBAL_ERROR_TEXT = "TotpSettings_GlobalErrorText"

    const val DISABLED_DESC_TEXT = "TotpSettings_DisabledDescText"
    const val SETUP_TOTP_BUTTON = "TotpSettings_SetupTotpButton"
    const val DISABLED_ACTION_ERROR_TEXT = "TotpSettings_DisabledActionErrorText"

    const val STEP1_TITLE = "TotpSettings_Step1Title"
    const val STEP1_DESC = "TotpSettings_Step1Desc"
    const val QR_CODE_BOX = "TotpSettings_QrCodeBox"
    const val MANUAL_KEY_LABEL = "TotpSettings_ManualKeyLabel"
    const val SECRET_KEY_TEXT = "TotpSettings_SecretKeyText"
    const val COPY_SECRET_KEY_BUTTON = "TotpSettings_CopySecretKeyButton"
    const val STEP2_TITLE = "TotpSettings_Step2Title"
    const val STEP2_DESC = "TotpSettings_Step2Desc"
    const val CODE_INPUT = "TotpSettings_CodeInput"
    const val CONFIRM_SETUP_BUTTON = "TotpSettings_ConfirmSetupButton"
    const val SETUP_ACTION_ERROR_TEXT = "TotpSettings_SetupActionErrorText"

    const val ENABLED_TITLE = "TotpSettings_EnabledTitle"
    const val RECOVERY_CODES_TITLE = "TotpSettings_RecoveryCodesTitle"
    const val RECOVERY_CODES_DESC = "TotpSettings_RecoveryCodesDesc"
    const val RECOVERY_CODES_CONTAINER = "TotpSettings_RecoveryCodesContainer"
    const val COPY_ALL_RECOVERY_CODES_BUTTON = "TotpSettings_CopyAllRecoveryCodesButton"
    const val REGENERATE_RECOVERY_CODES_BUTTON = "TotpSettings_RegenerateRecoveryCodesButton"
    const val DISABLE_TOTP_BUTTON = "TotpSettings_DisableTotpButton"
    const val ENABLED_ACTION_ERROR_TEXT = "TotpSettings_EnabledActionErrorText"
}
