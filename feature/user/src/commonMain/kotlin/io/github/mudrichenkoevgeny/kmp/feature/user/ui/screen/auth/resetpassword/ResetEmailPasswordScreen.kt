package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.resetpassword

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
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreBackButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenOverlayLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.*
import org.jetbrains.compose.resources.stringResource

/**
 * Password reset UI: email + send code, then code + new password with resend timer and change-email action.
 *
 * @param component Decompose component providing state and callbacks.
 */
@Composable
fun ResetEmailPasswordScreen(component: ResetEmailPasswordComponent) {
    val state by component.state.subscribeAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val currentState = state) {
            is ResetEmailPasswordScreenState.Loading -> FullscreenLoading()
            is ResetEmailPasswordScreenState.EmailInput -> {
                EmailInputContent(
                    state = currentState,
                    onEmailChanged = component::onEmailChanged,
                    onSendCodeClick = component::onSendCodeClick,
                    onBackClick = component::onBackClick
                )
            }
            is ResetEmailPasswordScreenState.ResetInput -> {
                ResetInputContent(
                    state = currentState,
                    onCodeChanged = component::onCodeChanged,
                    onPasswordChanged = component::onPasswordChanged,
                    onConfirmResetClick = component::onConfirmResetClick,
                    onResendCodeClick = component::onSendCodeClick,
                    onResetEmailClick = component::onResetEmailClick,
                    onBackClick = component::onBackClick
                )
            }
        }
    }
}

@Composable
private fun EmailInputContent(
    state: ResetEmailPasswordScreenState.EmailInput,
    onEmailChanged: (String) -> Unit,
    onSendCodeClick: () -> Unit,
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
                        .testTag(ResetEmailPasswordTestTags.EMAIL_STEP_BACK_BUTTON),
                    enabled = !state.actionLoading
                )

                Text(
                    text = stringResource(Res.string.reset_password),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.testTag(ResetEmailPasswordTestTags.EMAIL_STEP_TITLE)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = state.email,
                    onValueChange = onEmailChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(ResetEmailPasswordTestTags.EMAIL_INPUT),
                    label = { Text(stringResource(Res.string.email)) },
                    isError = state.actionError != null,
                    enabled = !state.actionLoading,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                ErrorText(state.actionError, ResetEmailPasswordTestTags.EMAIL_STEP_ERROR_TEXT)
            }

            Button(
                onClick = onSendCodeClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(ResetEmailPasswordTestTags.SEND_CODE_BUTTON),
                enabled = state.isEmailValid && !state.actionLoading
            ) {
                Text(stringResource(Res.string.send_code))
            }
        }
        if (state.actionLoading) FullscreenOverlayLoading()
    }
}

@Composable
private fun ResetInputContent(
    state: ResetEmailPasswordScreenState.ResetInput,
    onCodeChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onConfirmResetClick: () -> Unit,
    onResendCodeClick: () -> Unit,
    onResetEmailClick: () -> Unit,
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
                        .testTag(ResetEmailPasswordTestTags.RESET_STEP_BACK_BUTTON),
                    enabled = !state.actionLoading
                )

                Text(
                    text = stringResource(Res.string.enter_confirmation_code),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.testTag(ResetEmailPasswordTestTags.RESET_STEP_TITLE)
                )
            }

            Text(
                text = stringResource(Res.string.code_sent_to, state.email),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .padding(top = CoreTheme.dimens.paddingSmall)
                    .testTag(ResetEmailPasswordTestTags.CODE_SENT_INFO_TEXT)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = state.code,
                    onValueChange = onCodeChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(ResetEmailPasswordTestTags.CODE_INPUT),
                    label = { Text(stringResource(Res.string.confirmation_code)) },
                    enabled = !state.actionLoading,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                )

                Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))

                OutlinedTextField(
                    value = state.newPassword,
                    onValueChange = onPasswordChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(ResetEmailPasswordTestTags.NEW_PASSWORD_INPUT),
                    label = { Text(stringResource(Res.string.new_password)) },
                    enabled = !state.actionLoading,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                ErrorText(state.actionError, ResetEmailPasswordTestTags.RESET_STEP_ERROR_TEXT)

                Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))

                if (!state.canResendCode) {
                    Text(
                        text = stringResource(Res.string.resend_code_timer, state.resendTimerSeconds),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.testTag(ResetEmailPasswordTestTags.RESEND_TIMER_TEXT)
                    )
                } else {
                    TextButton(
                        onClick = onResendCodeClick,
                        modifier = Modifier.testTag(ResetEmailPasswordTestTags.RESEND_CODE_BUTTON)
                    ) {
                        Text(text = stringResource(Res.string.resend_code))
                    }
                }
            }

            Button(
                onClick = onConfirmResetClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(ResetEmailPasswordTestTags.CONFIRM_BUTTON),
                enabled = state.canConfirm
            ) {
                Text(stringResource(Res.string.confirm))
            }

            Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))

            TextButton(
                onClick = onResetEmailClick,
                modifier = Modifier.testTag(ResetEmailPasswordTestTags.CHANGE_EMAIL_BUTTON)
            ) {
                Text(text = stringResource(Res.string.change_email))
            }
        }
        if (state.actionLoading) FullscreenOverlayLoading()
    }
}

@Composable
private fun ErrorText(error: AppError?, tag: String) {
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
                    .testTag(tag)
            )
        }
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun ResetEmailPasswordScreenPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
            Surface {
                EmailInputContent(
                    state = ResetEmailPasswordScreenState.EmailInput(email = "test@example.com"),
                    onEmailChanged = {},
                    onSendCodeClick = {},
                    onBackClick = {}
                )
            }
        }
    }
}

internal object ResetEmailPasswordTestTags {
    const val EMAIL_STEP_BACK_BUTTON = "ResetEmailPassword_EmailStepBackButton"
    const val EMAIL_STEP_TITLE = "ResetEmailPassword_EmailStepTitle"
    const val EMAIL_INPUT = "ResetEmailPassword_EmailInput"
    const val SEND_CODE_BUTTON = "ResetEmailPassword_SendCodeButton"
    const val EMAIL_STEP_ERROR_TEXT = "ResetEmailPassword_EmailStepErrorText"

    const val RESET_STEP_BACK_BUTTON = "ResetEmailPassword_ResetStepBackButton"
    const val RESET_STEP_TITLE = "ResetEmailPassword_ResetStepTitle"
    const val CODE_SENT_INFO_TEXT = "ResetEmailPassword_CodeSentInfoText"
    const val CODE_INPUT = "ResetEmailPassword_CodeInput"
    const val NEW_PASSWORD_INPUT = "ResetEmailPassword_NewPasswordInput"
    const val RESEND_TIMER_TEXT = "ResetEmailPassword_ResendTimerText"
    const val RESEND_CODE_BUTTON = "ResetEmailPassword_ResendCodeButton"
    const val CONFIRM_BUTTON = "ResetEmailPassword_ConfirmButton"
    const val CHANGE_EMAIL_BUTTON = "ResetEmailPassword_ChangeEmailButton"
    const val RESET_STEP_ERROR_TEXT = "ResetEmailPassword_ResetStepErrorText"
}
