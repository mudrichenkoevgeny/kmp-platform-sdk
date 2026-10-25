package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.totp.main

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.ic_copy
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
import io.github.mudrichenkoevgeny.kmp.feature.user.localization.LocalUserStrings
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.totp.TotpMainComponentMock
import io.github.mudrichenkoevgeny.shared.foundation.core.common.ui.UiValues
import org.jetbrains.compose.resources.painterResource
import io.github.mudrichenkoevgeny.kmp.core.common.Res as CommonRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TotpMainScreen(component: TotpMainComponent) {
    val state by component.state.subscribeAsState()
    val userUi = LocalUserStrings.current.ui

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        CoreScreenTitleText(
                            text = userUi.totpMain,
                            modifier = Modifier.testTag(TotpMainTestTags.TITLE)
                        )
                    },
                    navigationIcon = {
                        CoreBackButton(
                            onClick = component::onBackClick,
                            modifier = Modifier.testTag(TotpMainTestTags.BACK_BUTTON)
                        )
                    }
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                val initError = state.initError
                if (initError != null) {
                    CoreErrorText(
                        text = initError.toLocalizedMessage(),
                        modifier = Modifier.testTag(TotpMainTestTags.GLOBAL_ERROR_TEXT)
                    )
                } else if (!state.isInitialized) {
                    FullscreenLoading()
                } else {
                    Children(
                        stack = component.stack,
                        modifier = Modifier.fillMaxSize()
                    ) { child ->
                        when (val activeChild = child.instance) {
                            is TotpMainComponent.Child.Status -> {
                                if (state.isEnabled) {
                                    EnabledContent(
                                        state = state,
                                        onRecoveryCodesClick = component::onRecoveryCodesClick,
                                        onDisableClick = component::onDisableClick
                                    )
                                } else {
                                    DisabledContent(
                                        state = state,
                                        onSetupClick = component::onSetupClick
                                    )
                                }
                            }
                            is TotpMainComponent.Child.Setup -> {
                                SetupInProgressContent(
                                    state = state,
                                    setup = activeChild,
                                    onCodeChanged = component::onCodeChanged,
                                    onConfirmClick = component::onConfirmSetupClick
                                )
                            }
                        }
                    }
                }
                
                val dialogSlot by component.dialogSlot.subscribeAsState()
                dialogSlot.child?.instance?.let { dialogChild ->
                    when (dialogChild) {
                        is TotpMainComponent.DialogChild.ConfirmDisable -> {
                            AlertDialog(
                                onDismissRequest = component::onDismissDialogs,
                                title = { CoreTitleText(text = userUi.dialogConfirmTitle) },
                                text = { CoreBodyText(text = userUi.disableTotpConfirmMsg) },
                                confirmButton = {
                                    CoreTextButton(
                                        text = userUi.dialogConfirm,
                                        onClick = component::onConfirmDisable
                                    )
                                },
                                dismissButton = {
                                    CoreTextButton(
                                        text = userUi.dialogCancel,
                                        onClick = component::onDismissDialogs
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
private fun DisabledContent(
    state: TotpMainScreenState,
    onSetupClick: () -> Unit
) {
    val userUi = LocalUserStrings.current.ui
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(CoreTheme.dimens.paddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CoreBodyText(
            text = userUi.totpDisabledDesc,
            textAlign = TextAlign.Center,
            modifier = Modifier.testTag(TotpMainTestTags.DISABLED_DESC_TEXT)
        )
        Spacer(Modifier.height(CoreTheme.dimens.paddingLarge))
        CoreButton(
            text = userUi.setupTotp,
            onClick = onSetupClick,
            modifier = Modifier.testTag(TotpMainTestTags.SETUP_TOTP_BUTTON),
            enabled = !state.actionLoading
        )
        ErrorText(
            error = state.actionError,
            testTag = TotpMainTestTags.DISABLED_ACTION_ERROR_TEXT
        )
    }
}

@Composable
private fun SetupInProgressContent(
    state: TotpMainScreenState,
    setup: TotpMainComponent.Child.Setup,
    onCodeChanged: (String) -> Unit,
    onConfirmClick: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val userUi = LocalUserStrings.current.ui

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(CoreTheme.dimens.paddingLarge)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CoreTitleText(
            text = userUi.totpSetupStep1,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.testTag(TotpMainTestTags.STEP1_TITLE)
        )
        CoreBodyText(
            text = userUi.totpSetupStep1Desc,
            textAlign = TextAlign.Center,
            modifier = Modifier.testTag(TotpMainTestTags.STEP1_DESC)
        )

        Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))

        Box(
            modifier = Modifier
                .padding(CoreTheme.dimens.paddingMedium)
                .size(CoreTheme.dimens.qrCodeSize)
                .testTag(TotpMainTestTags.QR_CODE_BOX),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberQrCodePainter(setup.otpAuthUrl),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))

        CoreBodyText(
            text = userUi.totpManualKey,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.testTag(TotpMainTestTags.MANUAL_KEY_LABEL)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CoreTitleText(
                text = setup.secretKey,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.testTag(TotpMainTestTags.SECRET_KEY_TEXT)
            )
            IconButton(
                onClick = { clipboardManager.setText(AnnotatedString(setup.secretKey)) },
                modifier = Modifier.testTag(TotpMainTestTags.COPY_SECRET_KEY_BUTTON)
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
            text = userUi.totpSetupStep2,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.testTag(TotpMainTestTags.STEP2_TITLE)
        )
        CoreBodyText(
            text = userUi.totpSetupStep2Desc,
            textAlign = TextAlign.Center,
            modifier = Modifier.testTag(TotpMainTestTags.STEP2_DESC)
        )

        Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))

        CoreCodeTextField(
            value = state.code,
            onValueChange = onCodeChanged,
            label = { CoreBodyText(userUi.totpCode) },
            placeholder = { CoreBodyText(UiValues.Placeholder.TOTP_CODE) },
            enabled = !state.actionLoading,
            isError = state.actionError != null,
            modifier = Modifier.testTag(TotpMainTestTags.CODE_INPUT)
        )

        ErrorText(
            error = state.actionError,
            testTag = TotpMainTestTags.SETUP_ACTION_ERROR_TEXT
        )

        Spacer(Modifier.height(CoreTheme.dimens.paddingLarge))

        CoreButton(
            text = userUi.confirm,
            onClick = onConfirmClick,
            modifier = Modifier.testTag(TotpMainTestTags.CONFIRM_SETUP_BUTTON),
            enabled = state.canConfirm
        )
    }
}

@Composable
private fun EnabledContent(
    state: TotpMainScreenState,
    onRecoveryCodesClick: () -> Unit,
    onDisableClick: () -> Unit
) {
    val userUi = LocalUserStrings.current.ui
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(CoreTheme.dimens.paddingLarge)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CoreTitleText(
                text = userUi.totpEnabledTitle,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.testTag(TotpMainTestTags.ENABLED_TITLE)
            )

            Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))

            CoreBodyText(
                text = userUi.totpEnabledDesc,
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag(TotpMainTestTags.ENABLED_DESC)
            )

            Spacer(Modifier.height(CoreTheme.dimens.paddingLarge))

            CoreButton(
                text = userUi.recoveryCodesTitle,
                onClick = onRecoveryCodesClick,
                modifier = Modifier.testTag(TotpMainTestTags.RECOVERY_CODES_BUTTON),
                enabled = !state.actionLoading
            )

            Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))

            CoreButton(
                text = userUi.disableTotp,
                onClick = onDisableClick,
                modifier = Modifier.testTag(TotpMainTestTags.DISABLE_TOTP_BUTTON),
                enabled = !state.actionLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            )

            ErrorText(
                error = state.actionError,
                testTag = TotpMainTestTags.ENABLED_ACTION_ERROR_TEXT
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
internal class TotpMainPreviewProvider : PreviewParameterProvider<TotpMainScreenState> {
    private val items: List<Pair<String, TotpMainScreenState>> = listOf(
        "Disabled" to TotpMainScreenState(
            isInitialized = true,
            isEnabled = false
        ),
        "Setup In Progress" to TotpMainScreenState(
            isInitialized = true,
            isEnabled = false,
            code = "123456"
        ),
        "Enabled" to TotpMainScreenState(
            isInitialized = true,
            isEnabled = true
        ),
        "Action Loading" to TotpMainScreenState(
            isInitialized = true,
            actionLoading = true
        ),
        "Global Error" to TotpMainScreenState(
            initError = CommonError.Unknown()
        ),
        "Fullscreen Loading" to TotpMainScreenState()
    )

    override val values: Sequence<TotpMainScreenState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun TotpMainScreenPreviewContent(state: TotpMainScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        TotpMainScreen(
            component = TotpMainComponentMock(initialState = state)
        )
    }
}

private val defaultTotpMainPreviewState = TotpMainScreenState(
    isInitialized = true,
    isEnabled = false
)

@InternalApi
@ScreenSizePreviews
@Composable
private fun StatesPreview(
    @PreviewParameter(TotpMainPreviewProvider::class) state: TotpMainScreenState
) {
    ScreenPreviewContainer {
        TotpMainScreenPreviewContent(state = state)
    }
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        TotpMainScreenPreviewContent(state = defaultTotpMainPreviewState)
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        TotpMainScreenPreviewContent(state = defaultTotpMainPreviewState)
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        TotpMainScreenPreviewContent(state = defaultTotpMainPreviewState)
    }
}

