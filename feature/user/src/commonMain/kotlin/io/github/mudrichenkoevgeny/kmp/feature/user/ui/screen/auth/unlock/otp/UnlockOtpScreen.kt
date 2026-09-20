package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.otp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.*
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.unlock.otp.UnlockOtpComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.UnlockMethod
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnlockOtpScreen(component: UnlockOtpComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.unlock_account),
                        modifier = Modifier.testTag(UnlockOtpTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    CoreBackButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(UnlockOtpTestTags.BACK_BUTTON)
                    )
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(CoreTheme.dimens.paddingLarge)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingMedium)
            ) {
                Text(
                    text = state.target,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.testTag(UnlockOtpTestTags.TARGET_TEXT)
                )

                OutlinedTextField(
                    value = state.codeInput,
                    onValueChange = component::onCodeChanged,
                    label = { Text(text = stringResource(Res.string.totp_setup_step2)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(UnlockOtpTestTags.CODE_INPUT)
                )

                Button(
                    onClick = component::onUnlockClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(UnlockOtpTestTags.UNLOCK_BUTTON),
                    enabled = !state.actionLoading && state.codeInput.isNotBlank()
                ) {
                    Text(text = stringResource(Res.string.unlock_account))
                }

                TextButton(
                    onClick = component::onResendCodeClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(UnlockOtpTestTags.RESEND_BUTTON),
                    enabled = !state.actionLoading && state.remainingDelaySeconds == 0
                ) {
                    Text(text = stringResource(Res.string.setup_totp))
                }

                ErrorText(state.actionError)
            }

            if (state.actionLoading) {
                FullscreenLoading(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun ErrorText(error: AppError?) {
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
                    .padding(top = CoreTheme.dimens.paddingSmall)
                    .testTag(UnlockOtpTestTags.ACTION_ERROR_TEXT)
            )
        }
    }
}

internal object UnlockOtpTestTags {
    const val TITLE = "UnlockOtp_Title"
    const val BACK_BUTTON = "UnlockOtp_BackButton"
    const val TARGET_TEXT = "UnlockOtp_TargetText"
    const val CODE_INPUT = "UnlockOtp_CodeInput"
    const val UNLOCK_BUTTON = "UnlockOtp_UnlockButton"
    const val RESEND_BUTTON = "UnlockOtp_ResendButton"
    const val ACTION_ERROR_TEXT = "UnlockOtp_ActionErrorText"
}

@InternalApi
internal class UnlockOtpPreviewProvider :
    PreviewParameterProvider<UnlockOtpScreenState> {

    private val items: List<Pair<String, UnlockOtpScreenState>> = listOf(
        "Email OTP Input" to UnlockOtpScreenState(
            method = UnlockMethod.EMAIL,
            target = "user@example.com",
            codeInput = "123456"
        ),
        "Phone OTP Input" to UnlockOtpScreenState(
            method = UnlockMethod.PHONE,
            target = "+79991234567",
            codeInput = "654321"
        ),
        "Resend Delay Active" to UnlockOtpScreenState(
            method = UnlockMethod.EMAIL,
            target = "user@example.com",
            codeInput = "",
            remainingDelaySeconds = 42
        ),
        "Action Loading" to UnlockOtpScreenState(
            method = UnlockMethod.EMAIL,
            target = "user@example.com",
            codeInput = "123456",
            actionLoading = true
        ),
        "Action Error" to UnlockOtpScreenState(
            method = UnlockMethod.EMAIL,
            target = "user@example.com",
            codeInput = "123456",
            actionError = CommonError.Unknown()
        )
    )

    override val values: Sequence<UnlockOtpScreenState> =
        items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? =
        items.getOrNull(index)?.first
}

@OptIn(InternalApi::class)
@Composable
private fun UnlockOtpScreenPreviewContent(state: UnlockOtpScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        Surface {
            UnlockOtpScreen(
                component = UnlockOtpComponentMock(initialState = state)
            )
        }
    }
}

private val defaultOtpPreviewState = UnlockOtpScreenState(
    method = UnlockMethod.EMAIL,
    target = "user@example.com",
    codeInput = "123456"
)

@OptIn(InternalApi::class)
@Preview(showBackground = true, group = "States")
@Composable
private fun StatesPreview(
    @PreviewParameter(UnlockOtpPreviewProvider::class) state: UnlockOtpScreenState
) {
    ScreenPreviewContainer {
        UnlockOtpScreenPreviewContent(state = state)
    }
}

@OptIn(InternalApi::class)
@ScreenSizePreviews
@Composable
private fun AdaptivePreview() {
    ScreenPreviewContainer {
        UnlockOtpScreenPreviewContent(state = defaultOtpPreviewState)
    }
}

@OptIn(InternalApi::class)
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        UnlockOtpScreenPreviewContent(state = defaultOtpPreviewState)
    }
}

@OptIn(InternalApi::class)
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        UnlockOtpScreenPreviewContent(state = defaultOtpPreviewState)
    }
}
