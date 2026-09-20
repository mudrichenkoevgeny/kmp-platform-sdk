package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.Res as CommonRes
import io.github.mudrichenkoevgeny.kmp.core.common.*
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreBackButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.error.FullscreenError
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input.CoreOutlinedTextField
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreBodyText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreErrorText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreScreenTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.Res
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.*
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.detail.UserDetailComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.accountstatus.UserAccountStatus
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(component: UserDetailComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    CoreScreenTitleText(
                        text = stringResource(Res.string.user_details_title),
                        modifier = Modifier.testTag(UserDetailTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    CoreBackButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(UserDetailTestTags.BACK_BUTTON)
                    )
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (val currentState = state) {
                is UserDetailScreenState.Loading -> FullscreenLoading()
                is UserDetailScreenState.Error -> FullscreenError(
                    error = currentState.error,
                    onRetry = component::onRetry,
                    modifier = Modifier.testTag(UserDetailTestTags.GLOBAL_ERROR)
                )
                is UserDetailScreenState.Content -> {
                    Content(
                        state = currentState,
                        onAuthorityLevelChanged = component::onAuthorityLevelChanged,
                        onAccountStatusChanged = component::onAccountStatusChanged,
                        onLockoutTypeChanged = component::onLockoutTypeChanged,
                        onTemporaryLockoutUntilChanged = component::onTemporaryLockoutUntilChanged,
                        onUpdateClick = component::onUpdateClick,
                        onDeleteClick = component::onDeleteClick,
                        onSessionsClick = component::onSessionsClick,
                        onIdentifiersClick = component::onIdentifiersClick
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(
    state: UserDetailScreenState.Content,
    onAuthorityLevelChanged: (String) -> Unit,
    onAccountStatusChanged: (String) -> Unit,
    onLockoutTypeChanged: (String) -> Unit,
    onTemporaryLockoutUntilChanged: (String) -> Unit,
    onUpdateClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onSessionsClick: () -> Unit,
    onIdentifiersClick: () -> Unit
) {
    val notAvailableText = stringResource(Res.string.not_available)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(CoreTheme.dimens.paddingMedium)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingMedium)
    ) {
        CoreTitleText(
            text = "${stringResource(Res.string.user_id)}: ${state.user.id.value}",
            style = MaterialTheme.typography.titleMedium
        )
        CoreBodyText(
            text = "${stringResource(Res.string.user_role)}: ${state.user.role.name}"
        )
        CoreBodyText(
            text = stringResource(Res.string.totp_enabled_label, state.user.isTotpEnabled)
        )
        CoreBodyText(
            text = stringResource(Res.string.created_at_label, state.user.createdAt.toString())
        )
        CoreBodyText(
            text = stringResource(Res.string.last_login_at_label, state.user.lastLoginAt?.toString() ?: notAvailableText)
        )
        CoreBodyText(
            text = stringResource(Res.string.last_active_at_label, state.user.lastActiveAt?.toString() ?: notAvailableText)
        )

        if (state.user.accountStatus == UserAccountStatus.PENDING_DELETION && state.user.scheduledPermanentDeletionAt != null) {
            CoreErrorText(
                text = stringResource(Res.string.scheduled_deletion_at_label, state.user.scheduledPermanentDeletionAt.toString())
            )
        }

        CoreButton(
            text = stringResource(Res.string.user_sessions),
            onClick = onSessionsClick,
            modifier = Modifier.testTag(UserDetailTestTags.SESSIONS_BUTTON)
        )

        CoreButton(
            text = stringResource(Res.string.user_identifiers),
            onClick = onIdentifiersClick,
            modifier = Modifier.testTag(UserDetailTestTags.IDENTIFIERS_BUTTON)
        )

        CoreOutlinedTextField(
            value = state.accountStatusInput,
            onValueChange = onAccountStatusChanged,
            label = { CoreBodyText(stringResource(Res.string.user_account_status)) },
            placeholder = { CoreBodyText(stringResource(Res.string.user_account_status)) },
            modifier = Modifier.testTag(UserDetailTestTags.ACCOUNT_STATUS_INPUT),
            enabled = !state.isSaving && !state.isDeleting
        )

        CoreOutlinedTextField(
            value = state.authorityLevelInput,
            onValueChange = onAuthorityLevelChanged,
            label = { CoreBodyText(stringResource(Res.string.authority_level)) },
            placeholder = { CoreBodyText(stringResource(Res.string.authority_level)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(UserDetailTestTags.AUTHORITY_LEVEL_INPUT),
            enabled = !state.isSaving && !state.isDeleting
        )

        CoreOutlinedTextField(
            value = state.lockoutTypeInput,
            onValueChange = onLockoutTypeChanged,
            label = { CoreBodyText(stringResource(CommonRes.string.ui_common_lockout_type)) },
            placeholder = { CoreBodyText(stringResource(CommonRes.string.ui_common_lockout_type)) },
            modifier = Modifier.testTag(UserDetailTestTags.LOCKOUT_TYPE_INPUT),
            enabled = !state.isSaving && !state.isDeleting
        )

        CoreOutlinedTextField(
            value = state.temporaryLockoutUntilInput,
            onValueChange = onTemporaryLockoutUntilChanged,
            label = { CoreBodyText(stringResource(CommonRes.string.ui_common_lockout_until)) },
            placeholder = { CoreBodyText(stringResource(CommonRes.string.ui_common_lockout_until)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(UserDetailTestTags.TEMPORARY_LOCKOUT_UNTIL_INPUT),
            enabled = !state.isSaving && !state.isDeleting
        )

        CoreButton(
            text = stringResource(if (state.isSaving) Res.string.saving else Res.string.update_user),
            onClick = onUpdateClick,
            modifier = Modifier.testTag(UserDetailTestTags.UPDATE_BUTTON),
            enabled = !state.isSaving && !state.isDeleting
        )

        state.saveError?.let {
            CoreErrorText(
                text = it.toLocalizedMessage(),
                modifier = Modifier.testTag(UserDetailTestTags.SAVE_ERROR_TEXT)
            )
        }

        CoreButton(
            text = stringResource(Res.string.delete_user),
            onClick = onDeleteClick,
            modifier = Modifier.testTag(UserDetailTestTags.DELETE_BUTTON),
            enabled = !state.isSaving && !state.isDeleting,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        )

        state.deleteError?.let {
            CoreErrorText(
                text = it.toLocalizedMessage(),
                modifier = Modifier.testTag(UserDetailTestTags.DELETE_ERROR_TEXT)
            )
        }
    }
}

@InternalApi
internal class UserDetailPreviewProvider : PreviewParameterProvider<UserDetailScreenState> {
    private val items: List<Pair<String, UserDetailScreenState>> = listOf(
        "Content" to UserDetailScreenState.Content(
            user = userDetailsMock(),
            authorityLevelInput = "0",
            accountStatusInput = "ACTIVE",
            lockoutTypeInput = "NONE",
            temporaryLockoutUntilInput = ""
        ),
        "Pending Deletion" to UserDetailScreenState.Content(
            user = userDetailsMock(accountStatus = UserAccountStatus.PENDING_DELETION),
            authorityLevelInput = "0",
            accountStatusInput = "PENDING_DELETION",
            lockoutTypeInput = "NONE",
            temporaryLockoutUntilInput = ""
        ),
        "Saving" to UserDetailScreenState.Content(
            user = userDetailsMock(),
            authorityLevelInput = "0",
            accountStatusInput = "ACTIVE",
            lockoutTypeInput = "NONE",
            temporaryLockoutUntilInput = "",
            isSaving = true
        ),
        "Error" to UserDetailScreenState.Error(error = CommonError.Unknown()),
        "Loading" to UserDetailScreenState.Loading
    )

    override val values: Sequence<UserDetailScreenState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun UserDetailScreenPreviewContent(state: UserDetailScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        UserDetailScreen(
            component = UserDetailComponentMock(initialState = state)
        )
    }
}

@InternalApi
private val defaultUserDetailPreviewState = UserDetailScreenState.Content(
    user = userDetailsMock(),
    authorityLevelInput = "0",
    accountStatusInput = "ACTIVE",
    lockoutTypeInput = "NONE",
    temporaryLockoutUntilInput = ""
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StatesPreview(
    @PreviewParameter(UserDetailPreviewProvider::class) state: UserDetailScreenState
) {
    ScreenPreviewContainer {
        UserDetailScreenPreviewContent(state = state)
    }
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        UserDetailScreenPreviewContent(state = defaultUserDetailPreviewState)
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        UserDetailScreenPreviewContent(state = defaultUserDetailPreviewState)
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        UserDetailScreenPreviewContent(state = defaultUserDetailPreviewState)
    }
}

object UserDetailTestTags {
    const val TITLE = "UserDetail_Title"
    const val BACK_BUTTON = "UserDetail_BackButton"
    const val GLOBAL_ERROR = "UserDetail_GlobalError"
    const val ACCOUNT_STATUS_INPUT = "UserDetail_AccountStatusInput"
    const val AUTHORITY_LEVEL_INPUT = "UserDetail_AuthorityLevelInput"
    const val LOCKOUT_TYPE_INPUT = "UserDetail_LockoutTypeInput"
    const val TEMPORARY_LOCKOUT_UNTIL_INPUT = "UserDetail_TemporaryLockoutUntilInput"
    const val UPDATE_BUTTON = "UserDetail_UpdateButton"
    const val DELETE_BUTTON = "UserDetail_DeleteButton"
    const val SESSIONS_BUTTON = "UserDetail_SessionsButton"
    const val IDENTIFIERS_BUTTON = "UserDetail_IdentifiersButton"
    const val SAVE_ERROR_TEXT = "UserDetail_SaveErrorText"
    const val DELETE_ERROR_TEXT = "UserDetail_DeleteErrorText"
}
