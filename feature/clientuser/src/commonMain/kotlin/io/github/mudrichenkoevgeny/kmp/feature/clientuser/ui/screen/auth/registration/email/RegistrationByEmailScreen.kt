package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreTextButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input.CoreCodeTextField
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input.CoreEmailTextField
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input.CorePasswordTextField
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
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.mock.ui.screen.auth.registration.email.RegistrationByEmailComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.*
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationByEmailScreen(component: RegistrationByEmailComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    CoreScreenTitleText(
                        text = stringResource(Res.string.registration_by_email),
                        modifier = Modifier.testTag(RegistrationByEmailTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    CoreBackButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(RegistrationByEmailTestTags.BACK_BUTTON)
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
                is RegistrationByEmailScreenState.EmailInput -> {
                    EmailInputContent(s, component::onEmailChanged, component::onSendCodeClick)
                }
                is RegistrationByEmailScreenState.RegistrationInput -> {
                    RegistrationInputContent(
                        s,
                        component::onCodeChanged,
                        component::onPasswordChanged,
                        component::onTogglePasswordVisibility,
                        component::onRegisterClick,
                        component::onSendCodeClick
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
private fun EmailInputContent(
    state: RegistrationByEmailScreenState.EmailInput,
    onEmailChanged: (String) -> Unit,
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
            CoreEmailTextField(
                value = state.email,
                onValueChange = onEmailChanged,
                modifier = Modifier.testTag(RegistrationByEmailTestTags.EMAIL_INPUT),
                isError = state.actionError != null
            )

            state.actionError?.let { error ->
                CoreErrorText(
                    text = error.toLocalizedMessage(),
                    modifier = Modifier.testTag(RegistrationByEmailTestTags.EMAIL_STEP_ERROR_TEXT)
                )
            }
        }

        CoreButton(
            text = stringResource(Res.string.send_code),
            onClick = onSendCodeClick,
            modifier = Modifier.testTag(RegistrationByEmailTestTags.SEND_CODE_BUTTON),
            enabled = state.canSendCode
        )
    }
}

@Composable
private fun RegistrationInputContent(
    state: RegistrationByEmailScreenState.RegistrationInput,
    onCodeChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onRegisterClick: () -> Unit,
    onResendClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(CoreTheme.dimens.paddingLarge)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CoreTitleText(
            text = stringResource(Res.string.enter_confirmation_code),
            modifier = Modifier.testTag(RegistrationByEmailTestTags.CODE_STEP_TITLE)
        )

        CoreBodyText(
            text = stringResource(Res.string.code_sent_to, state.email),
            modifier = Modifier.testTag(RegistrationByEmailTestTags.CODE_SENT_INFO_TEXT)
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
                modifier = Modifier.testTag(RegistrationByEmailTestTags.CODE_INPUT),
                isError = state.actionError != null
            )

            Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))

            CorePasswordTextField(
                value = state.password,
                onValueChange = onPasswordChanged,
                isPasswordVisible = state.isPasswordVisible,
                onTogglePasswordVisibility = onTogglePasswordVisibility,
                label = { CoreBodyText(stringResource(Res.string.password)) },
                placeholder = { CoreBodyText(stringResource(Res.string.password)) },
                modifier = Modifier.testTag(RegistrationByEmailTestTags.PASSWORD_INPUT),
                isError = (!state.isPasswordValid) || (state.actionError != null),
                toggleModifier = Modifier.testTag(RegistrationByEmailTestTags.TOGGLE_PASSWORD_VISIBILITY_BUTTON)
            )

            state.actionError?.let { error ->
                CoreErrorText(
                    text = error.toLocalizedMessage(),
                    modifier = Modifier.testTag(RegistrationByEmailTestTags.REGISTRATION_STEP_ERROR_TEXT)
                )
            }

            Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))

            if (state.resendTimerSeconds > 0) {
                CoreSmallText(
                    text = stringResource(Res.string.resend_code_timer, state.resendTimerSeconds),
                    modifier = Modifier.testTag(RegistrationByEmailTestTags.RESEND_TIMER_TEXT)
                )
            } else {
                CoreTextButton(
                    text = stringResource(Res.string.resend_code),
                    onClick = onResendClick,
                    modifier = Modifier.testTag(RegistrationByEmailTestTags.RESEND_CODE_BUTTON),
                    enabled = state.canResendCode
                )
            }
        }

        CoreButton(
            text = stringResource(Res.string.register),
            onClick = onRegisterClick,
            modifier = Modifier.testTag(RegistrationByEmailTestTags.REGISTER_BUTTON),
            enabled = state.canRegister
        )
    }
}

