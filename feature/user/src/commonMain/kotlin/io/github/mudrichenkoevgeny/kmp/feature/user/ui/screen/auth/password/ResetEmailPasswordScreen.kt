package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.password

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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenOverlayLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun ResetEmailPasswordScreen(component: ResetEmailPasswordComponent) {
    val state by component.state.subscribeAsState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
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
                    text = stringResource(Res.string.reset_password),
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
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
                modifier = Modifier.padding(top = Dimens.paddingSmall)
            )

            Spacer(Modifier.height(Dimens.paddingLarge))

            OutlinedTextField(
                value = state.code,
                onValueChange = onCodeChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(Res.string.confirmation_code)) },
                enabled = !state.actionLoading,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
            )

            Spacer(Modifier.height(Dimens.paddingMedium))

            OutlinedTextField(
                value = state.newPassword,
                onValueChange = onPasswordChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(Res.string.new_password)) },
                enabled = !state.actionLoading,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
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
                onClick = onConfirmResetClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = state.canConfirm
            ) {
                Text(stringResource(Res.string.confirm))
            }

            TextButton(onClick = onResetEmailClick) {
                Text(text = stringResource(Res.string.change_email))
            }
        }
        if (state.actionLoading) FullscreenOverlayLoading()
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