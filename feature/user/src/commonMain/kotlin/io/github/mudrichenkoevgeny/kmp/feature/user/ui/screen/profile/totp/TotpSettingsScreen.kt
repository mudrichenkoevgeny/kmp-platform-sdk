package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.*
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totprecoverycodes.TotpRecoveryCodes
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totpsetup.TotpSetup
import qrgenerator.QRCodeImage
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TotpSettingsScreen(component: TotpSettingsComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.totp_settings),
                        modifier = Modifier.testTag(TotpSettingsTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(TotpSettingsTestTags.BACK_BUTTON)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
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
                    Text(
                        text = currentState.error.toLocalizedMessage(),
                        color = MaterialTheme.colorScheme.error,
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
            .padding(Dimens.paddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.totp_disabled_desc),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.testTag(TotpSettingsTestTags.DISABLED_DESC_TEXT)
        )
        Spacer(Modifier.height(Dimens.paddingLarge))
        Button(
            onClick = onSetupClick,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(TotpSettingsTestTags.SETUP_TOTP_BUTTON),
            enabled = !state.actionLoading
        ) {
            Text(text = stringResource(Res.string.setup_totp))
        }
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
            .padding(Dimens.paddingLarge)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.totp_setup_step1),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.testTag(TotpSettingsTestTags.STEP1_TITLE)
        )
        Text(
            text = stringResource(Res.string.totp_setup_step1_desc),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.testTag(TotpSettingsTestTags.STEP1_DESC)
        )

        Spacer(Modifier.height(Dimens.paddingMedium))

        Box(
            modifier = Modifier
                .padding(Dimens.paddingMedium)
                .size(200.dp)
                .testTag(TotpSettingsTestTags.QR_CODE_BOX),
            contentAlignment = Alignment.Center
        ) {
            QRCodeImage(
                url = state.setup.otpAuthUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(Modifier.height(Dimens.paddingMedium))

        Text(
            text = stringResource(Res.string.totp_manual_key),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.testTag(TotpSettingsTestTags.MANUAL_KEY_LABEL)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = state.setup.secretKey,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.testTag(TotpSettingsTestTags.SECRET_KEY_TEXT)
            )
            IconButton(
                onClick = { clipboardManager.setText(AnnotatedString(state.setup.secretKey)) },
                modifier = Modifier.testTag(TotpSettingsTestTags.COPY_SECRET_KEY_BUTTON)
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = null,
                    modifier = Modifier.size(Dimens.paddingMedium)
                )
            }
        }

        Spacer(Modifier.height(Dimens.paddingLarge))

        Text(
            text = stringResource(Res.string.totp_setup_step2),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.testTag(TotpSettingsTestTags.STEP2_TITLE)
        )
        Text(
            text = stringResource(Res.string.totp_setup_step2_desc),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.testTag(TotpSettingsTestTags.STEP2_DESC)
        )

        Spacer(Modifier.height(Dimens.paddingMedium))

        OutlinedTextField(
            value = state.code,
            onValueChange = onCodeChanged,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(TotpSettingsTestTags.CODE_INPUT),
            label = { Text(stringResource(Res.string.totp_code)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            enabled = !state.actionLoading,
            singleLine = true
        )

        ErrorText(
            error = state.actionError,
            testTag = TotpSettingsTestTags.SETUP_ACTION_ERROR_TEXT
        )

        Spacer(Modifier.height(Dimens.paddingLarge))

        Button(
            onClick = onConfirmClick,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(TotpSettingsTestTags.CONFIRM_SETUP_BUTTON),
            enabled = state.canConfirm
        ) {
            Text(text = stringResource(Res.string.confirm))
        }
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
                .padding(Dimens.paddingLarge)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.totp_enabled_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.testTag(TotpSettingsTestTags.ENABLED_TITLE)
            )

            Spacer(Modifier.height(Dimens.paddingLarge))

            Text(
                text = stringResource(Res.string.recovery_codes_title),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.testTag(TotpSettingsTestTags.RECOVERY_CODES_TITLE)
            )
            Text(
                text = stringResource(Res.string.recovery_codes_desc),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.testTag(TotpSettingsTestTags.RECOVERY_CODES_DESC)
            )

            Spacer(Modifier.height(Dimens.paddingMedium))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(TotpSettingsTestTags.RECOVERY_CODES_CONTAINER),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                state.recoveryCodes.codes.forEach { code ->
                    Text(
                        text = code,
                        style = MaterialTheme.typography.bodyLarge,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(Modifier.height(Dimens.paddingSmall))

            TextButton(
                onClick = {
                    val allCodes = state.recoveryCodes.codes.joinToString("\n")
                    clipboardManager.setText(AnnotatedString(allCodes))
                },
                modifier = Modifier.testTag(TotpSettingsTestTags.COPY_ALL_RECOVERY_CODES_BUTTON)
            ) {
                Text(text = stringResource(Res.string.copy_all))
            }

            Spacer(Modifier.height(Dimens.paddingLarge))

            OutlinedButton(
                onClick = onRegenerateClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(TotpSettingsTestTags.REGENERATE_RECOVERY_CODES_BUTTON),
                enabled = !state.actionLoading
            ) {
                Text(text = stringResource(Res.string.regenerate_recovery_codes))
            }

            Spacer(Modifier.height(Dimens.paddingSmall))

            Button(
                onClick = onDisableClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(TotpSettingsTestTags.DISABLE_TOTP_BUTTON),
                enabled = !state.actionLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(text = stringResource(Res.string.disable_totp))
            }

            ErrorText(
                error = state.actionError,
                testTag = TotpSettingsTestTags.ENABLED_ACTION_ERROR_TEXT
            )
        }

        if (state.showDisableConfirmation) {
            AlertDialog(
                onDismissRequest = onDismissDialogs,
                title = { Text(text = stringResource(Res.string.dialog_confirm_title)) },
                text = { Text(text = stringResource(Res.string.disable_totp_confirm_msg)) },
                confirmButton = {
                    TextButton(onClick = onConfirmDisable) {
                        Text(text = stringResource(Res.string.dialog_confirm))
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismissDialogs) {
                        Text(text = stringResource(Res.string.dialog_cancel))
                    }
                }
            )
        }

        if (state.showRegenerateConfirmation) {
            AlertDialog(
                onDismissRequest = onDismissDialogs,
                title = { Text(text = stringResource(Res.string.dialog_confirm_title)) },
                text = { Text(text = stringResource(Res.string.regenerate_codes_confirm_msg)) },
                confirmButton = {
                    TextButton(onClick = onConfirmRegenerate) {
                        Text(text = stringResource(Res.string.dialog_confirm))
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismissDialogs) {
                        Text(text = stringResource(Res.string.dialog_cancel))
                    }
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
private fun TotpSettingsDisabledPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
            Surface {
                DisabledContent(
                    state = TotpSettingsScreenState.Disabled(),
                    onSetupClick = {}
                )
            }
        }
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun TotpSettingsSetupInProgressPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
            Surface {
                SetupInProgressContent(
                    state = TotpSettingsScreenState.SetupInProgress(
                        setup = TotpSetup(
                            secretKey = "JBSWY3DPEHPK3PXP",
                            otpAuthUrl = "otpauth://totp/Example:user@example.com?secret=JBSWY3DPEHPK3PXP&issuer=Example",
                            mfaToken = "mfa_token_sample"
                        ),
                        code = "123456"
                    ),
                    onCodeChanged = {},
                    onConfirmClick = {}
                )
            }
        }
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun TotpSettingsEnabledPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
            Surface {
                EnabledContent(
                    state = TotpSettingsScreenState.Enabled(
                        recoveryCodes = TotpRecoveryCodes(
                            codes = listOf("1111-2222", "3333-4444", "5555-6666", "7777-8888")
                        )
                    ),
                    onDisableClick = {},
                    onConfirmDisable = {},
                    onRegenerateClick = {},
                    onConfirmRegenerate = {},
                    onDismissDialogs = {}
                )
            }
        }
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