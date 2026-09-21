package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.totp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
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
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreTextButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input.CoreOutlinedTextField
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenOverlayLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreBodyText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreErrorText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreScreenTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.DialogPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.DialogSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.*
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.login.totp.LoginByTotpComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.utils.FieldValidator
import org.jetbrains.compose.resources.stringResource

/**
 * MFA login form: handles TOTP code entry and switching to recovery code mode.
 *
 * @param component Decompose component providing state and callbacks.
 */
@Composable
fun LoginByTotpScreen(component: LoginByTotpComponent) {
    val state by component.state.subscribeAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val currentState = state) {
            is LoginByTotpScreenState.Loading -> {
                FullscreenLoading()
            }
            is LoginByTotpScreenState.Content -> {
                LoginByTotpContent(
                    state = currentState,
                    onCodeChanged = component::onCodeChanged,
                    onToggleModeClick = component::onToggleModeClick,
                    onSubmitClick = component::onSubmitClick,
                    onBackClick = component::onBackClick
                )
            }
        }
    }
}

@Composable
private fun LoginByTotpContent(
    state: LoginByTotpScreenState.Content,
    onCodeChanged: (String) -> Unit,
    onToggleModeClick: () -> Unit,
    onSubmitClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(CoreTheme.dimens.paddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CoreBackButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .testTag(LoginByTotpTestTags.BACK_BUTTON),
                    enabled = !state.actionLoading
                )

                val titleRes = when (state.mode) {
                    LoginByTotpScreenState.Mode.TOTP -> Res.string.login_by_totp
                    LoginByTotpScreenState.Mode.RECOVERY_CODE -> Res.string.login_by_recovery_code
                }

                CoreScreenTitleText(
                    text = stringResource(titleRes),
                    modifier = Modifier.testTag(LoginByTotpTestTags.TITLE)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val descriptionText = when (state.mode) {
                    LoginByTotpScreenState.Mode.TOTP -> stringResource(
                        Res.string.login_by_totp_desc,
                        FieldValidator.TOTP_CODE_LENGTH
                    )
                    LoginByTotpScreenState.Mode.RECOVERY_CODE -> stringResource(
                        Res.string.login_by_recovery_code_desc
                    )
                }

                CoreBodyText(
                    text = descriptionText,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(bottom = CoreTheme.dimens.paddingLarge)
                        .testTag(LoginByTotpTestTags.DESCRIPTION)
                )

                val labelRes = when (state.mode) {
                    LoginByTotpScreenState.Mode.TOTP -> Res.string.totp_code
                    LoginByTotpScreenState.Mode.RECOVERY_CODE -> Res.string.recovery_code
                }

                val keyboardType = when (state.mode) {
                    LoginByTotpScreenState.Mode.TOTP -> KeyboardType.Number
                    LoginByTotpScreenState.Mode.RECOVERY_CODE -> KeyboardType.Text
                }

                CoreOutlinedTextField(
                    value = state.code,
                    onValueChange = onCodeChanged,
                    modifier = Modifier
                        .testTag(LoginByTotpTestTags.CODE_INPUT),
                    label = { CoreBodyText(stringResource(labelRes)) },
                    isError = state.actionError != null,
                    enabled = !state.actionLoading,
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                    singleLine = true
                )

                ErrorText(state.actionError)
            }

            CoreButton(
                text = stringResource(Res.string.login),
                onClick = onSubmitClick,
                modifier = Modifier.testTag(LoginByTotpTestTags.SUBMIT_BUTTON),
                enabled = state.canSubmit
            )

            Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))

            val toggleTextRes = when (state.mode) {
                LoginByTotpScreenState.Mode.TOTP -> Res.string.use_recovery_code
                LoginByTotpScreenState.Mode.RECOVERY_CODE -> Res.string.use_totp
            }

            CoreTextButton(
                text = stringResource(toggleTextRes),
                onClick = onToggleModeClick,
                modifier = Modifier.testTag(LoginByTotpTestTags.TOGGLE_MODE_BUTTON),
                enabled = !state.actionLoading
            )
        }

        if (state.actionLoading) {
            FullscreenOverlayLoading()
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
            CoreErrorText(
                text = it.toLocalizedMessage(),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = CoreTheme.dimens.paddingSmall)
                    .testTag(LoginByTotpTestTags.ERROR_TEXT)
            )
        }
    }
}

@InternalApi
internal class LoginByTotpPreviewProvider : PreviewParameterProvider<LoginByTotpScreenState> {
    private val items: List<Pair<String, LoginByTotpScreenState>> = listOf(
        "TOTP Mode" to LoginByTotpScreenState.Content(
            mfaToken = "token",
            mode = LoginByTotpScreenState.Mode.TOTP,
            code = "123456"
        ),
        "Recovery Code Mode" to LoginByTotpScreenState.Content(
            mfaToken = "token",
            mode = LoginByTotpScreenState.Mode.RECOVERY_CODE,
            code = "RECOVERY-CODE-123"
        ),
        "Inline Error" to LoginByTotpScreenState.Content(
            mfaToken = "token",
            mode = LoginByTotpScreenState.Mode.TOTP,
            code = "111111",
            actionError = CommonError.Unknown()
        ),
        "Action Loading" to LoginByTotpScreenState.Content(
            mfaToken = "token",
            mode = LoginByTotpScreenState.Mode.TOTP,
            code = "123456",
            actionLoading = true
        ),
        "Fullscreen Loading" to LoginByTotpScreenState.Loading
    )

    override val values: Sequence<LoginByTotpScreenState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun LoginByTotpScreenPreviewContent(state: LoginByTotpScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        LoginByTotpScreen(
            component = LoginByTotpComponentMock(initialState = state)
        )
    }
}

private val defaultLoginByTotpPreviewState = LoginByTotpScreenState.Content(
    mfaToken = "token",
    mode = LoginByTotpScreenState.Mode.TOTP,
    code = "123456"
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StatesPreview(
    @PreviewParameter(LoginByTotpPreviewProvider::class) state: LoginByTotpScreenState
) {
    DialogPreviewContainer {
        LoginByTotpScreenPreviewContent(state = state)
    }
}

@InternalApi
@DialogSizePreviews
@Composable
private fun DialogSizePreview() {
    DialogPreviewContainer {
        LoginByTotpScreenPreviewContent(state = defaultLoginByTotpPreviewState)
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    DialogPreviewContainer {
        LoginByTotpScreenPreviewContent(state = defaultLoginByTotpPreviewState)
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    DialogPreviewContainer {
        LoginByTotpScreenPreviewContent(state = defaultLoginByTotpPreviewState)
    }
}

internal object LoginByTotpTestTags {
    const val BACK_BUTTON = "LoginByTotp_BackButton"
    const val TITLE = "LoginByTotp_Title"
    const val DESCRIPTION = "LoginByTotp_Description"
    const val CODE_INPUT = "LoginByTotp_CodeInput"
    const val SUBMIT_BUTTON = "LoginByTotp_SubmitButton"
    const val TOGGLE_MODE_BUTTON = "LoginByTotp_ToggleModeButton"
    const val ERROR_TEXT = "LoginByTotp_ErrorText"
}
