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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreBackButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreTextButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input.CoreCodeTextField
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input.CoreOutlinedTextField
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreBodyText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreErrorText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreScreenTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreSmallText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.mock.ui.screen.auth.login.phone.LoginByPhoneComponentMock
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
                title = {
                    CoreScreenTitleText(
                        text = stringResource(Res.string.sign_in_with_phone)
                    )
                },
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
            CoreTitleText(
                text = stringResource(Res.string.enter_phone_number),
                modifier = Modifier.testTag(LoginByPhoneTestTags.PHONE_STEP_TITLE)
            )

            Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))

            CoreOutlinedTextField(
                value = state.phoneNumber,
                onValueChange = onPhoneChanged,
                label = { CoreBodyText(stringResource(Res.string.phone_number)) },
                placeholder = { CoreBodyText(stringResource(Res.string.phone_number)) },
                modifier = Modifier.testTag(LoginByPhoneTestTags.PHONE_INPUT),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                isError = state.actionError != null,
                enabled = !state.actionLoading
            )

            state.actionError?.let { error ->
                Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))
                CoreErrorText(
                    text = error.toLocalizedMessage(),
                    modifier = Modifier.testTag(LoginByPhoneTestTags.PHONE_STEP_ERROR_TEXT)
                )
            }
        }

        CoreButton(
            text = stringResource(Res.string.send_code),
            onClick = onSendCodeClick,
            modifier = Modifier.testTag(LoginByPhoneTestTags.SEND_CODE_BUTTON),
            enabled = state.canSendCode
        )
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
        CoreTitleText(
            text = stringResource(Res.string.enter_confirmation_code),
            modifier = Modifier.testTag(LoginByPhoneTestTags.CODE_STEP_TITLE)
        )

        CoreBodyText(
            text = stringResource(Res.string.code_sent_to, state.phoneNumber),
            modifier = Modifier.testTag(LoginByPhoneTestTags.CODE_SENT_INFO_TEXT)
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
                placeholder = { CoreBodyText(stringResource(Res.string.enter_confirmation_code)) },
                modifier = Modifier.testTag(LoginByPhoneTestTags.CODE_INPUT),
                isError = state.actionError != null,
                enabled = !state.actionLoading
            )

            state.actionError?.let { error ->
                Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))
                CoreErrorText(
                    text = error.toLocalizedMessage(),
                    modifier = Modifier.testTag(LoginByPhoneTestTags.CODE_STEP_ERROR_TEXT)
                )
            }

            Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))

            if (state.resendTimerSeconds > 0) {
                CoreSmallText(
                    text = stringResource(Res.string.resend_code_timer, state.resendTimerSeconds),
                    modifier = Modifier.testTag(LoginByPhoneTestTags.RESEND_TIMER_TEXT)
                )
            } else {
                CoreTextButton(
                    text = stringResource(Res.string.resend_code),
                    onClick = onResendClick,
                    modifier = Modifier.testTag(LoginByPhoneTestTags.RESEND_CODE_BUTTON),
                    enabled = state.canResendCode
                )
            }
        }

        CoreButton(
            text = stringResource(Res.string.confirm),
            onClick = onConfirmClick,
            modifier = Modifier.testTag(LoginByPhoneTestTags.CONFIRM_BUTTON),
            enabled = state.canConfirmCode
        )

        Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))

        CoreTextButton(
            text = stringResource(Res.string.change_phone_number),
            onClick = onChangePhoneClick,
            modifier = Modifier.testTag(LoginByPhoneTestTags.CHANGE_PHONE_BUTTON),
            enabled = !state.actionLoading
        )
    }
}

@InternalApi
internal class LoginByPhonePreviewProvider : PreviewParameterProvider<LoginByPhoneScreenState> {
    private val items: List<Pair<String, LoginByPhoneScreenState>> = listOf(
        "Phone Input" to LoginByPhoneScreenState.PhoneInput(
            phoneNumber = "+1234567890"
        ),
        "Phone Error" to LoginByPhoneScreenState.PhoneInput(
            phoneNumber = "+1234567890",
            actionError = CommonError.Unknown()
        ),
        "Code Input" to LoginByPhoneScreenState.CodeInput(
            phoneNumber = "+1234567890",
            code = "123456"
        ),
        "Code Timer Active" to LoginByPhoneScreenState.CodeInput(
            phoneNumber = "+1234567890",
            code = "",
            resendTimerSeconds = 30
        ),
        "Action Loading" to LoginByPhoneScreenState.PhoneInput(
            phoneNumber = "+1234567890",
            actionLoading = true
        )
    )

    override val values: Sequence<LoginByPhoneScreenState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun LoginByPhoneScreenPreviewContent(state: LoginByPhoneScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        Surface {
            LoginByPhoneScreen(
                component = LoginByPhoneComponentMock(initialState = state)
            )
        }
    }
}

private val defaultLoginByPhonePreviewState = LoginByPhoneScreenState.PhoneInput(
    phoneNumber = "+1234567890"
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StatesPreview(
    @PreviewParameter(LoginByPhonePreviewProvider::class) state: LoginByPhoneScreenState
) {
    ScreenPreviewContainer {
        LoginByPhoneScreenPreviewContent(state = state)
    }
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        LoginByPhoneScreenPreviewContent(state = defaultLoginByPhonePreviewState)
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        LoginByPhoneScreenPreviewContent(state = defaultLoginByPhonePreviewState)
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        LoginByPhoneScreenPreviewContent(state = defaultLoginByPhonePreviewState)
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
