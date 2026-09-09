package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.error.FullscreenError
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.Res
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.*
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(component: UserDetailComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.user_details_title),
                        modifier = Modifier.testTag(UserDetailTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(UserDetailTestTags.BACK_BUTTON)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
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
    onUpdateClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onSessionsClick: () -> Unit,
    onIdentifiersClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.paddingMedium)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(Dimens.paddingMedium)
    ) {
        Text(
            text = "${stringResource(Res.string.user_id)}: ${state.user.id.value}",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "${stringResource(Res.string.user_role)}: ${state.user.role.name}",
            style = MaterialTheme.typography.bodyMedium
        )

        Button(
            onClick = onSessionsClick,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(UserDetailTestTags.SESSIONS_BUTTON)
        ) {
            Text(stringResource(Res.string.user_sessions))
        }

        Button(
            onClick = onIdentifiersClick,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(UserDetailTestTags.IDENTIFIERS_BUTTON)
        ) {
            Text(stringResource(Res.string.user_identifiers))
        }

        OutlinedTextField(
            value = state.accountStatusInput,
            onValueChange = onAccountStatusChanged,
            label = { Text(stringResource(Res.string.user_account_status)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag(UserDetailTestTags.ACCOUNT_STATUS_INPUT),
            enabled = !state.isSaving && !state.isDeleting
        )

        OutlinedTextField(
            value = state.authorityLevelInput,
            onValueChange = onAuthorityLevelChanged,
            label = { Text(stringResource(Res.string.authority_level)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag(UserDetailTestTags.AUTHORITY_LEVEL_INPUT),
            enabled = !state.isSaving && !state.isDeleting
        )

        Button(
            onClick = onUpdateClick,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(UserDetailTestTags.UPDATE_BUTTON),
            enabled = !state.isSaving && !state.isDeleting
        ) {
            Text(stringResource(if (state.isSaving) Res.string.saving else Res.string.update_user))
        }

        state.saveError?.let {
            Text(
                text = it.toLocalizedMessage(),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.testTag(UserDetailTestTags.SAVE_ERROR_TEXT)
            )
        }

        Button(
            onClick = onDeleteClick,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(UserDetailTestTags.DELETE_BUTTON),
            enabled = !state.isSaving && !state.isDeleting,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text(stringResource(Res.string.delete_user))
        }

        state.deleteError?.let {
            Text(
                text = it.toLocalizedMessage(),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.testTag(UserDetailTestTags.DELETE_ERROR_TEXT)
            )
        }
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun UserDetailPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
            Surface {
                Content(
                    state = UserDetailScreenState.Content(
                        user = userDetailsMock(),
                        authorityLevelInput = "0",
                        accountStatusInput = "ACTIVE"
                    ),
                    onAuthorityLevelChanged = {},
                    onAccountStatusChanged = {},
                    onUpdateClick = {},
                    onDeleteClick = {},
                    onSessionsClick = {},
                    onIdentifiersClick = {}
                )
            }
        }
    }
}

object UserDetailTestTags {
    const val TITLE = "UserDetail_Title"
    const val BACK_BUTTON = "UserDetail_BackButton"
    const val GLOBAL_ERROR = "UserDetail_GlobalError"
    const val ACCOUNT_STATUS_INPUT = "UserDetail_AccountStatusInput"
    const val AUTHORITY_LEVEL_INPUT = "UserDetail_AuthorityLevelInput"
    const val UPDATE_BUTTON = "UserDetail_UpdateButton"
    const val DELETE_BUTTON = "UserDetail_DeleteButton"
    const val SESSIONS_BUTTON = "UserDetail_SessionsButton"
    const val IDENTIFIERS_BUTTON = "UserDetail_IdentifiersButton"
    const val SAVE_ERROR_TEXT = "UserDetail_SaveErrorText"
    const val DELETE_ERROR_TEXT = "UserDetail_DeleteErrorText"
}
