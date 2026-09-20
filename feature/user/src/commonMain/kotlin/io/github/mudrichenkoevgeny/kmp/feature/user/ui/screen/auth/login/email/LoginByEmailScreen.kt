package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
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
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input.CoreEmailTextField
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input.CorePasswordTextField
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
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.login.email.LoginByEmailComponentMock
import org.jetbrains.compose.resources.stringResource

/**
 * Email login form with back navigation, forgot-password and registration links, and loading overlay on submit.
 *
 * @param component Decompose component providing state and callbacks.
 */
@Composable
fun LoginByEmailScreen(component: LoginByEmailComponent) {
    val state by component.state.subscribeAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val currentState = state) {
            is LoginByEmailScreenState.Loading -> {
                FullscreenLoading()
            }
            is LoginByEmailScreenState.Content -> {
                LoginByEmailContent(
                    state = currentState,
                    onEmailChanged = component::onEmailChanged,
                    onPasswordChanged = component::onPasswordChanged,
                    onTogglePasswordVisibility = component::onTogglePasswordVisibility,
                    onLoginClick = component::onLoginClick,
                    onForgotPasswordClick = component::onForgotPasswordClick,
                    onRegistrationClick = component::onRegistrationClick,
                    onBackClick = component::onBackClick
                )
            }
        }
    }
}

@Composable
private fun LoginByEmailContent(
    state: LoginByEmailScreenState.Content,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onRegistrationClick: () -> Unit,
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
                        .testTag(LoginByEmailTestTags.BACK_BUTTON),
                    enabled = !state.actionLoading
                )

                CoreScreenTitleText(
                    text = stringResource(Res.string.login_by_email),
                    modifier = Modifier.testTag(LoginByEmailTestTags.TITLE)
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
                    modifier = Modifier.testTag(LoginByEmailTestTags.EMAIL_INPUT),
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
                    modifier = Modifier.testTag(LoginByEmailTestTags.PASSWORD_INPUT),
                    isError = state.actionError != null,
                    toggleModifier = Modifier.testTag(LoginByEmailTestTags.TOGGLE_PASSWORD_VISIBILITY_BUTTON)
                )

                ErrorText(state.actionError)

                CoreTextButton(
                    text = stringResource(Res.string.forgot_password),
                    onClick = onForgotPasswordClick,
                    modifier = Modifier
                        .align(Alignment.End)
                        .testTag(LoginByEmailTestTags.FORGOT_PASSWORD_BUTTON),
                    enabled = !state.actionLoading
                )
            }

            CoreButton(
                text = stringResource(Res.string.login),
                onClick = onLoginClick,
                modifier = Modifier.testTag(LoginByEmailTestTags.LOGIN_BUTTON),
                enabled = state.canLogin
            )

            if (state.isRegistrationAvailable) {
                Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))

                CoreTextButton(
                    text = stringResource(Res.string.no_account_register),
                    onClick = onRegistrationClick,
                    modifier = Modifier.testTag(LoginByEmailTestTags.REGISTRATION_BUTTON),
                    enabled = !state.actionLoading
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
            CoreErrorText(
                text = it.toLocalizedMessage(),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = CoreTheme.dimens.paddingSmall)
                    .testTag(LoginByEmailTestTags.ERROR_TEXT)
            )
        }
    }
}

internal object LoginByEmailTestTags {
    const val BACK_BUTTON = "LoginByEmail_BackButton"
    const val TITLE = "LoginByEmail_Title"
    const val EMAIL_INPUT = "LoginByEmail_EmailInput"
    const val PASSWORD_INPUT = "LoginByEmail_PasswordInput"
    const val TOGGLE_PASSWORD_VISIBILITY_BUTTON = "LoginByEmail_TogglePasswordVisibilityButton"
    const val FORGOT_PASSWORD_BUTTON = "LoginByEmail_ForgotPasswordButton"
    const val LOGIN_BUTTON = "LoginByEmail_LoginButton"
    const val REGISTRATION_BUTTON = "LoginByEmail_RegistrationButton"
    const val ERROR_TEXT = "LoginByEmail_ErrorText"
}

@InternalApi
internal class LoginByEmailPreviewProvider :
    PreviewParameterProvider<LoginByEmailScreenState> {

    private val items: List<Pair<String, LoginByEmailScreenState>> = listOf(
        "Default Content" to LoginByEmailScreenState.Content(),
        "Filled Content" to LoginByEmailScreenState.Content(
            email = "user@example.com",
            isEmailValid = true,
            password = "SecretPassword123!",
            isPasswordValid = true,
            isRegistrationAvailable = true
        ),
        "Inline Error" to LoginByEmailScreenState.Content(
            email = "wrong@example.com",
            actionError = CommonError.Unknown(),
            isRegistrationAvailable = true
        ),
        "Action Loading" to LoginByEmailScreenState.Content(
            email = "user@example.com",
            actionLoading = true
        ),
        "Fullscreen Loading" to LoginByEmailScreenState.Loading,
        "Long Text Overflow" to LoginByEmailScreenState.Content(
            email = "this_is_an_extremely_long_email_address_for_testing_overflow_behavior@example.domain.com",
            isRegistrationAvailable = true
        )
    )

    override val values: Sequence<LoginByEmailScreenState> =
        items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? =
        items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun LoginByEmailScreenPreviewContent(state: LoginByEmailScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        LoginByEmailScreen(
            component = LoginByEmailComponentMock(initialState = state)
        )
    }
}

private val defaultPreviewState = LoginByEmailScreenState.Content(
    email = "user@example.com",
    isEmailValid = true,
    password = "SecretPassword123!",
    isPasswordValid = true,
    isRegistrationAvailable = true
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StatesPreview(
    @PreviewParameter(LoginByEmailPreviewProvider::class) state: LoginByEmailScreenState
) {
    DialogPreviewContainer {
        LoginByEmailScreenPreviewContent(state = state)
    }
}

@InternalApi
@DialogSizePreviews
@Composable
private fun DialogSizePreview() {
    DialogPreviewContainer {
        LoginByEmailScreenPreviewContent(state = defaultPreviewState)
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    DialogPreviewContainer {
        LoginByEmailScreenPreviewContent(state = defaultPreviewState)
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    DialogPreviewContainer {
        LoginByEmailScreenPreviewContent(state = defaultPreviewState)
    }
}
