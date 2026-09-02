package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.totp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenOverlayLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.*
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.login.totp.LoginByTotpComponentMock
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
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
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
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.paddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .testTag(LoginByTotpTestTags.BACK_BUTTON),
                    enabled = !state.actionLoading
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }

                val titleRes = when (state.mode) {
                    LoginByTotpScreenState.Mode.TOTP -> Res.string.login_by_totp
                    LoginByTotpScreenState.Mode.RECOVERY_CODE -> Res.string.login_by_recovery_code
                }

                Text(
                    text = stringResource(titleRes),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.testTag(LoginByTotpTestTags.TITLE)
                )
            }

            Spacer(Modifier.height(Dimens.paddingLarge))

            val labelRes = when (state.mode) {
                LoginByTotpScreenState.Mode.TOTP -> Res.string.totp_code
                LoginByTotpScreenState.Mode.RECOVERY_CODE -> Res.string.recovery_code
            }

            val keyboardType = when (state.mode) {
                LoginByTotpScreenState.Mode.TOTP -> KeyboardType.Number
                LoginByTotpScreenState.Mode.RECOVERY_CODE -> KeyboardType.Text
            }

            OutlinedTextField(
                value = state.code,
                onValueChange = onCodeChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(LoginByTotpTestTags.CODE_INPUT),
                label = { Text(stringResource(labelRes)) },
                isError = state.actionError != null,
                enabled = !state.actionLoading,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                singleLine = true
            )

            ErrorText(state.actionError)

            Spacer(Modifier.height(Dimens.paddingLarge))

            Button(
                onClick = onSubmitClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(LoginByTotpTestTags.SUBMIT_BUTTON),
                enabled = state.canSubmit
            ) {
                Text(stringResource(Res.string.login))
            }

            Spacer(Modifier.height(Dimens.paddingMedium))

            val toggleTextRes = when (state.mode) {
                LoginByTotpScreenState.Mode.TOTP -> Res.string.use_recovery_code
                LoginByTotpScreenState.Mode.RECOVERY_CODE -> Res.string.use_totp
            }

            TextButton(
                onClick = onToggleModeClick,
                modifier = Modifier.testTag(LoginByTotpTestTags.TOGGLE_MODE_BUTTON),
                enabled = !state.actionLoading
            ) {
                Text(
                    text = stringResource(toggleTextRes),
                    style = MaterialTheme.typography.labelLarge
                )
            }
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
            Text(
                text = it.toLocalizedMessage(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = Dimens.paddingSmall)
                    .testTag(LoginByTotpTestTags.ERROR_TEXT)
            )
        }
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun LoginByTotpContentPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
            Surface {
                LoginByTotpContent(
                    state = LoginByTotpScreenState.Content(
                        mfaToken = "token",
                        mode = LoginByTotpScreenState.Mode.TOTP
                    ),
                    onCodeChanged = {},
                    onToggleModeClick = {},
                    onSubmitClick = {},
                    onBackClick = {}
                )
            }
        }
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun LoginByTotpContentRecoveryPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
            Surface {
                LoginByTotpContent(
                    state = LoginByTotpScreenState.Content(
                        mfaToken = "token",
                        mode = LoginByTotpScreenState.Mode.RECOVERY_CODE
                    ),
                    onCodeChanged = {},
                    onToggleModeClick = {},
                    onSubmitClick = {},
                    onBackClick = {}
                )
            }
        }
    }
}

internal object LoginByTotpTestTags {
    const val BACK_BUTTON = "LoginByTotp_BackButton"
    const val TITLE = "LoginByTotp_Title"
    const val CODE_INPUT = "LoginByTotp_CodeInput"
    const val SUBMIT_BUTTON = "LoginByTotp_SubmitButton"
    const val TOGGLE_MODE_BUTTON = "LoginByTotp_ToggleModeButton"
    const val ERROR_TEXT = "LoginByTotp_ErrorText"
}