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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input.CoreCodeTextField
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input.CoreEmailTextField
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
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.resetpassword.ResetEmailPasswordComponentMock
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

                CoreScreenTitleText(
                    text = stringResource(Res.string.reset_password),
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
                CoreEmailTextField(
                    value = state.email,
                    onValueChange = onEmailChanged,
                    label = { CoreBodyText(stringResource(Res.string.email)) },
                    placeholder = { CoreBodyText(stringResource(Res.string.email)) },
                    modifier = Modifier.testTag(ResetEmailPasswordTestTags.EMAIL_INPUT),
                    isError = state.actionError != null,
                    enabled = !state.actionLoading
                )

                ErrorText(state.actionError, ResetEmailPasswordTestTags.EMAIL_STEP_ERROR_TEXT)
            }

            CoreButton(
                text = stringResource(Res.string.send_code),
                onClick = onSendCodeClick,
                modifier = Modifier.testTag(ResetEmailPasswordTestTags.SEND_CODE_BUTTON),
                enabled = state.isEmailValid && !state.actionLoading
            )
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

                CoreScreenTitleText(
                    text = stringResource(Res.string.enter_confirmation_code),
                    modifier = Modifier.testTag(ResetEmailPasswordTestTags.RESET_STEP_TITLE)
                )
            }

            CoreBodyText(
                text = stringResource(Res.string.code_sent_to, state.email),
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
                CoreCodeTextField(
                    value = state.code,
                    onValueChange = onCodeChanged,
                    label = { CoreBodyText(stringResource(Res.string.confirmation_code)) },
                    placeholder = { CoreBodyText(stringResource(Res.string.confirmation_code)) },
                    modifier = Modifier.testTag(ResetEmailPasswordTestTags.CODE_INPUT),
                    isError = state.actionError != null
                )

                Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))

                CoreOutlinedTextField(
                    value = state.newPassword,
                    onValueChange = onPasswordChanged,
                    modifier = Modifier.testTag(ResetEmailPasswordTestTags.NEW_PASSWORD_INPUT),
                    label = { CoreBodyText(stringResource(Res.string.new_password)) },
                    placeholder = { CoreBodyText(stringResource(Res.string.new_password)) },
                    enabled = !state.actionLoading,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                ErrorText(state.actionError, ResetEmailPasswordTestTags.RESET_STEP_ERROR_TEXT)

                Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))

                if (!state.canResendCode) {
                    CoreBodyText(
                        text = stringResource(Res.string.resend_code_timer, state.resendTimerSeconds),
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.testTag(ResetEmailPasswordTestTags.RESEND_TIMER_TEXT)
                    )
                } else {
                    CoreTextButton(
                        text = stringResource(Res.string.resend_code),
                        onClick = onResendCodeClick,
                        modifier = Modifier.testTag(ResetEmailPasswordTestTags.RESEND_CODE_BUTTON)
                    )
                }
            }

            CoreButton(
                text = stringResource(Res.string.confirm),
                onClick = onConfirmResetClick,
                modifier = Modifier.testTag(ResetEmailPasswordTestTags.CONFIRM_BUTTON),
                enabled = state.canConfirm
            )

            Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))

            CoreTextButton(
                text = stringResource(Res.string.change_email),
                onClick = onResetEmailClick,
                modifier = Modifier.testTag(ResetEmailPasswordTestTags.CHANGE_EMAIL_BUTTON)
            )
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
            CoreErrorText(
                text = it.toLocalizedMessage(),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = CoreTheme.dimens.paddingSmall)
                    .testTag(tag)
            )
        }
    }
}

@InternalApi
internal class ResetEmailPasswordPreviewProvider :
    PreviewParameterProvider<ResetEmailPasswordScreenState> {

    private val items: List<Pair<String, ResetEmailPasswordScreenState>> = listOf(
        "Email Step Input" to ResetEmailPasswordScreenState.EmailInput(
            email = "user@example.com",
            isEmailValid = true
        ),
        "Reset Step Input" to ResetEmailPasswordScreenState.ResetInput(
            email = "user@example.com",
            code = "123456",
            newPassword = "Password123!",
            isPasswordValid = true
        ),
        "Email Step Error" to ResetEmailPasswordScreenState.EmailInput(
            email = "wrong@example.com",
            actionError = CommonError.Unknown()
        ),
        "Reset Step Error" to ResetEmailPasswordScreenState.ResetInput(
            email = "user@example.com",
            code = "123456",
            actionError = CommonError.Unknown()
        ),
        "Resend Timer Active" to ResetEmailPasswordScreenState.ResetInput(
            email = "user@example.com",
            resendTimerSeconds = 42
        ),
        "Action Loading" to ResetEmailPasswordScreenState.ResetInput(
            email = "user@example.com",
            actionLoading = true
        ),
        "Fullscreen Loading" to ResetEmailPasswordScreenState.Loading
    )

    override val values: Sequence<ResetEmailPasswordScreenState> =
        items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? =
        items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun ResetEmailPasswordScreenPreviewContent(state: ResetEmailPasswordScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        ResetEmailPasswordScreen(
            component = ResetEmailPasswordComponentMock(initialState = state)
        )
    }
}

private val defaultResetEmailPasswordPreviewState = ResetEmailPasswordScreenState.EmailInput(
    email = "user@example.com",
    isEmailValid = true
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StatesPreview(
    @PreviewParameter(ResetEmailPasswordPreviewProvider::class) state: ResetEmailPasswordScreenState
) {
    DialogPreviewContainer {
        ResetEmailPasswordScreenPreviewContent(state = state)
    }
}

@InternalApi
@DialogSizePreviews
@Composable
private fun DialogSizePreview() {
    DialogPreviewContainer {
        ResetEmailPasswordScreenPreviewContent(state = defaultResetEmailPasswordPreviewState)
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    DialogPreviewContainer {
        ResetEmailPasswordScreenPreviewContent(state = defaultResetEmailPasswordPreviewState)
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    DialogPreviewContainer {
        ResetEmailPasswordScreenPreviewContent(state = defaultResetEmailPasswordPreviewState)
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