internal object RegistrationByEmailTestTags {
    const val BACK_BUTTON = "RegistrationByEmail_BackButton"
    const val TITLE = "RegistrationByEmail_Title"
    const val EMAIL_INPUT = "RegistrationByEmail_EmailInput"
    const val SEND_CODE_BUTTON = "RegistrationByEmail_SendCodeButton"
    const val EMAIL_STEP_ERROR_TEXT = "RegistrationByEmail_EmailStepErrorText"
    const val CODE_STEP_TITLE = "RegistrationByEmail_CodeStepTitle"
    const val CODE_SENT_INFO_TEXT = "RegistrationByEmail_CodeSentInfoText"
    const val CODE_INPUT = "RegistrationByEmail_CodeInput"
    const val PASSWORD_INPUT = "RegistrationByEmail_PasswordInput"
    const val TOGGLE_PASSWORD_VISIBILITY_BUTTON = "RegistrationByEmail_TogglePasswordVisibilityButton"
    const val REGISTER_BUTTON = "RegistrationByEmail_RegisterButton"
    const val RESEND_TIMER_TEXT = "RegistrationByEmail_ResendTimerText"
    const val RESEND_CODE_BUTTON = "RegistrationByEmail_ResendCodeButton"
    const val REGISTRATION_STEP_ERROR_TEXT = "RegistrationByEmail_RegistrationStepErrorText"
}

@InternalApi
internal class RegistrationByEmailPreviewProvider :
    PreviewParameterProvider<RegistrationByEmailScreenState> {

    private val items: List<Pair<String, RegistrationByEmailScreenState>> = listOf(
        "Email Input" to RegistrationByEmailScreenState.EmailInput(
            email = "user@example.com"
        ),
        "Email Error" to RegistrationByEmailScreenState.EmailInput(
            email = "invalid-email",
            actionError = CommonError.Unknown()
        ),
        "Code & Password Input" to RegistrationByEmailScreenState.RegistrationInput(
            email = "user@example.com",
            code = "123456",
            password = "SecretPassword123!"
        ),
        "Code Resend Timer" to RegistrationByEmailScreenState.RegistrationInput(
            email = "user@example.com",
            code = "",
            password = "",
            resendTimerSeconds = 45
        ),
        "Action Loading" to RegistrationByEmailScreenState.EmailInput(
            email = "user@example.com",
            actionLoading = true
        ),
        "Long Text Overflow" to RegistrationByEmailScreenState.RegistrationInput(
            email = "this_is_an_extremely_long_email_address_for_testing_overflow_behavior@example.domain.com",
            code = "",
            password = "",
            resendTimerSeconds = 120
        )
    )

    override val values: Sequence<RegistrationByEmailScreenState> =
        items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? =
        items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun RegistrationByEmailScreenPreviewContent(state: RegistrationByEmailScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        Surface {
            RegistrationByEmailScreen(
                component = RegistrationByEmailComponentMock(initialState = state)
            )
        }
    }
}

private val defaultPreviewState = RegistrationByEmailScreenState.RegistrationInput(
    email = "user@example.com",
    code = "123",
    password = "pass"
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StatesPreview(
    @PreviewParameter(RegistrationByEmailPreviewProvider::class) state: RegistrationByEmailScreenState
) {
    ScreenPreviewContainer {
        RegistrationByEmailScreenPreviewContent(state = state)
    }
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun AdaptivePreview() {
    ScreenPreviewContainer {
        RegistrationByEmailScreenPreviewContent(state = defaultPreviewState)
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        RegistrationByEmailScreenPreviewContent(state = defaultPreviewState)
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        RegistrationByEmailScreenPreviewContent(state = defaultPreviewState)
    }
}
