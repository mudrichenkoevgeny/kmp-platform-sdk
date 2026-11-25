package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.ic_back
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
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewFontScales
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewScreenSizes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewThemes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.preview.StorageFileImagePreview
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.rememberFilePickerLauncher
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.create.CreateUserComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.localization.LocalUserStrings
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserAccountStatus
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.core.common.ui.UiValues
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.category.StorageCategory
import org.jetbrains.compose.resources.painterResource
import io.github.mudrichenkoevgeny.kmp.core.common.Res as CommonRes

/**
 * Screen for administrative creation of a new user account.
 *
 * @param component Decompose controller for user creation form and state.
 * @param modifier Optional modifier.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun CreateUserScreen(
    component: CreateUserComponent,
    modifier: Modifier = Modifier
) {
    val state by component.state.subscribeAsState()
    val userUi = LocalUserStrings.current.ui

    val filePickerLauncher = rememberFilePickerLauncher { selectedFiles ->
        val localUri = selectedFiles.firstOrNull()?.localUri
        if (localUri != null) {
            val firstFile = selectedFiles.first()
            component.onAvatarFileSelected(firstFile)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    CoreScreenTitleText(
                        text = userUi.createUser,
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
                onEmailChange = component::onEmailChanged,
                onPasswordChange = component::onPasswordChanged,
                onNicknameChange = component::onNicknameChanged,
                onDisplayNameChange = component::onDisplayNameChanged,
                onBirthdateChange = component::onBirthdateChanged,
                onChangeAvatarClick = { filePickerLauncher.launch() },
                onRoleChange = component::onRoleChanged,
                onStatusChange = component::onStatusChanged,
                onAuthorityLevelChange = component::onAuthorityLevelChanged,
                onQuotaBytesChange = component::onQuotaBytesChanged,
                onMaxUploadSizeBytesChange = component::onMaxUploadSizeBytesChanged,
                onMaxPendingUploadsChange = component::onMaxPendingUploadsChanged,
                onCreateClick = component::onCreateClick
            )
        }
    }
}

@Composable
private fun CreateUserForm(
    state: CreateUserScreenState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNicknameChange: (String) -> Unit,
    onDisplayNameChange: (String) -> Unit,
    onBirthdateChange: (String) -> Unit,
    onChangeAvatarClick: () -> Unit,
    onRoleChange: (String) -> Unit,
    onStatusChange: (String) -> Unit,
    onAuthorityLevelChange: (String) -> Unit,
    onQuotaBytesChange: (String) -> Unit,
    onMaxUploadSizeBytesChange: (String) -> Unit,
    onMaxPendingUploadsChange: (String) -> Unit,
    onCreateClick: () -> Unit
) {
    val userUi = LocalUserStrings.current.ui

    Column(
        verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingMedium)
    ) {
        CreateUserAvatarHeader(
            avatarSourceUrl = state.selectedAvatarPayload?.localUri,
            onChangeAvatarClick = onChangeAvatarClick,
            isLoading = state.isLoading
        )

        CreateUserInfoSection(
            state = state,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onNicknameChange = onNicknameChange,
            onDisplayNameChange = onDisplayNameChange,
            onBirthdateChange = onBirthdateChange
        )

        CoreOutlinedTextField(
            value = state.role,
            onValueChange = onRoleChange,
            label = { CoreBodyText(userUi.user) },
            enabled = !state.isLoading,
            modifier = Modifier.testTag(CreateUserTestTags.ROLE_INPUT)
        )

        CoreOutlinedTextField(
            value = state.status,
            onValueChange = onStatusChange,
            label = { CoreBodyText(userUi.user) },
            enabled = !state.isLoading,
            modifier = Modifier.testTag(CreateUserTestTags.STATUS_INPUT)
        )

        CoreOutlinedTextField(
            value = state.authorityLevel,
            onValueChange = onAuthorityLevelChange,
            label = { CoreBodyText(userUi.authorityLevel) },
            placeholder = { CoreBodyText(userUi.authorityLevel) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            enabled = !state.isLoading,
            modifier = Modifier.testTag(CreateUserTestTags.AUTHORITY_LEVEL_INPUT)
        )

        CreateUserStorageConfigSection(
            state = state,
            onQuotaBytesChange = onQuotaBytesChange,
            onMaxUploadSizeBytesChange = onMaxUploadSizeBytesChange,
            onMaxPendingUploadsChange = onMaxPendingUploadsChange
        )

        if (state.error != null) {
            CoreErrorText(
                text = state.error.toLocalizedMessage(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(CreateUserTestTags.ERROR_TEXT)
            )
        }

        CoreButton(
            text = if (state.isLoading) userUi.saving else userUi.createUser,
            onClick = onCreateClick,
            enabled = !state.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(CreateUserTestTags.CREATE_BUTTON)
        )
    }
}

@Composable
private fun CreateUserAvatarHeader(
    avatarSourceUrl: String?,
    onChangeAvatarClick: () -> Unit,
    isLoading: Boolean
) {
    val userUi = LocalUserStrings.current.ui

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
                .size(CoreTheme.dimens.previewContainerHeight)
                .clip(CircleShape)
                .border(
                    width = CoreTheme.dimens.borderWidthThick,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = CircleShape
                ),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = CircleShape
        ) {
            if (!avatarSourceUrl.isNullOrBlank()) {
                StorageFileImagePreview(
                    imageUrl = avatarSourceUrl,
                    category = StorageCategory.IMAGE,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(CommonRes.drawable.ic_back),
                        contentDescription = null,
                        modifier = Modifier.size(CoreTheme.dimens.iconSizeXLarge),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(CoreTheme.dimens.paddingSmall))

        CoreButton(
            text = "${userUi.editProfileTitle} (${userUi.avatar})",
            onClick = onChangeAvatarClick,
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(CreateUserTestTags.AVATAR_CHANGE_BUTTON)
        )
    }
}

@Composable
private fun CreateUserInfoSection(
    state: CreateUserScreenState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNicknameChange: (String) -> Unit,
    onDisplayNameChange: (String) -> Unit,
    onBirthdateChange: (String) -> Unit
) {
    val userUi = LocalUserStrings.current.ui

    Column(
        verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingMedium)
    ) {
        CoreEmailTextField(
            value = state.email,
            onValueChange = onEmailChange,
            label = { CoreBodyText(userUi.email) },
            placeholder = { CoreBodyText(UiValues.Placeholder.EMAIL) },
            modifier = Modifier.testTag(CreateUserTestTags.EMAIL_INPUT),
            enabled = !state.isLoading
        )

        CoreOutlinedTextField(
            value = state.password,
            onValueChange = onPasswordChange,
            label = { CoreBodyText(userUi.password) },
            placeholder = { CoreBodyText(userUi.password) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.testTag(CreateUserTestTags.PASSWORD_INPUT),
            enabled = !state.isLoading
        )

        CoreOutlinedTextField(
            value = state.nickname,
            onValueChange = onNicknameChange,
            label = { CoreBodyText(userUi.userLabel) },
            placeholder = { CoreBodyText(userUi.userLabel) },
            modifier = Modifier.testTag(CreateUserTestTags.NICKNAME_INPUT),
            enabled = !state.isLoading
        )

        CoreOutlinedTextField(
            value = state.displayName,
            onValueChange = onDisplayNameChange,
            label = { CoreBodyText(userUi.user) },
            placeholder = { CoreBodyText(userUi.user) },
            modifier = Modifier.testTag(CreateUserTestTags.DISPLAY_NAME_INPUT),
            enabled = !state.isLoading
        )

        CoreOutlinedTextField(
            value = state.birthdate,
            onValueChange = onBirthdateChange,
            label = { CoreBodyText(userUi.birthdate) },
            placeholder = { CoreBodyText(userUi.birthdate) },
            modifier = Modifier.testTag(CreateUserTestTags.BIRTHDATE_INPUT),
            enabled = !state.isLoading
        )
    }
}

@Composable
private fun CreateUserStorageConfigSection(
    state: CreateUserScreenState,
    onQuotaBytesChange: (String) -> Unit,
    onMaxUploadSizeBytesChange: (String) -> Unit,
    onMaxPendingUploadsChange: (String) -> Unit
) {
    val userUi = LocalUserStrings.current.ui

    Column(
        verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingMedium)
    ) {
        CoreOutlinedTextField(
            value = state.quotaBytes,
            onValueChange = onQuotaBytesChange,
            label = { CoreBodyText(userUi.user) },
            placeholder = { CoreBodyText(userUi.user) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            enabled = !state.isLoading,
            modifier = Modifier.testTag(CreateUserTestTags.QUOTA_BYTES_INPUT)
        )

        CoreOutlinedTextField(
            value = state.maxUploadSizeBytes,
            onValueChange = onMaxUploadSizeBytesChange,
            label = { CoreBodyText(userUi.user) },
            placeholder = { CoreBodyText(userUi.user) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            enabled = !state.isLoading,
            modifier = Modifier.testTag(CreateUserTestTags.MAX_UPLOAD_SIZE_BYTES_INPUT)
        )

        CoreOutlinedTextField(
            value = state.maxPendingUploads,
            onValueChange = onMaxPendingUploadsChange,
            label = { CoreBodyText(userUi.user) },
            placeholder = { CoreBodyText(userUi.user) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            enabled = !state.isLoading,
            modifier = Modifier.testTag(CreateUserTestTags.MAX_PENDING_UPLOADS_INPUT)
        )
    }
}

@InternalApi
internal class CreateUserPreviewProvider : PreviewParameterProvider<CreateUserScreenState> {
    private val sampleState = CreateUserScreenState(
        email = "newuser@example.com",
        password = "password123",
        nickname = "newuser",
        displayName = "New User",
        role = UserRole.STAFF.value,
        status = UserAccountStatus.ACTIVE.value,
        authorityLevel = "1"
    )

    private val items: List<Pair<String, CreateUserScreenState>> = listOf(
        "Form" to sampleState,
        "Loading" to sampleState.copy(isLoading = true),
        "Error" to CreateUserScreenState(error = CommonError.Unknown())
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
    email = "newuser@example.com",
    password = "password123",
    nickname = "newuser",
    displayName = "New User",
    role = UserRole.STAFF.value,
    status = UserAccountStatus.ACTIVE.value,
    authorityLevel = "1"
)

@InternalApi
@PreviewScreenSizes
@Composable
private fun StatesPreview(
    @PreviewParameter(CreateUserPreviewProvider::class) state: CreateUserScreenState
) {
    ScreenPreviewContainer {
        CreateUserScreenPreviewContent(state = state)
    }
}

@InternalApi
@PreviewScreenSizes
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        CreateUserScreenPreviewContent(state = defaultCreateUserPreviewState)
    }
}

@InternalApi
@PreviewThemes
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        CreateUserScreenPreviewContent(state = defaultCreateUserPreviewState)
    }
}

@InternalApi
@PreviewFontScales
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        CreateUserScreenPreviewContent(state = defaultCreateUserPreviewState)
    }
}

public object CreateUserTestTags {
    public const val TITLE: String = "CreateUser_Title"
    public const val BACK_BUTTON: String = "CreateUser_BackButton"
    public const val AVATAR_CHANGE_BUTTON: String = "CreateUser_AvatarChangeButton"
    public const val EMAIL_INPUT: String = "CreateUser_EmailInput"
    public const val PASSWORD_INPUT: String = "CreateUser_PasswordInput"
    public const val NICKNAME_INPUT: String = "CreateUser_NicknameInput"
    public const val DISPLAY_NAME_INPUT: String = "CreateUser_DisplayNameInput"
    public const val BIRTHDATE_INPUT: String = "CreateUser_BirthdateInput"
    public const val ROLE_INPUT: String = "CreateUser_RoleInput"
    public const val STATUS_INPUT: String = "CreateUser_StatusInput"
    public const val AUTHORITY_LEVEL_INPUT: String = "CreateUser_AuthorityLevelInput"
    public const val QUOTA_BYTES_INPUT: String = "CreateUser_QuotaBytesInput"
    public const val MAX_UPLOAD_SIZE_BYTES_INPUT: String = "CreateUser_MaxUploadSizeBytesInput"
    public const val MAX_PENDING_UPLOADS_INPUT: String = "CreateUser_MaxPendingUploadsInput"
    public const val ERROR_TEXT: String = "CreateUser_ErrorText"
    public const val CREATE_BUTTON: String = "CreateUser_CreateButton"
}
