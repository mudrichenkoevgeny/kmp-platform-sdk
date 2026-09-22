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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.Res as CommonRes
import io.github.mudrichenkoevgeny.kmp.core.common.ic_refresh
import io.github.mudrichenkoevgeny.kmp.core.common.retry
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
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
import io.github.mudrichenkoevgeny.kmp.feature.user.*
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.main.MainProfileComponentMock
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.accountlockout.AccountLockoutType
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.accountstatus.UserAccountStatus
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainProfileScreen(component: MainProfileComponent) {
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
                            onClick = component::onRefresh,
                            modifier = Modifier.testTag(MainProfileTestTags.REFRESH_BUTTON)
                        ) {
                            Icon(
                                painter = painterResource(CommonRes.drawable.ic_refresh),
                                contentDescription = stringResource(CommonRes.string.retry),
                                modifier = Modifier.padding(CoreTheme.dimens.paddingExtraSmall)
                            )
                        }
                    }
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                when (val currentState = state) {
                    is MainProfileScreenState.Loading -> FullscreenLoading()
                    is MainProfileScreenState.Unauthorized -> UnauthorizedContent(
                        state = currentState,
                        onLoginClick = component::onLoginClick
                    )
                    is MainProfileScreenState.Content -> ProfileContent(
                        state = currentState,
                        onLogoutClick = component::onLogoutClick,
                        onConfirmLogout = component::onConfirmLogout,
                        onTotpMainClick = component::onTotpMainClick,
                        onSessionsClick = component::onSessionsClick,
                        onIdentifiersClick = component::onIdentifiersClick,
                        onDeleteAccountClick = component::onDeleteAccountClick,
                        onConfirmDeleteAccount = component::onConfirmDeleteAccount,
                        onRestoreAccountClick = component::onRestoreAccountClick,
                        onUnlockAccountClick = component::onUnlockAccountClick,
                        onDismissDialog = component::onDismissDialog
                    )
                    is MainProfileScreenState.Error -> {
                        CoreErrorText(
                            text = currentState.error.toLocalizedMessage(),
                            modifier = Modifier.testTag(MainProfileTestTags.GLOBAL_ERROR_TEXT)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UnauthorizedContent(
    state: MainProfileScreenState.Unauthorized,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(CoreTheme.dimens.paddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CoreBodyText(
            text = stringResource(Res.string.not_authorized),
            modifier = Modifier.testTag(MainProfileTestTags.UNAUTHORIZED_TEXT)
        )
        Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))
        CoreButton(
            text = stringResource(Res.string.login),
            onClick = onLoginClick,
            modifier = Modifier.testTag(MainProfileTestTags.LOGIN_BUTTON)
        )
        ErrorText(state.actionError)
    }
}

@Composable
private fun ProfileContent(
    state: MainProfileScreenState.Content,
    onLogoutClick: () -> Unit,
    onConfirmLogout: () -> Unit,
    onTotpMainClick: () -> Unit,
    onSessionsClick: () -> Unit,
    onIdentifiersClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    onConfirmDeleteAccount: () -> Unit,
    onRestoreAccountClick: () -> Unit,
    onUnlockAccountClick: () -> Unit,
    onDismissDialog: () -> Unit
) {
    val isPendingDeletion = state.user.accountStatus == UserAccountStatus.PENDING_DELETION
    val isLocked = state.user.lockoutType != AccountLockoutType.NONE ||
            state.user.accountStatus == UserAccountStatus.SECURITY_HOLD

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(CoreTheme.dimens.paddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLocked) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = CoreTheme.dimens.paddingMedium)
                        .testTag(MainProfileTestTags.LOCKED_CARD),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(CoreTheme.dimens.paddingMedium),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CoreTitleText(
                            text = stringResource(Res.string.account_locked_title),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))
                        CoreBodyText(
                            text = stringResource(Res.string.account_locked_desc),
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))
                        CoreButton(
                            text = stringResource(Res.string.unlock_account),
                            onClick = onUnlockAccountClick,
                            modifier = Modifier.testTag(MainProfileTestTags.UNLOCK_ACCOUNT_BUTTON),
                            enabled = !state.actionLoading
                        )
                    }
                }
            }

            if (isPendingDeletion) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = CoreTheme.dimens.paddingMedium)
                        .testTag(MainProfileTestTags.PENDING_DELETION_CARD),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(CoreTheme.dimens.paddingMedium),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CoreTitleText(
                            text = stringResource(Res.string.account_pending_deletion_title),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))
                        CoreBodyText(
                            text = stringResource(Res.string.account_pending_deletion_desc),
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))
                        CoreButton(
                            text = stringResource(Res.string.restore_account),
                            onClick = onRestoreAccountClick,
                            modifier = Modifier.testTag(MainProfileTestTags.RESTORE_ACCOUNT_BUTTON),
                            enabled = !state.actionLoading
                        )
                    }
                }
            }

            CoreBodyText(
                text = stringResource(Res.string.user_id, state.user.id.asHexDashString()),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.testTag(MainProfileTestTags.USER_ID_TEXT)
            )

            Spacer(Modifier.height(CoreTheme.dimens.paddingLarge))

            CoreButton(
                text = stringResource(Res.string.totp_main),
                onClick = onTotpMainClick,
                modifier = Modifier.testTag(MainProfileTestTags.TOTP_MAIN_BUTTON),
                enabled = !state.actionLoading
            )

            Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))

            CoreButton(
                text = stringResource(Res.string.sessions),
                onClick = onSessionsClick,
                modifier = Modifier.testTag(MainProfileTestTags.SESSIONS_BUTTON),
                enabled = !state.actionLoading
            )

            Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))

            CoreButton(
                text = stringResource(Res.string.identifiers),
                onClick = onIdentifiersClick,
                modifier = Modifier.testTag(MainProfileTestTags.IDENTIFIERS_BUTTON),
                enabled = !state.actionLoading
            )

            Spacer(Modifier.height(CoreTheme.dimens.paddingLarge))

            if (state.isAccountDeletionAvailable && !isPendingDeletion) {
                CoreButton(
                    text = stringResource(Res.string.delete_account),
                    onClick = onDeleteAccountClick,
                    modifier = Modifier.testTag(MainProfileTestTags.DELETE_ACCOUNT_BUTTON),
                    enabled = !state.actionLoading
                )
                Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))
            }

            CoreButton(
                text = stringResource(Res.string.logout),
                onClick = onLogoutClick,
                modifier = Modifier.testTag(MainProfileTestTags.LOGOUT_BUTTON),
                enabled = !state.actionLoading
            )

            ErrorText(state.actionError)
        }

        if (state.showDeleteConfirmation) {
            AlertDialog(
                onDismissRequest = onDismissDialog,
                title = { CoreTitleText(text = stringResource(Res.string.dialog_confirm_title)) },
                text = { CoreBodyText(text = stringResource(Res.string.delete_account_confirm_msg)) },
                confirmButton = {
                    CoreTextButton(
                        text = stringResource(Res.string.dialog_confirm),
                        onClick = onConfirmDeleteAccount
                    )
                },
                dismissButton = {
                    CoreTextButton(
                        text = stringResource(Res.string.dialog_cancel),
                        onClick = onDismissDialog
                    )
                }
            )
        }

        if (state.showLogoutConfirmation) {
            AlertDialog(
                onDismissRequest = onDismissDialog,
                title = { CoreTitleText(text = stringResource(Res.string.dialog_confirm_title)) },
                text = { CoreBodyText(text = stringResource(Res.string.logout_confirm_msg)) },
                confirmButton = {
                    CoreTextButton(
                        text = stringResource(Res.string.dialog_confirm),
                        onClick = onConfirmLogout
                    )
                },
                dismissButton = {
                    CoreTextButton(
                        text = stringResource(Res.string.dialog_cancel),
                        onClick = onDismissDialog
                    )
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
            CoreErrorText(
                text = it.toLocalizedMessage(),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = CoreTheme.dimens.paddingSmall)
                    .testTag(MainProfileTestTags.ACTION_ERROR_TEXT)
            )
        }
    }
}

