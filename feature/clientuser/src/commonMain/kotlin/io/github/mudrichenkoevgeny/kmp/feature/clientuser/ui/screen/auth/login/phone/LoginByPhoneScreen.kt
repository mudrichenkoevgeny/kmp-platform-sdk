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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.arkivanov.decompose.extensions.compose.stack.Children
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
import io.github.mudrichenkoevgeny.kmp.feature.user.localization.LocalUserStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginByPhoneScreen(component: LoginByPhoneComponent) {
    val state by component.state.subscribeAsState()
    val userUi = LocalUserStrings.current.ui

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    CoreScreenTitleText(
                        text = userUi.signInWithPhone
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
            Children(
                stack = component.stack,
                modifier = Modifier.fillMaxSize()
            ) { child ->
                when (val activeChild = child.instance) {
                    is LoginByPhoneComponent.Child.PhoneInput -> {
                        PhoneInputContent(
                            state = state,
                            onPhoneChanged = component::onPhoneChanged,
                            onSendCodeClick = component::onSendCodeClick
                        )
                    }
                    is LoginByPhoneComponent.Child.CodeInput -> {
                        CodeInputContent(
                            state = state,
                            phone = activeChild.phone,
                            onCodeChanged = component::onCodeChanged,
                            onConfirmClick = component::onConfirmCodeClick,
                            onResendClick = component::onSendCodeClick,
                            onChangePhoneClick = component::onResetPhoneClick
                        )
                    }
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
    state: LoginByPhoneScreenState,
    onPhoneChanged: (String) -> Unit,
    onSendCodeClick: () -> Unit
) {
    val userUi = LocalUserStrings.current.ui
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
                text = userUi.enterPhoneNumber,
                modifier = Modifier.testTag(LoginByPhoneTestTags.PHONE_STEP_TITLE)
            )

            Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))

            CoreOutlinedTextField(
                value = state.phoneNumber,
                onValueChange = onPhoneChanged,
                label = { CoreBodyText(userUi.phoneNumber) },
                placeholder = { CoreBodyText(userUi.phoneNumber) },
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
            text = userUi.sendCode,
            onClick = onSendCodeClick,
            modifier = Modifier.testTag(LoginByPhoneTestTags.SEND_CODE_BUTTON),
            enabled = state.canSendCode
        )
    }
}

@Composable
private fun CodeInputContent(
    state: LoginByPhoneScreenState,
    phone: String,
    onCodeChanged: (String) -> Unit,
    onConfirmClick: () -> Unit,
    onResendClick: () -> Unit,
    onChangePhoneClick: () -> Unit
) {
    val userUi = LocalUserStrings.current.ui
    Column(
        modifier = Modifier
            .padding(CoreTheme.dimens.paddingLarge)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CoreTitleText(
            text = userUi.enterConfirmationCode,
            modifier = Modifier.testTag(LoginByPhoneTestTags.CODE_STEP_TITLE)
        )

        CoreBodyText(
            text = userUi.codeSentTo(phone),
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
                label = { CoreBodyText(userUi.confirmationCode) },
                placeholder = { CoreBodyText(userUi.enterConfirmationCode) },
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
                    text = userUi.resendCodeTimer(state.resendTimerSeconds),
                    modifier = Modifier.testTag(LoginByPhoneTestTags.RESEND_TIMER_TEXT)
                )
            } else {
                CoreTextButton(
                    text = userUi.resendCode,
                    onClick = onResendClick,
                    modifier = Modifier.testTag(LoginByPhoneTestTags.RESEND_CODE_BUTTON),
                    enabled = state.canResendCode
                )
            }
        }

        CoreButton(
            text = userUi.confirm,
            onClick = onConfirmClick,
            modifier = Modifier.testTag(LoginByPhoneTestTags.CONFIRM_BUTTON),
            enabled = state.canConfirmCode
        )

        Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))

        CoreTextButton(
            text = userUi.changePhoneNumber,
            onClick = onChangePhoneClick,
            modifier = Modifier.testTag(LoginByPhoneTestTags.CHANGE_PHONE_BUTTON),
            enabled = !state.actionLoading
        )
    }
}

@InternalApi
internal class LoginByPhonePreviewProvider : PreviewParameterProvider<LoginByPhoneScreenState> {
    private val items: List<Pair<String, LoginByPhoneScreenState>> = listOf(
        "Phone Input" to LoginByPhoneScreenState(
            phoneNumber = "+1234567890"
        ),
        "Phone Error" to LoginByPhoneScreenState(
            phoneNumber = "+1234567890",
            actionError = CommonError.Unknown()
        ),
        "Code Input" to LoginByPhoneScreenState(
            phoneNumber = "+1234567890",
            code = "123456"
        ),
        "Code Timer Active" to LoginByPhoneScreenState(
            phoneNumber = "+1234567890",
            code = "",
            resendTimerSeconds = 30
        ),
        "Action Loading" to LoginByPhoneScreenState(
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

private val defaultLoginByPhonePreviewState = LoginByPhoneScreenState(
    phoneNumber = "+1234567890"
)

@InternalApi
@ScreenSizePreviews
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