object TotpMainTestTags {
    const val TITLE = "TotpMain_Title"
    const val BACK_BUTTON = "TotpMain_BackButton"
    const val GLOBAL_ERROR_TEXT = "TotpMain_GlobalErrorText"

    const val DISABLED_DESC_TEXT = "TotpMain_DisabledDescText"
    const val SETUP_TOTP_BUTTON = "TotpMain_SetupTotpButton"
    const val DISABLED_ACTION_ERROR_TEXT = "TotpMain_DisabledActionErrorText"

    const val STEP1_TITLE = "TotpMain_Step1Title"
    const val STEP1_DESC = "TotpMain_Step1Desc"
    const val QR_CODE_BOX = "TotpMain_QrCodeBox"
    const val MANUAL_KEY_LABEL = "TotpMain_ManualKeyLabel"
    const val SECRET_KEY_TEXT = "TotpMain_SecretKeyText"
    const val COPY_SECRET_KEY_BUTTON = "TotpMain_CopySecretKeyButton"
    const val STEP2_TITLE = "TotpMain_Step2Title"
    const val STEP2_DESC = "TotpMain_Step2Desc"
    const val CODE_INPUT = "TotpMain_CodeInput"
    const val CONFIRM_SETUP_BUTTON = "TotpMain_ConfirmSetupButton"
    const val SETUP_ACTION_ERROR_TEXT = "TotpMain_SetupActionErrorText"

    const val ENABLED_TITLE = "TotpMain_EnabledTitle"
    const val ENABLED_DESC = "TotpMain_EnabledDescText"
    const val RECOVERY_CODES_BUTTON = "TotpMain_RecoveryCodesButton"
    const val DISABLE_TOTP_BUTTON = "TotpMain_DisableTotpButton"
    const val ENABLED_ACTION_ERROR_TEXT = "TotpMain_EnabledActionErrorText"
}
