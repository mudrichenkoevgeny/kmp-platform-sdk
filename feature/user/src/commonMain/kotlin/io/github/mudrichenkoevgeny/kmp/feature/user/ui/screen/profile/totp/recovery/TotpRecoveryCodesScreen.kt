package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.recovery

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreBackButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreTextButton
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
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.totp.recovery.TotpRecoveryCodesComponentMock
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totprecoverycodes.TotpRecoveryCodes
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TotpRecoveryCodesScreen(component: TotpRecoveryCodesComponent) {
    val state by component.state.subscribeAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        CoreScreenTitleText(
                            text = stringResource(Res.string.recovery_codes_title),
                            modifier = Modifier.testTag(TotpRecoveryCodesTestTags.TITLE)
                        )
                    },
                    navigationIcon = {
                        CoreBackButton(
                            onClick = component::onBackClick,
                            modifier = Modifier.testTag(TotpRecoveryCodesTestTags.BACK_BUTTON)
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
                when (val currentState = state) {
                    is TotpRecoveryCodesScreenState.Loading -> FullscreenLoading()
                    is TotpRecoveryCodesScreenState.Content -> Content(
                        state = currentState,
                        onRegenerateClick = component::onRegenerateClick,
                        onConfirmRegenerate = component::onConfirmRegenerate,
                        onDismissDialogs = component::onDismissDialogs
                    )
                    is TotpRecoveryCodesScreenState.Error -> {
                        CoreErrorText(
                            text = currentState.error.toLocalizedMessage(),
                            modifier = Modifier.testTag(TotpRecoveryCodesTestTags.GLOBAL_ERROR_TEXT)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Content(
    state: TotpRecoveryCodesScreenState.Content,
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
                text = stringResource(Res.string.recovery_codes_title),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.testTag(TotpRecoveryCodesTestTags.RECOVERY_CODES_TITLE)
            )
            CoreBodyText(
                text = stringResource(Res.string.recovery_codes_desc),
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag(TotpRecoveryCodesTestTags.RECOVERY_CODES_DESC)
            )

            Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(TotpRecoveryCodesTestTags.RECOVERY_CODES_CONTAINER),
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
                modifier = Modifier.testTag(TotpRecoveryCodesTestTags.COPY_ALL_RECOVERY_CODES_BUTTON)
            )

            Spacer(Modifier.height(CoreTheme.dimens.paddingLarge))

            CoreButton(
                text = stringResource(Res.string.regenerate_recovery_codes),
                onClick = onRegenerateClick,
                modifier = Modifier.testTag(TotpRecoveryCodesTestTags.REGENERATE_RECOVERY_CODES_BUTTON),
                enabled = !state.actionLoading
            )

            ErrorText(
                error = state.actionError,
                testTag = TotpRecoveryCodesTestTags.ACTION_ERROR_TEXT
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
internal class TotpRecoveryCodesPreviewProvider : PreviewParameterProvider<TotpRecoveryCodesScreenState> {
    private val items: List<Pair<String, TotpRecoveryCodesScreenState>> = listOf(
        "Content" to TotpRecoveryCodesScreenState.Content(
            recoveryCodes = TotpRecoveryCodes(
                codes = listOf("1111-2222", "3333-4444", "5555-6666", "7777-8888")
            )
        ),
        "Global Error" to TotpRecoveryCodesScreenState.Error(error = CommonError.Unknown()),
        "Loading" to TotpRecoveryCodesScreenState.Loading
    )

    override val values: Sequence<TotpRecoveryCodesScreenState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun TotpRecoveryCodesScreenPreviewContent(state: TotpRecoveryCodesScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        TotpRecoveryCodesScreen(
            component = TotpRecoveryCodesComponentMock(initialState = state)
        )
    }
}

private val defaultTotpRecoveryCodesPreviewState = TotpRecoveryCodesScreenState.Content(
    recoveryCodes = TotpRecoveryCodes(codes = listOf("1111-2222", "3333-4444"))
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StatesPreview(
    @PreviewParameter(TotpRecoveryCodesPreviewProvider::class) state: TotpRecoveryCodesScreenState
) {
    ScreenPreviewContainer {
        TotpRecoveryCodesScreenPreviewContent(state = state)
    }
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        TotpRecoveryCodesScreenPreviewContent(state = defaultTotpRecoveryCodesPreviewState)
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        TotpRecoveryCodesScreenPreviewContent(state = defaultTotpRecoveryCodesPreviewState)
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        TotpRecoveryCodesScreenPreviewContent(state = defaultTotpRecoveryCodesPreviewState)
    }
}

object TotpRecoveryCodesTestTags {
    const val TITLE = "TotpRecoveryCodes_Title"
    const val BACK_BUTTON = "TotpRecoveryCodes_BackButton"
    const val GLOBAL_ERROR_TEXT = "TotpRecoveryCodes_GlobalErrorText"
    const val RECOVERY_CODES_TITLE = "TotpRecoveryCodes_TitleText"
    const val RECOVERY_CODES_DESC = "TotpRecoveryCodes_DescText"
    const val RECOVERY_CODES_CONTAINER = "TotpRecoveryCodes_Container"
    const val COPY_ALL_RECOVERY_CODES_BUTTON = "TotpRecoveryCodes_CopyAllButton"
    const val REGENERATE_RECOVERY_CODES_BUTTON = "TotpRecoveryCodes_RegenerateButton"
    const val ACTION_ERROR_TEXT = "TotpRecoveryCodes_ActionErrorText"
}
