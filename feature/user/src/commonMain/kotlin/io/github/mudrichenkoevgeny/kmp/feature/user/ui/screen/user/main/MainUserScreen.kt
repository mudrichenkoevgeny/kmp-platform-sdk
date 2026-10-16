package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.main

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.ic_refresh
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.retry
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreTextButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenOverlayLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreBodyText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreErrorText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.delete_account
import io.github.mudrichenkoevgeny.kmp.feature.user.delete_account_confirm_msg
import io.github.mudrichenkoevgeny.kmp.feature.user.dialog_cancel
import io.github.mudrichenkoevgeny.kmp.feature.user.dialog_confirm
import io.github.mudrichenkoevgeny.kmp.feature.user.dialog_confirm_title
import io.github.mudrichenkoevgeny.kmp.feature.user.identifiers
import io.github.mudrichenkoevgeny.kmp.feature.user.logout
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.main.MainUserComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.sessions
import io.github.mudrichenkoevgeny.kmp.feature.user.sign_in
import io.github.mudrichenkoevgeny.kmp.feature.user.totp_main
import io.github.mudrichenkoevgeny.kmp.feature.user.user_id
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import io.github.mudrichenkoevgeny.kmp.core.common.Res as CommonRes

object MainUserTestTags {
    const val REFRESH_BUTTON = "MainUser_RefreshButton"
    const val USER_ID_TEXT = "MainUser_UserIdText"
    const val LOGOUT_BUTTON = "MainUser_LogoutButton"
    const val LOGIN_BUTTON = "MainUser_LoginButton"
    const val GLOBAL_ERROR_TEXT = "MainUser_GlobalErrorText"
}

@OptIn(ExperimentalMaterial3Api::class, InternalApi::class)
@Composable
fun MainUserScreen(component: MainUserComponent) {
    val state by component.state.subscribeAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { },
                    actions = {
                        IconButton(
                            onClick = component::onRetry,
                            modifier = Modifier.testTag(MainUserTestTags.REFRESH_BUTTON)
                        ) {
                            Icon(
                                painter = painterResource(CommonRes.drawable.ic_refresh),
                                contentDescription = stringResource(CommonRes.string.retry),
                                modifier = Modifier.padding(CoreTheme.dimens.paddingExtraSmall)
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (val current = state) {
                    is MainUserScreenState.Loading -> FullscreenLoading()
                    is MainUserScreenState.Unauthorized -> {
                        UnauthorizedUserContent(
                            actionError = current.actionError,
                            onLoginClick = component::onLoginClick
                        )
                    }
                    is MainUserScreenState.Content -> {
                        MainUserContent(
                            state = current,
                            onTotpMainClick = component::onTotpMainClick,
                            onSessionsClick = component::onSessionsClick,
                            onIdentifiersClick = component::onIdentifiersClick,
                            onDeleteAccountClick = component::onDeleteUserClick,
                            onLogoutClick = component::onLogoutClick
                        )

                        if (current.isDeleteConfirmationVisible) {
                            AlertDialog(
                                onDismissRequest = component::onDismissDeleteUserDialog,
                                title = { CoreTitleText(text = stringResource(Res.string.dialog_confirm_title)) },
                                text = { CoreBodyText(text = stringResource(Res.string.delete_account_confirm_msg)) },
                                confirmButton = {
                                    CoreButton(
                                        text = stringResource(Res.string.dialog_confirm),
                                        onClick = component::onConfirmDeleteUserClick
                                    )
                                },
                                dismissButton = {
                                    CoreTextButton(
                                        text = stringResource(Res.string.dialog_cancel),
                                        onClick = component::onDismissDeleteUserDialog
                                    )
                                }
                            )
                        }

                        if (current.actionLoading) {
                            FullscreenOverlayLoading()
                        }
                    }
                    is MainUserScreenState.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CoreErrorText(
                                text = current.error.toLocalizedMessage(),
                                modifier = Modifier.testTag(MainUserTestTags.GLOBAL_ERROR_TEXT)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UnauthorizedUserContent(
    actionError: AppError?,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(CoreTheme.dimens.paddingMedium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (actionError != null) {
            CoreErrorText(
                text = actionError.toLocalizedMessage(),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(CoreTheme.dimens.paddingMedium))
        }

        CoreButton(
            text = stringResource(Res.string.sign_in),
            onClick = onLoginClick,
            modifier = Modifier.testTag(MainUserTestTags.LOGIN_BUTTON)
        )
    }
}

@Composable
private fun MainUserContent(
    state: MainUserScreenState.Content,
    onTotpMainClick: () -> Unit,
    onSessionsClick: () -> Unit,
    onIdentifiersClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(CoreTheme.dimens.paddingMedium)
    ) {
        AnimatedVisibility(
            visible = state.actionError != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            state.actionError?.let { error ->
                Column {
                    CoreErrorText(
                        text = error.toLocalizedMessage(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(CoreTheme.dimens.paddingMedium))
                }
            }
        }

        CoreBodyText(
            text = "${stringResource(Res.string.user_id)}: ${state.user.id.asHexDashString()}",
            modifier = Modifier.testTag(MainUserTestTags.USER_ID_TEXT)
        )

        Spacer(modifier = Modifier.height(CoreTheme.dimens.paddingLarge))

        CoreButton(
            text = stringResource(Res.string.totp_main),
            onClick = onTotpMainClick,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(CoreTheme.dimens.paddingSmall))

        CoreButton(
            text = stringResource(Res.string.sessions),
            onClick = onSessionsClick,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(CoreTheme.dimens.paddingSmall))

        CoreButton(
            text = stringResource(Res.string.identifiers),
            onClick = onIdentifiersClick,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(CoreTheme.dimens.paddingLarge))

        CoreButton(
            text = stringResource(Res.string.delete_account),
            onClick = onDeleteAccountClick,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(CoreTheme.dimens.paddingSmall))

        CoreButton(
            text = stringResource(Res.string.logout),
            onClick = onLogoutClick,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(MainUserTestTags.LOGOUT_BUTTON)
        )
    }
}

@InternalApi
@Composable
private fun MainUserScreenPreviewContent() {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        MainUserScreen(
            component = MainUserComponentMock()
        )
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun MainUserScreenPreview() {
    ScreenPreviewContainer {
        MainUserScreenPreviewContent()
    }
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        MainUserScreenPreviewContent()
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        MainUserScreenPreviewContent()
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        MainUserScreenPreviewContent()
    }
}