@InternalApi
internal class MainProfilePreviewProvider : PreviewParameterProvider<MainProfileScreenState> {
    private val items: List<Pair<String, MainProfileScreenState>> = listOf(
        "Unauthorized" to MainProfileScreenState.Unauthorized(),
        "Content Default" to MainProfileScreenState.Content(user = userDetailsMock(), isAccountDeletionAvailable = true),
        "Content Locked" to MainProfileScreenState.Content(user = userDetailsMock(lockoutType = AccountLockoutType.INDEFINITE)),
        "Content Pending Deletion" to MainProfileScreenState.Content(user = userDetailsMock(accountStatus = UserAccountStatus.PENDING_DELETION)),
        "Action Loading" to MainProfileScreenState.Content(user = userDetailsMock(), actionLoading = true),
        "Inline Error" to MainProfileScreenState.Content(user = userDetailsMock(), actionError = CommonError.Unknown()),
        "Global Error" to MainProfileScreenState.Error(error = CommonError.Unknown()),
        "Fullscreen Loading" to MainProfileScreenState.Loading
    )

    override val values: Sequence<MainProfileScreenState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun MainProfileScreenPreviewContent(state: MainProfileScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        MainProfileScreen(
            component = MainProfileComponentMock(initialState = state)
        )
    }
}

@InternalApi
private val defaultMainProfilePreviewState = MainProfileScreenState.Content(
    user = userDetailsMock(),
    isAccountDeletionAvailable = true
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StatesPreview(
    @PreviewParameter(MainProfilePreviewProvider::class) state: MainProfileScreenState
) {
    ScreenPreviewContainer {
        MainProfileScreenPreviewContent(state = state)
    }
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        MainProfileScreenPreviewContent(state = defaultMainProfilePreviewState)
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        MainProfileScreenPreviewContent(state = defaultMainProfilePreviewState)
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        MainProfileScreenPreviewContent(state = defaultMainProfilePreviewState)
    }
}

internal object MainProfileTestTags {
    const val UNAUTHORIZED_TEXT = "MainProfile_UnauthorizedText"
    const val LOGIN_BUTTON = "MainProfile_LoginButton"

    const val LOCKED_CARD = "MainProfile_LockedCard"
    const val UNLOCK_ACCOUNT_BUTTON = "MainProfile_UnlockAccountButton"

    const val PENDING_DELETION_CARD = "MainProfile_PendingDeletionCard"
    const val RESTORE_ACCOUNT_BUTTON = "MainProfile_RestoreAccountButton"

    const val USER_ID_TEXT = "MainProfile_UserIdText"
    const val TOTP_MAIN_BUTTON = "MainProfile_TotpMainButton"
    const val SESSIONS_BUTTON = "MainProfile_SessionsButton"
    const val IDENTIFIERS_BUTTON = "MainProfile_IdentifiersButton"
    const val DELETE_ACCOUNT_BUTTON = "MainProfile_DeleteAccountButton"
    const val LOGOUT_BUTTON = "MainProfile_LogoutButton"

    const val ACTION_ERROR_TEXT = "MainProfile_ActionErrorText"
    const val GLOBAL_ERROR_TEXT = "MainProfile_GlobalErrorText"
    const val REFRESH_BUTTON = "MainProfile_RefreshButton"
}
