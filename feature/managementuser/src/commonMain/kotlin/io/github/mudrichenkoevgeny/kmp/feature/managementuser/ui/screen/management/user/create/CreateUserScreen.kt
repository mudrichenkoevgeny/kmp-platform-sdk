package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.ic_back
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.localization.KmpLocalization
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreBackButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.container.CoreScrollableScreenContent
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input.CoreEmailTextField
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input.CoreOutlinedTextField
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreBodyText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreErrorText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreScreenTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewFontScales
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewScreenSizes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewThemes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.preview.StorageFileImagePreview
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.rememberFilePickerLauncher
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.create.CreateUserComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.localization.LocalUserStrings
import io.github.mudrichenkoevgeny.shared.foundation.core.common.ui.UiValues
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.category.StorageCategory
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.localization.StorageModuleStrings
import org.jetbrains.compose.resources.painterResource
import io.github.mudrichenkoevgeny.kmp.core.common.Res as CommonRes

/**
 * Screen for administrative creation of a new user account.
 *
 * @param component Decompose controller for user creation form and state.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun CreateUserScreen(component: CreateUserComponent) {
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
        topBar = {
            TopAppBar(
                title = {
                    CoreScreenTitleText(
                        text = userUi.createUserTitle,
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
                onNicknameChanged = component::onNicknameChanged,
                onDisplayNameChanged = component::onDisplayNameChanged,
                onBirthdateChanged = component::onBirthdateChanged,
                onChangeAvatarClick = { filePickerLauncher.launch() },
                onRoleChanged = component::onRoleChanged,
                onStatusChanged = component::onStatusChanged,
                onAuthorityLevelChanged = component::onAuthorityLevelChanged,
                onQuotaBytesChanged = component::onQuotaBytesChanged,
                onMaxUploadSizeBytesChanged = component::onMaxUploadSizeBytesChanged,
                onMaxPendingUploadsChanged = component::onMaxPendingUploadsChanged,
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
    onNicknameChanged: (String) -> Unit,
    onDisplayNameChanged: (String) -> Unit,
    onBirthdateChanged: (String) -> Unit,
    onChangeAvatarClick: () -> Unit,
    onRoleChanged: (String) -> Unit,
    onStatusChanged: (String) -> Unit,
    onAuthorityLevelChanged: (String) -> Unit,
    onQuotaBytesChanged: (String) -> Unit,
    onMaxUploadSizeBytesChanged: (String) -> Unit,
    onMaxPendingUploadsChanged: (String) -> Unit,
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
            onEmailChanged = onEmailChanged,
            onPasswordChanged = onPasswordChanged,
            onNicknameChanged = onNicknameChanged,
            onDisplayNameChanged = onDisplayNameChanged,
            onBirthdateChanged = onBirthdateChanged
        )

        CreateUserRoleSection(
            state = state,
            onRoleChanged = onRoleChanged,
            onStatusChanged = onStatusChanged,
            onAuthorityLevelChanged = onAuthorityLevelChanged
        )

        CreateUserStorageSection(
            state = state,
            onQuotaBytesChanged = onQuotaBytesChanged,
            onMaxUploadSizeBytesChanged = onMaxUploadSizeBytesChanged,
            onMaxPendingUploadsChanged = onMaxPendingUploadsChanged
        )

        CoreButton(
            text = if (state.isLoading) userUi.saving else userUi.createUser,
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

@Composable
private fun CreateUserAvatarHeader(
    avatarSourceUrl: String?,
    onChangeAvatarClick: () -> Unit,
    isLoading: Boolean
) {
    val userUi = LocalUserStrings.current.ui

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
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
    }

    CoreButton(
        text = "${userUi.editProfileTitle} (${userUi.avatar})",
        onClick = onChangeAvatarClick,
        enabled = !isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .testTag(CreateUserTestTags.AVATAR_CHANGE_BUTTON)
    )
}

@Composable
private fun CreateUserInfoSection(
    state: CreateUserScreenState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onNicknameChanged: (String) -> Unit,
    onDisplayNameChanged: (String) -> Unit,
    onBirthdateChanged: (String) -> Unit
) {
    val userUi = LocalUserStrings.current.ui

    CoreEmailTextField(
        value = state.email,
        onValueChange = onEmailChanged,
        label = { CoreBodyText(userUi.email) },
        placeholder = { CoreBodyText(UiValues.Placeholder.EMAIL) },
        modifier = Modifier.testTag(CreateUserTestTags.EMAIL_INPUT),
        enabled = !state.isLoading
    )

    CoreOutlinedTextField(
        value = state.password,
        onValueChange = onPasswordChanged,
        label = { CoreBodyText(userUi.password) },
        placeholder = { CoreBodyText(userUi.password) },
        modifier = Modifier.testTag(CreateUserTestTags.PASSWORD_INPUT),
        enabled = !state.isLoading
    )

    CoreOutlinedTextField(
        value = state.nickname,
        onValueChange = onNicknameChanged,
        label = { CoreBodyText(userUi.userLabel) },
        placeholder = { CoreBodyText(userUi.userLabel) },
        modifier = Modifier.testTag(CreateUserTestTags.NICKNAME_INPUT),
        enabled = !state.isLoading
    )

    CoreOutlinedTextField(
        value = state.displayName,
        onValueChange = onDisplayNameChanged,
        label = { CoreBodyText(userUi.user) },
        placeholder = { CoreBodyText(userUi.user) },
        modifier = Modifier.testTag(CreateUserTestTags.DISPLAY_NAME_INPUT),
        enabled = !state.isLoading
    )

    CoreOutlinedTextField(
        value = state.birthdate,
        onValueChange = onBirthdateChanged,
        label = { CoreBodyText(userUi.birthdate) },
        placeholder = { CoreBodyText(userUi.birthdate) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.testTag(CreateUserTestTags.BIRTHDATE_INPUT),
        enabled = !state.isLoading
    )
}

@Composable
private fun CreateUserRoleSection(
    state: CreateUserScreenState,
    onRoleChanged: (String) -> Unit,
    onStatusChanged: (String) -> Unit,
    onAuthorityLevelChanged: (String) -> Unit
) {
    val userUi = LocalUserStrings.current.ui

    CoreOutlinedTextField(
        value = state.role,
        onValueChange = onRoleChanged,
        label = { CoreBodyText(userUi.userRole) },
        placeholder = { CoreBodyText(userUi.userRole) },
        modifier = Modifier.testTag(CreateUserTestTags.ROLE_INPUT),
        enabled = !state.isLoading
    )

    CoreOutlinedTextField(
        value = state.status,
        onValueChange = onStatusChanged,
        label = { CoreBodyText(userUi.userAccountStatus) },
        placeholder = { CoreBodyText(userUi.userAccountStatus) },
        modifier = Modifier.testTag(CreateUserTestTags.STATUS_INPUT),
        enabled = !state.isLoading
    )

    CoreOutlinedTextField(
        value = state.authorityLevel,
        onValueChange = onAuthorityLevelChanged,
        label = { CoreBodyText(userUi.authorityLevel) },
        placeholder = { CoreBodyText(userUi.authorityLevel) },
        modifier = Modifier.testTag(CreateUserTestTags.AUTHORITY_LEVEL_INPUT),
        enabled = !state.isLoading
    )
}

@Composable
private fun CreateUserStorageSection(
    state: CreateUserScreenState,
    onQuotaBytesChanged: (String) -> Unit,
    onMaxUploadSizeBytesChanged: (String) -> Unit,
    onMaxPendingUploadsChanged: (String) -> Unit
) {
    val userUi = LocalUserStrings.current.ui
    val storageUi = remember { StorageModuleStrings(KmpLocalization.resolver, KmpLocalization.currentLocale).ui }

    CoreTitleText(
        text = userUi.customStorageConfiguration,
        style = MaterialTheme.typography.titleMedium
    )

    CoreOutlinedTextField(
        value = state.quotaBytes,
        onValueChange = onQuotaBytesChanged,
        label = { CoreBodyText(storageUi.storageQuotaBytes) },
        placeholder = { CoreBodyText(storageUi.defaultNull) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.testTag(CreateUserTestTags.QUOTA_BYTES_INPUT),
        enabled = !state.isLoading
    )

    CoreOutlinedTextField(
        value = state.maxUploadSizeBytes,
        onValueChange = onMaxUploadSizeBytesChanged,
        label = { CoreBodyText(storageUi.storageMaxUploadSizeBytes) },
        placeholder = { CoreBodyText(storageUi.defaultNull) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.testTag(CreateUserTestTags.MAX_UPLOAD_SIZE_BYTES_INPUT),
        enabled = !state.isLoading
    )

    CoreOutlinedTextField(
        value = state.maxPendingUploads,
        onValueChange = onMaxPendingUploadsChanged,
        label = { CoreBodyText(storageUi.storageMaxPendingUploads) },
        placeholder = { CoreBodyText(storageUi.defaultNull) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.testTag(CreateUserTestTags.MAX_PENDING_UPLOADS_INPUT),
        enabled = !state.isLoading
    )
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
            authorityLevel = "0",
            quotaBytes = "10737418240",
            maxUploadSizeBytes = "104857600",
            maxPendingUploads = "5"
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

@InternalApi
@PreviewScreenSizes
@Composable
private fun ScreenSizePreview(
    @PreviewParameter(CreateUserPreviewProvider::class) state: CreateUserScreenState
) {
    ScreenPreviewContainer {
        CreateUserScreenPreviewContent(state = state)
    }
}

@InternalApi
@PreviewThemes
@Composable
private fun ThemePreview(
    @PreviewParameter(CreateUserPreviewProvider::class) state: CreateUserScreenState
) {
    ScreenPreviewContainer {
        CreateUserScreenPreviewContent(state = state)
    }
}

@InternalApi
@PreviewFontScales
@Composable
private fun FontScalePreview(
    @PreviewParameter(CreateUserPreviewProvider::class) state: CreateUserScreenState
) {
    ScreenPreviewContainer {
        CreateUserScreenPreviewContent(state = state)
    }
}

public object CreateUserTestTags {
    public const val TITLE: String = "CreateUser_Title"
    public const val BACK_BUTTON: String = "CreateUser_BackButton"
    public const val EMAIL_INPUT: String = "CreateUser_EmailInput"
    public const val PASSWORD_INPUT: String = "CreateUser_PasswordInput"
    public const val NICKNAME_INPUT: String = "CreateUser_NicknameInput"
    public const val DISPLAY_NAME_INPUT: String = "CreateUser_DisplayNameInput"
    public const val AVATAR_CHANGE_BUTTON: String = "CreateUser_AvatarChangeButton"
    public const val BIRTHDATE_INPUT: String = "CreateUser_BirthdateInput"
    public const val ROLE_INPUT: String = "CreateUser_RoleInput"
    public const val STATUS_INPUT: String = "CreateUser_StatusInput"
    public const val AUTHORITY_LEVEL_INPUT: String = "CreateUser_AuthorityLevelInput"
    public const val QUOTA_BYTES_INPUT: String = "CreateUser_QuotaBytesInput"
    public const val MAX_UPLOAD_SIZE_BYTES_INPUT: String = "CreateUser_MaxUploadSizeBytesInput"
    public const val MAX_PENDING_UPLOADS_INPUT: String = "CreateUser_MaxPendingUploadsInput"
    public const val CREATE_BUTTON: String = "CreateUser_CreateButton"
    public const val ERROR_TEXT: String = "CreateUser_ErrorText"
}
