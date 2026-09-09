package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.Res
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.*
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUserScreen(component: CreateUserComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.create_user_title),
                        modifier = Modifier.testTag(CreateUserTestTags.TITLE),
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(CreateUserTestTags.BACK_BUTTON),
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
            )
        }
    ) { padding ->
        CreateUserForm(
            state = state,
            onEmailChanged = component::onEmailChanged,
            onPasswordChanged = component::onPasswordChanged,
            onRoleChanged = component::onRoleChanged,
            onStatusChanged = component::onStatusChanged,
            onAuthorityLevelChanged = component::onAuthorityLevelChanged,
            onCreateClick = component::onCreateClick,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(Dimens.paddingMedium)
                .verticalScroll(rememberScrollState()),
        )
    }
}

@Composable
private fun CreateUserForm(
    state: CreateUserScreenState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onRoleChanged: (String) -> Unit,
    onStatusChanged: (String) -> Unit,
    onAuthorityLevelChanged: (String) -> Unit,
    onCreateClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.paddingMedium),
    ) {
        OutlinedTextField(
            value = state.email,
            onValueChange = onEmailChanged,
            label = { Text(stringResource(Res.string.email)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag(CreateUserTestTags.EMAIL_INPUT),
            enabled = !state.isLoading,
        )

        OutlinedTextField(
            value = state.password,
            onValueChange = onPasswordChanged,
            label = { Text(stringResource(Res.string.password)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag(CreateUserTestTags.PASSWORD_INPUT),
            enabled = !state.isLoading,
        )

        OutlinedTextField(
            value = state.role,
            onValueChange = onRoleChanged,
            label = { Text(stringResource(Res.string.user_role)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag(CreateUserTestTags.ROLE_INPUT),
            enabled = !state.isLoading,
        )

        OutlinedTextField(
            value = state.status,
            onValueChange = onStatusChanged,
            label = { Text(stringResource(Res.string.user_account_status)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag(CreateUserTestTags.STATUS_INPUT),
            enabled = !state.isLoading,
        )

        OutlinedTextField(
            value = state.authorityLevel,
            onValueChange = onAuthorityLevelChanged,
            label = { Text(stringResource(Res.string.authority_level)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag(CreateUserTestTags.AUTHORITY_LEVEL_INPUT),
            enabled = !state.isLoading,
        )

        Button(
            onClick = onCreateClick,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(CreateUserTestTags.CREATE_BUTTON),
            enabled = !state.isLoading,
        ) {
            Text(stringResource(if (state.isLoading) Res.string.saving else Res.string.create_user))
        }

        state.error?.let {
            Text(
                text = it.toLocalizedMessage(),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.testTag(CreateUserTestTags.ERROR_TEXT),
            )
        }
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun CreateUserPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
            Surface {
                CreateUserForm(
                    state = CreateUserScreenState(
                        email = "test@example.com",
                        password = "password123",
                    ),
                    onEmailChanged = {},
                    onPasswordChanged = {},
                    onRoleChanged = {},
                    onStatusChanged = {},
                    onAuthorityLevelChanged = {},
                    onCreateClick = {},
                )
            }
        }
    }
}

object CreateUserTestTags {
    const val TITLE = "CreateUser_Title"
    const val BACK_BUTTON = "CreateUser_BackButton"
    const val EMAIL_INPUT = "CreateUser_EmailInput"
    const val PASSWORD_INPUT = "CreateUser_PasswordInput"
    const val ROLE_INPUT = "CreateUser_RoleInput"
    const val STATUS_INPUT = "CreateUser_StatusInput"
    const val AUTHORITY_LEVEL_INPUT = "CreateUser_AuthorityLevelInput"
    const val CREATE_BUTTON = "CreateUser_CreateButton"
    const val ERROR_TEXT = "CreateUser_ErrorText"
}
