package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.registration.email

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.MockAppErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenOverlayLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.*
import org.jetbrains.compose.resources.stringResource

/**
 * Registration UI: email + send code, then code + password with resend and change-email actions.
 *
 * @param component Decompose component providing state and callbacks.
 */
@Composable
fun RegistrationByEmailScreen(component: RegistrationByEmailComponent) {
    val state by component.state.subscribeAsState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val currentState = state) {
            is RegistrationByEmailScreenState.Loading -> {
                FullscreenLoading()
            }
            is RegistrationByEmailScreenState.EmailInput -> {
                EmailInputContent(
                    state = currentState,
                    onEmailChanged = component::onEmailChanged,
                    onSendCodeClick = component::onSendCodeClick,
                    onBackClick = component::onBackClick
                )
            }
            is RegistrationByEmailScreenState.RegistrationInput -> {
                RegistrationInputContent(
                    state = currentState,
                    onCodeChanged = component::onCodeChanged,
                    onPasswordChanged = component::onPasswordChanged,
                    onTogglePasswordVisibility = component::onTogglePasswordVisibility,
                    onRegisterClick = component::onRegisterClick,
                    onResendCodeClick = component::onSendCodeClick,
                    onBackClick = component::onBackClick
                )
            }
        }
    }
}

@Composable
private fun EmailInputContent(
    state: RegistrationByEmailScreenState.EmailInput,
    onEmailChanged: (String) -> Unit,
    onSendCodeClick: () -> Unit,
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
                    modifier = Modifier.align(Alignment.CenterStart),
                    enabled = !state.actionLoading
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }

                Text(
                    text = stringResource(Res.string.registration_by_email),
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            Spacer(Modifier.height(Dimens.paddingLarge))

            OutlinedTextField(
                value = state.email,
                onValueChange = onEmailChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(Res.string.email)) },
                isError = state.actionError != null,
                enabled = !state.actionLoading,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )

            ErrorText(state.actionError)

            Spacer(Modifier.height(Dimens.paddingLarge))

            Button(
                onClick = onSendCodeClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isEmailValid && !state.actionLoading
            ) {
                Text(stringResource(Res.string.send_code))
            }
        }

        if (state.actionLoading) {
            FullscreenOverlayLoading()
        }
    }
}

@Composable
private fun RegistrationInputContent(
    state: RegistrationByEmailScreenState.RegistrationInput,
    onCodeChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onRegisterClick: () -> Unit,
    onResendCodeClick: () -> Unit,
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
                    modifier = Modifier.align(Alignment.CenterStart),
                    enabled = !state.actionLoading
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }

                Text(
                    text = stringResource(Res.string.enter_confirmation_code),
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            Text(
                text = stringResource(Res.string.code_sent_to, state.email),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(top = Dimens.paddingSmall),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(Dimens.paddingLarge))

            OutlinedTextField(
                value = state.code,
                onValueChange = onCodeChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(Res.string.confirmation_code)) },
                enabled = !state.actionLoading,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                singleLine = true
            )

            Spacer(Modifier.height(Dimens.paddingMedium))

            OutlinedTextField(
                value = state.password,
                onValueChange = onPasswordChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(Res.string.password)) },
                isError = state.actionError != null,
                enabled = !state.actionLoading,
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = onTogglePasswordVisibility) {
                        val icon = if (state.isPasswordVisible) Icons.Default.Check else Icons.Default.Info
                        Icon(imageVector = icon, contentDescription = null)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )

            ErrorText(state.actionError)

            Spacer(Modifier.height(Dimens.paddingMedium))

            if (!state.canResendCode) {
                Text(
                    text = stringResource(Res.string.resend_code_timer, state.resendTimerSeconds),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            } else {
                TextButton(onClick = onResendCodeClick) {
                    Text(text = stringResource(Res.string.resend_code))
                }
            }

            Spacer(Modifier.height(Dimens.paddingLarge))

            Button(
                onClick = onRegisterClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = state.canRegister
            ) {
                Text(stringResource(Res.string.register))
            }

            TextButton(
                onClick = onBackClick,
                enabled = !state.actionLoading
            ) {
                Text(
                    text = stringResource(Res.string.change_email),
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
                modifier = Modifier.padding(top = Dimens.paddingSmall)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegistrationEmailInputPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides MockAppErrorParser) {
            Surface {
                EmailInputContent(
                    state = RegistrationByEmailScreenState.EmailInput(
                        email = "test@example.com",
                        isEmailValid = true
                    ),
                    onEmailChanged = {},
                    onSendCodeClick = {},
                    onBackClick = {}
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegistrationInputPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides MockAppErrorParser) {
            Surface {
                RegistrationInputContent(
                    state = RegistrationByEmailScreenState.RegistrationInput(
                        email = "test@example.com",
                        code = "123",
                        resendTimerSeconds = 59
                    ),
                    onCodeChanged = {},
                    onPasswordChanged = {},
                    onTogglePasswordVisibility = {},
                    onRegisterClick = {},
                    onResendCodeClick = {},
                    onBackClick = {}
                )
            }
        }
    }
}