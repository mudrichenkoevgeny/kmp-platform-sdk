package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
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
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import org.jetbrains.compose.resources.stringResource

// TODO: In development
@Composable
fun MainProfileScreen(component: MainProfileComponent) {
    val state by component.state.subscribeAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val currentState = state) {
            is MainProfileScreenState.Loading -> FullscreenLoading()
            is MainProfileScreenState.Unauthorized -> UnauthorizedContent(onLoginClick = component::onLoginClick)
            is MainProfileScreenState.Content -> ProfileContent(
                state = currentState,
                onLogoutClick = component::onLogoutClick,
                onTotpSettingsClick = component::onTotpSettingsClick,
                onSessionsClick = component::onSessionsClick,
                onIdentifiersClick = component::onIdentifiersClick,
                onDeleteAccountClick = component::onDeleteAccountClick,
                onConfirmDeleteAccount = component::onConfirmDeleteAccount,
                onDismissDialog = component::onDismissDialog
            )
            is MainProfileScreenState.Error -> {
                Text(
                    text = currentState.error.toLocalizedMessage(),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.testTag(MainProfileTestTags.GLOBAL_ERROR_TEXT)
                )
            }
        }
    }
}

@Composable
private fun UnauthorizedContent(onLoginClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(Res.string.not_authorized),
            modifier = Modifier.testTag(MainProfileTestTags.UNAUTHORIZED_TEXT)
        )
        Spacer(Modifier.height(Dimens.paddingMedium))
        Button(
            onClick = onLoginClick,
            modifier = Modifier.testTag(MainProfileTestTags.LOGIN_BUTTON)
        ) {
            Text(text = stringResource(Res.string.login))
        }
    }
}

@Composable
private fun ProfileContent(
    state: MainProfileScreenState.Content,
    onLogoutClick: () -> Unit,
    onTotpSettingsClick: () -> Unit,
    onSessionsClick: () -> Unit,
    onIdentifiersClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    onConfirmDeleteAccount: () -> Unit,
    onDismissDialog: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.paddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.user_id, state.user.id.asHexDashString()),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.testTag(MainProfileTestTags.USER_ID_TEXT)
            )

            Spacer(Modifier.height(Dimens.paddingLarge))

            OutlinedButton(
                onClick = onTotpSettingsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(MainProfileTestTags.TOTP_SETTINGS_BUTTON),
                enabled = !state.actionLoading
            ) {
                Text(text = stringResource(Res.string.totp_settings))
            }

            Spacer(Modifier.height(Dimens.paddingSmall))

            OutlinedButton(
                onClick = onSessionsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(MainProfileTestTags.SESSIONS_BUTTON),
                enabled = !state.actionLoading
            ) {
                Text(text = stringResource(Res.string.sessions))
            }

            Spacer(Modifier.height(Dimens.paddingSmall))

            OutlinedButton(
                onClick = onIdentifiersClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(MainProfileTestTags.IDENTIFIERS_BUTTON),
                enabled = !state.actionLoading
            ) {
                Text(text = stringResource(Res.string.identifiers))
            }

            Spacer(Modifier.height(Dimens.paddingLarge))

            if (state.isAccountDeletionAvailable) {
                OutlinedButton(
                    onClick = onDeleteAccountClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(MainProfileTestTags.DELETE_ACCOUNT_BUTTON),
                    enabled = !state.actionLoading
                ) {
                    Text(
                        text = stringResource(Res.string.delete_account),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Spacer(Modifier.height(Dimens.paddingSmall))
            }

            Button(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(MainProfileTestTags.LOGOUT_BUTTON),
                enabled = !state.actionLoading
            ) {
                Text(text = stringResource(Res.string.logout))
            }

            ErrorText(state.actionError)
        }

        if (state.showDeleteConfirmation) {
            AlertDialog(
                onDismissRequest = onDismissDialog,
                title = { Text(text = stringResource(Res.string.dialog_confirm_title)) },
                text = { Text(text = stringResource(Res.string.delete_account_confirm_msg)) },
                confirmButton = {
                    TextButton(onClick = onConfirmDeleteAccount) {
                        Text(text = stringResource(Res.string.dialog_confirm))
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismissDialog) {
                        Text(text = stringResource(Res.string.dialog_cancel))
                    }
                }
            )
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
                    .testTag(MainProfileTestTags.ACTION_ERROR_TEXT)
            )
        }
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun MainProfileScreenUnauthorizedPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
            Surface {
                UnauthorizedContent(onLoginClick = {})
            }
        }
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun MainProfileScreenContentPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
            Surface {
                ProfileContent(
                    state = MainProfileScreenState.Content(
                        user = userDetailsMock(),
                        isAccountDeletionAvailable = true
                    ),
                    onLogoutClick = {},
                    onTotpSettingsClick = {},
                    onSessionsClick = {},
                    onIdentifiersClick = {},
                    onDeleteAccountClick = {},
                    onConfirmDeleteAccount = {},
                    onDismissDialog = {}
                )
            }
        }
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun MainProfileScreenContentLoadingPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
            Surface {
                ProfileContent(
                    state = MainProfileScreenState.Content(
                        user = userDetailsMock(),
                        actionLoading = true
                    ),
                    onLogoutClick = {},
                    onTotpSettingsClick = {},
                    onSessionsClick = {},
                    onIdentifiersClick = {},
                    onDeleteAccountClick = {},
                    onConfirmDeleteAccount = {},
                    onDismissDialog = {}
                )
            }
        }
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun MainProfileScreenContentErrorPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
            Surface {
                ProfileContent(
                    state = MainProfileScreenState.Content(
                        user = userDetailsMock(),
                        actionError = CommonError.Unknown()
                    ),
                    onLogoutClick = {},
                    onTotpSettingsClick = {},
                    onSessionsClick = {},
                    onIdentifiersClick = {},
                    onDeleteAccountClick = {},
                    onConfirmDeleteAccount = {},
                    onDismissDialog = {}
                )
            }
        }
    }
}

internal object MainProfileTestTags {
    const val UNAUTHORIZED_TEXT = "MainProfile_UnauthorizedText"
    const val LOGIN_BUTTON = "MainProfile_LoginButton"

    const val USER_ID_TEXT = "MainProfile_UserIdText"
    const val TOTP_SETTINGS_BUTTON = "MainProfile_TotpSettingsButton"
    const val SESSIONS_BUTTON = "MainProfile_SessionsButton"
    const val IDENTIFIERS_BUTTON = "MainProfile_IdentifiersButton"
    const val DELETE_ACCOUNT_BUTTON = "MainProfile_DeleteAccountButton"
    const val LOGOUT_BUTTON = "MainProfile_LogoutButton"

    const val ACTION_ERROR_TEXT = "MainProfile_ActionErrorText"
    const val GLOBAL_ERROR_TEXT = "MainProfile_GlobalErrorText"
}