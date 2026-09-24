package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreBackButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.container.CoreScrollableScreenContent
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input.CoreEmailTextField
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input.CoreOutlinedTextField
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreBodyText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreErrorText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreScreenTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.Res
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.authority_level
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.create_user
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.create_user_title
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.email
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.create.CreateUserComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.password
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.saving
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.user_account_status
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.user_role
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUserScreen(component: CreateUserComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    CoreScreenTitleText(
                        text = stringResource(Res.string.create_user_title),
                        modifier = Modifier.testTag(CreateUserTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    CoreBackButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(CreateUserTestTags.BACK_BUTTON)
                    )
                }
            )
        }
    ) { padding ->
        CoreScrollableScreenContent(
            modifier = Modifier.padding(padding)
        ) {
            CreateUserForm(
                state = state,
                onEmailChanged = component::onEmailChanged,
                onPasswordChanged = component::onPasswordChanged,
                onRoleChanged = component::onRoleChanged,
                onStatusChanged = component::onStatusChanged,
                onAuthorityLevelChanged = component::onAuthorityLevelChanged,
                onCreateClick = component::onCreateClick
            )
        }
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
    onCreateClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingMedium)
    ) {
        CoreEmailTextField(
            value = state.email,
            onValueChange = onEmailChanged,
            label = { CoreBodyText(stringResource(Res.string.email)) },
            placeholder = { CoreBodyText(stringResource(Res.string.email)) },
            modifier = Modifier.testTag(CreateUserTestTags.EMAIL_INPUT),
            enabled = !state.isLoading
        )

        CoreOutlinedTextField(
            value = state.password,
            onValueChange = onPasswordChanged,
            label = { CoreBodyText(stringResource(Res.string.password)) },
            placeholder = { CoreBodyText(stringResource(Res.string.password)) },
            modifier = Modifier.testTag(CreateUserTestTags.PASSWORD_INPUT),
            enabled = !state.isLoading
        )

        CoreOutlinedTextField(
            value = state.role,
            onValueChange = onRoleChanged,
            label = { CoreBodyText(stringResource(Res.string.user_role)) },
            placeholder = { CoreBodyText(stringResource(Res.string.user_role)) },
            modifier = Modifier.testTag(CreateUserTestTags.ROLE_INPUT),
            enabled = !state.isLoading
        )

        CoreOutlinedTextField(
            value = state.status,
            onValueChange = onStatusChanged,
            label = { CoreBodyText(stringResource(Res.string.user_account_status)) },
            placeholder = { CoreBodyText(stringResource(Res.string.user_account_status)) },
            modifier = Modifier.testTag(CreateUserTestTags.STATUS_INPUT),
            enabled = !state.isLoading
        )

        CoreOutlinedTextField(
            value = state.authorityLevel,
            onValueChange = onAuthorityLevelChanged,
            label = { CoreBodyText(stringResource(Res.string.authority_level)) },
            placeholder = { CoreBodyText(stringResource(Res.string.authority_level)) },
            modifier = Modifier.testTag(CreateUserTestTags.AUTHORITY_LEVEL_INPUT),
            enabled = !state.isLoading
        )

        CoreButton(
            text = stringResource(if (state.isLoading) Res.string.saving else Res.string.create_user),
            onClick = onCreateClick,
            modifier = Modifier.testTag(CreateUserTestTags.CREATE_BUTTON),
            enabled = !state.isLoading
        )

        state.error?.let {
            CoreErrorText(
                text = it.toLocalizedMessage(),
                modifier = Modifier.testTag(CreateUserTestTags.ERROR_TEXT)
            )
        }
    }
}

@InternalApi
internal class CreateUserPreviewProvider : PreviewParameterProvider<CreateUserScreenState> {
    private val items: List<Pair<String, CreateUserScreenState>> = listOf(
        "Default" to CreateUserScreenState(),
        "Filled" to CreateUserScreenState(
            email = "user@example.com",
            password = "SecretPassword123!",
            role = "USER",
            status = "ACTIVE",
            authorityLevel = "0"
        ),
        "Loading" to CreateUserScreenState(
            email = "user@example.com",
            isLoading = true
        ),
        "Error" to CreateUserScreenState(
            email = "user@example.com",
            error = CommonError.Unknown()
        )
    )

    override val values: Sequence<CreateUserScreenState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun CreateUserScreenPreviewContent(state: CreateUserScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        CreateUserScreen(
            component = CreateUserComponentMock(initialState = state)
        )
    }
}

private val defaultCreateUserPreviewState = CreateUserScreenState(
    email = "user@example.com"
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StatesPreview(
    @PreviewParameter(CreateUserPreviewProvider::class) state: CreateUserScreenState
) {
    ScreenPreviewContainer {
        CreateUserScreenPreviewContent(state = state)
    }
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        CreateUserScreenPreviewContent(state = defaultCreateUserPreviewState)
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        CreateUserScreenPreviewContent(state = defaultCreateUserPreviewState)
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        CreateUserScreenPreviewContent(state = defaultCreateUserPreviewState)
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
