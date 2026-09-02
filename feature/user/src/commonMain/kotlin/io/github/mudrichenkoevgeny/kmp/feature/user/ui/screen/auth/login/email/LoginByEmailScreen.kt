package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
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
                        .testTag(LoginByEmailTestTags.BACK_BUTTON),
                    enabled = !state.actionLoading
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }

                Text(
                    text = stringResource(Res.string.login_by_email),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.testTag(LoginByEmailTestTags.TITLE)
                )
            }

            Spacer(Modifier.height(Dimens.paddingLarge))

            OutlinedTextField(
                value = state.email,
                onValueChange = onEmailChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(LoginByEmailTestTags.EMAIL_INPUT),
                label = { Text(stringResource(Res.string.email)) },
                isError = state.actionError != null,
                enabled = !state.actionLoading,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )

            Spacer(Modifier.height(Dimens.paddingMedium))

            OutlinedTextField(
                value = state.password,
                onValueChange = onPasswordChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(LoginByEmailTestTags.PASSWORD_INPUT),
                label = { Text(stringResource(Res.string.password)) },
                isError = state.actionError != null,
                enabled = !state.actionLoading,
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = onTogglePasswordVisibility,
                        modifier = Modifier.testTag(LoginByEmailTestTags.TOGGLE_PASSWORD_VISIBILITY_BUTTON)
                    ) {
                        val icon = if (state.isPasswordVisible) Icons.Default.Check else Icons.Default.Info
                        Icon(imageVector = icon, contentDescription = null)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )

            ErrorText(state.actionError)

            TextButton(
                onClick = onForgotPasswordClick,
                modifier = Modifier
                    .align(Alignment.End)
                    .testTag(LoginByEmailTestTags.FORGOT_PASSWORD_BUTTON),
                enabled = !state.actionLoading
            ) {
                Text(
                    text = stringResource(Res.string.forgot_password),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(Modifier.height(Dimens.paddingLarge))

            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(LoginByEmailTestTags.LOGIN_BUTTON),
                enabled = state.canLogin
            ) {
                Text(stringResource(Res.string.login))
            }

            Spacer(Modifier.height(Dimens.paddingMedium))

            if (state.isRegistrationAvailable) {
                TextButton(
                    onClick = onRegistrationClick,
                    modifier = Modifier.testTag(LoginByEmailTestTags.REGISTRATION_BUTTON),
                    enabled = !state.actionLoading
                ) {
                    Text(
                        text = stringResource(Res.string.no_account_register),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
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
                    .testTag(LoginByEmailTestTags.ERROR_TEXT)
            )
        }
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun LoginByEmailContentPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
            Surface {
                LoginByEmailContent(
                    state = LoginByEmailScreenState.Content(
                        email = "test@example.com",
                        isEmailValid = true,
                        isRegistrationAvailable = true
                    ),
                    onEmailChanged = {},
                    onPasswordChanged = {},
                    onTogglePasswordVisibility = {},
                    onLoginClick = {},
                    onForgotPasswordClick = {},
                    onRegistrationClick = {},
                    onBackClick = {}
                )
            }
        }
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun LoginByEmailContentLoadingPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
            Surface {
                LoginByEmailContent(
                    state = LoginByEmailScreenState.Content(
                        email = "test@example.com",
                        actionLoading = true
                    ),
                    onEmailChanged = {},
                    onPasswordChanged = {},
                    onTogglePasswordVisibility = {},
                    onLoginClick = {},
                    onForgotPasswordClick = {},
                    onRegistrationClick = {},
                    onBackClick = {}
                )
            }
        }
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun LoginByEmailContentErrorPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
            Surface {
                LoginByEmailContent(
                    state = LoginByEmailScreenState.Content(
                        email = "wrong@email.com",
                        actionError = CommonError.Unknown()
                    ),
                    onEmailChanged = {},
                    onPasswordChanged = {},
                    onTogglePasswordVisibility = {},
                    onLoginClick = {},
                    onForgotPasswordClick = {},
                    onRegistrationClick = {},
                    onBackClick = {}
                )
            }
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