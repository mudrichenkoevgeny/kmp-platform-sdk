package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
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
                title = { Text(stringResource(Res.string.registration_by_email)) },
                navigationIcon = {
                    IconButton(onClick = component::onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(Dimens.paddingLarge)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens.paddingMedium)
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
    OutlinedTextField(
        value = state.email,
        onValueChange = onEmailChanged,
        label = { Text(stringResource(Res.string.email)) },
        placeholder = { Text(stringResource(Res.string.email)) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        isError = state.actionError != null
    )

    Button(
        onClick = onSendCodeClick,
        modifier = Modifier.fillMaxWidth(),
        enabled = state.canSendCode
    ) {
        Text(stringResource(Res.string.send_code))
    }

    state.actionError?.let { error ->
        Text(
            text = error.toLocalizedMessage(),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
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
    Text(
        text = stringResource(Res.string.enter_confirmation_code),
        style = MaterialTheme.typography.titleLarge
    )

    Text(
        text = stringResource(Res.string.code_sent_to, state.email),
        style = MaterialTheme.typography.bodyMedium
    )

    OutlinedTextField(
        value = state.code,
        onValueChange = onCodeChanged,
        label = { Text(stringResource(Res.string.confirmation_code)) },
        placeholder = { Text(stringResource(Res.string.enter_confirmation_code)) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        isError = state.actionError != null
    )

    OutlinedTextField(
        value = state.password,
        onValueChange = onPasswordChanged,
        label = { Text(stringResource(Res.string.password)) },
        placeholder = { Text(stringResource(Res.string.password)) },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = onTogglePasswordVisibility) {
                Icon(
                    imageVector = if (state.isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = null
                )
            }
        },
        isError = !state.isPasswordValid || state.actionError != null
    )

    Button(
        onClick = onRegisterClick,
        modifier = Modifier.fillMaxWidth(),
        enabled = state.canRegister
    ) {
        Text(stringResource(Res.string.register))
    }

    if (state.resendTimerSeconds > 0) {
        Text(
            text = stringResource(Res.string.resend_code_timer, state.resendTimerSeconds),
            style = MaterialTheme.typography.bodySmall
        )
    } else {
        TextButton(
            onClick = onResendClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = state.canResendCode
        ) {
            Text(stringResource(Res.string.resend_code))
        }
    }

    state.actionError?.let { error ->
        Text(
            text = error.toLocalizedMessage(),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
}