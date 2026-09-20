package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.phone

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
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreBackButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.*
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginByPhoneScreen(component: LoginByPhoneComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.sign_in_with_phone)) },
                navigationIcon = {
                    CoreBackButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(LoginByPhoneTestTags.BACK_BUTTON)
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
            when (val s = state) {
                is LoginByPhoneScreenState.PhoneInput -> {
                    PhoneInputContent(s, component::onPhoneChanged, component::onSendCodeClick)
                }
                is LoginByPhoneScreenState.CodeInput -> {
                    CodeInputContent(
                        s,
                        component::onCodeChanged,
                        component::onConfirmCodeClick,
                        component::onSendCodeClick,
                        component::onResetPhoneClick
                    )
                }
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
private fun PhoneInputContent(
    state: LoginByPhoneScreenState.PhoneInput,
    onPhoneChanged: (String) -> Unit,
    onSendCodeClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(CoreTheme.dimens.paddingLarge)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.enter_phone_number),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.testTag(LoginByPhoneTestTags.PHONE_STEP_TITLE)
            )

            Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))

            OutlinedTextField(
                value = state.phoneNumber,
                onValueChange = onPhoneChanged,
                label = { Text(stringResource(Res.string.phone_number)) },
                placeholder = { Text(stringResource(Res.string.phone_number)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(LoginByPhoneTestTags.PHONE_INPUT),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                isError = state.actionError != null
            )

            state.actionError?.let { error ->
                Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))
                Text(
                    text = error.toLocalizedMessage(),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.testTag(LoginByPhoneTestTags.PHONE_STEP_ERROR_TEXT)
                )
            }
        }

        Button(
            onClick = onSendCodeClick,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(LoginByPhoneTestTags.SEND_CODE_BUTTON),
            enabled = state.canSendCode
        ) {
            Text(stringResource(Res.string.send_code))
        }
    }
}

@Composable
private fun CodeInputContent(
    state: LoginByPhoneScreenState.CodeInput,
    onCodeChanged: (String) -> Unit,
    onConfirmClick: () -> Unit,
    onResendClick: () -> Unit,
    onChangePhoneClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(CoreTheme.dimens.paddingLarge)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.enter_confirmation_code),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.testTag(LoginByPhoneTestTags.CODE_STEP_TITLE)
        )

        Text(
            text = stringResource(Res.string.code_sent_to, state.phoneNumber),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.testTag(LoginByPhoneTestTags.CODE_SENT_INFO_TEXT)
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
                label = { Text(stringResource(Res.string.confirmation_code)) },
                placeholder = { Text(stringResource(Res.string.enter_confirmation_code)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(LoginByPhoneTestTags.CODE_INPUT),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = state.actionError != null
            )

            state.actionError?.let { error ->
                Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))
                Text(
                    text = error.toLocalizedMessage(),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.testTag(LoginByPhoneTestTags.CODE_STEP_ERROR_TEXT)
                )
            }

            Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))

            if (state.resendTimerSeconds > 0) {
                Text(
                    text = stringResource(Res.string.resend_code_timer, state.resendTimerSeconds),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.testTag(LoginByPhoneTestTags.RESEND_TIMER_TEXT)
                )
            } else {
                TextButton(
                    onClick = onResendClick,
                    modifier = Modifier.testTag(LoginByPhoneTestTags.RESEND_CODE_BUTTON),
                    enabled = state.canResendCode
                ) {
                    Text(stringResource(Res.string.resend_code))
                }
            }
        }

        Button(
            onClick = onConfirmClick,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(LoginByPhoneTestTags.CONFIRM_BUTTON),
            enabled = state.canConfirmCode
        ) {
            Text(stringResource(Res.string.confirm))
        }

        Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))

        TextButton(
            onClick = onChangePhoneClick,
            modifier = Modifier.testTag(LoginByPhoneTestTags.CHANGE_PHONE_BUTTON),
            enabled = !state.actionLoading
        ) {
            Text(stringResource(Res.string.change_phone_number))
        }
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun LoginByPhoneScreenPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
            Surface {
                PhoneInputContent(
                    state = LoginByPhoneScreenState.PhoneInput(phoneNumber = "+1234567890"),
                    onPhoneChanged = {},
                    onSendCodeClick = {}
                )
            }
        }
    }
}

internal object LoginByPhoneTestTags {
    const val BACK_BUTTON = "LoginByPhone_BackButton"

    const val PHONE_STEP_TITLE = "LoginByPhone_PhoneStepTitle"
    const val PHONE_INPUT = "LoginByPhone_PhoneInput"
    const val SEND_CODE_BUTTON = "LoginByPhone_SendCodeButton"
    const val PHONE_STEP_ERROR_TEXT = "LoginByPhone_PhoneStepErrorText"

    const val CODE_STEP_TITLE = "LoginByPhone_CodeStepTitle"
    const val CODE_SENT_INFO_TEXT = "LoginByPhone_CodeSentInfoText"
    const val CODE_INPUT = "LoginByPhone_CodeInput"
    const val CONFIRM_BUTTON = "LoginByPhone_ConfirmButton"
    const val CHANGE_PHONE_BUTTON = "LoginByPhone_ChangePhoneButton"
    const val RESEND_TIMER_TEXT = "LoginByPhone_ResendTimerText"
    const val RESEND_CODE_BUTTON = "LoginByPhone_ResendCodeButton"
    const val CODE_STEP_ERROR_TEXT = "LoginByPhone_CodeStepErrorText"
}
