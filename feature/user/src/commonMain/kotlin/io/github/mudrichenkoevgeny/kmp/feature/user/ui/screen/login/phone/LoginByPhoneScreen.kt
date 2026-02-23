package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.login.phone

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.MockAppErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenOverlayLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginByPhoneScreen(component: LoginByPhoneComponent) {
    val state by component.state.subscribeAsState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val currentState = state) {
            is LoginByPhoneScreenState.Loading -> {
                FullscreenLoading()
            }
            is LoginByPhoneScreenState.PhoneInput -> {
                PhoneInputContent(
                    state = currentState,
                    onPhoneChanged = component::onPhoneChanged,
                    onSendCodeClick = component::onSendCodeClick
                )
            }
            is LoginByPhoneScreenState.CodeInput -> {
                CodeInputContent(
                    state = currentState,
                    onCodeChanged = component::onCodeChanged,
                    onConfirmCodeClick = component::onConfirmCodeClick,
                    onResetPhoneClick = component::onResetPhoneClick
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
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.paddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.enter_phone_number),
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(Dimens.paddingLarge))

            OutlinedTextField(
                value = state.phoneNumber,
                onValueChange = onPhoneChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(Res.string.phone_number)) },
                isError = state.actionError != null,
                enabled = !state.actionLoading,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            ErrorText(state.actionError)

            Spacer(Modifier.height(Dimens.paddingLarge))

            Button(
                onClick = onSendCodeClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isPhoneNumberValid && !state.actionLoading
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
private fun CodeInputContent(
    state: LoginByPhoneScreenState.CodeInput,
    onCodeChanged: (String) -> Unit,
    onConfirmCodeClick: () -> Unit,
    onResetPhoneClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.paddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.enter_confirmation_code),
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = stringResource(Res.string.code_sent_to, state.phoneNumber),
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

            ErrorText(state.actionError)

            Spacer(Modifier.height(Dimens.paddingMedium))

            if (!state.canResendCode) {
                Text(
                    text = stringResource(Res.string.resend_code_timer, state.resendTimerSeconds),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            } else {
                TextButton(onClick = onResetPhoneClick) {
                    Text(text = stringResource(Res.string.resend_code))
                }
            }

            Spacer(Modifier.height(Dimens.paddingLarge))

            Button(
                onClick = onConfirmCodeClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isCodeFullLength && !state.actionLoading
            ) {
                Text(stringResource(Res.string.confirm))
            }

            TextButton(onClick = onResetPhoneClick) {
                Text(text = stringResource(Res.string.change_phone_number))
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
private fun LoginByPhonePhoneInputPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides MockAppErrorParser) {
            Surface {
                PhoneInputContent(
                    state = LoginByPhoneScreenState.PhoneInput(
                        phoneNumber = "9991234567",
                        isPhoneNumberValid = true
                    ),
                    onPhoneChanged = {},
                    onSendCodeClick = {}
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginByPhonePhoneInputLoadingPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides MockAppErrorParser) {
            Surface {
                PhoneInputContent(
                    state = LoginByPhoneScreenState.PhoneInput(
                        phoneNumber = "9991234567",
                        isPhoneNumberValid = true,
                        actionLoading = true
                    ),
                    onPhoneChanged = {},
                    onSendCodeClick = {}
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginByPhoneCodeInputTimerPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides MockAppErrorParser) {
            Surface {
                CodeInputContent(
                    state = LoginByPhoneScreenState.CodeInput(
                        phoneNumber = "9991234567",
                        code = "123",
                        resendTimerSeconds = 45
                    ),
                    onCodeChanged = {},
                    onConfirmCodeClick = {},
                    onResetPhoneClick = {}
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginByPhoneCodeInputResendReadyPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides MockAppErrorParser) {
            Surface {
                CodeInputContent(
                    state = LoginByPhoneScreenState.CodeInput(
                        phoneNumber = "9991234567",
                        code = "123456",
                        resendTimerSeconds = 0
                    ),
                    onCodeChanged = {},
                    onConfirmCodeClick = {},
                    onResetPhoneClick = {}
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginByPhoneCodeInputErrorPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides MockAppErrorParser) {
            Surface {
                CodeInputContent(
                    state = LoginByPhoneScreenState.CodeInput(
                        phoneNumber = "9991234567",
                        code = "111111",
                        actionError = CommonError.Unknown()
                    ),
                    onCodeChanged = {},
                    onConfirmCodeClick = {},
                    onResetPhoneClick = {}
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginByPhoneFullscreenLoadingPreview() {
    MaterialTheme {
        Surface {
            Box(Modifier.fillMaxWidth().height(400.dp), contentAlignment = Alignment.Center) {
                FullscreenLoading()
            }
        }
    }
}