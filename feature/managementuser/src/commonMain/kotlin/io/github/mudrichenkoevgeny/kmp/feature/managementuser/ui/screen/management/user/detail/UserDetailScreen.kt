package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.ic_back
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.localization.KmpLocalization
import io.github.mudrichenkoevgeny.kmp.core.common.localization.LocalCommonStrings
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.time.formatInstantToDateTime
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreBackButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreTextButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.container.CoreScrollableScreenContent
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
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.preview.StorageFileImagePreview
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.rememberFilePickerLauncher
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.detail.UserDetailComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.localization.LocalUserStrings
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.accountlockout.AccountLockoutType
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.category.StorageCategory
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.localization.StorageModuleStrings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.accountstatus.UserAccountStatus
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.user.createTestUserPrivate
import org.jetbrains.compose.resources.painterResource
import io.github.mudrichenkoevgeny.kmp.core.common.Res as CommonRes

/**
 * Administrative user detail screen: displays user details and management action controls.
 *
 * @param component Component driving screen state and navigation callbacks.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(component: UserDetailComponent) {
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
                        text = userUi.userDetailsTitle,
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
                        onNicknameChanged = component::onNicknameChanged,
                        onDisplayNameChanged = component::onDisplayNameChanged,
                        onBirthdateChanged = component::onBirthdateChanged,
                        onChangeAvatarClick = { filePickerLauncher.launch() },
                        onAuthorityLevelChanged = component::onAuthorityLevelChanged,
                        onAccountStatusChanged = component::onAccountStatusChanged,
                        onLockoutTypeChanged = component::onLockoutTypeChanged,
                        onTemporaryLockoutUntilChanged = component::onTemporaryLockoutUntilChanged,
                        onQuotaBytesChanged = component::onQuotaBytesChanged,
                        onMaxUploadSizeBytesChanged = component::onMaxUploadSizeBytesChanged,
                        onMaxPendingUploadsChanged = component::onMaxPendingUploadsChanged,
                        onUpdateClick = component::onUpdateClick,
                        onDeleteClick = component::onDeleteClick,
                        onConfirmDeleteClick = component::onConfirmDeleteClick,
                        onDismissDeleteDialog = component::onDismissDeleteDialog,
                        onDisableTotpClick = component::onDisableTotpClick,
                        onSessionsClick = component::onSessionsClick,
                        onIdentifiersClick = component::onIdentifiersClick,
                        onFilesClick = component::onFilesClick,
                        onAuditEventsClick = component::onAuditEventsClick
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(
    state: UserDetailScreenState.Content,
    onNicknameChanged: (String) -> Unit,
    onDisplayNameChanged: (String) -> Unit,
    onBirthdateChanged: (String) -> Unit,
    onChangeAvatarClick: () -> Unit,
    onAuthorityLevelChanged: (String) -> Unit,
    onAccountStatusChanged: (String) -> Unit,
    onLockoutTypeChanged: (String) -> Unit,
    onTemporaryLockoutUntilChanged: (String) -> Unit,
    onQuotaBytesChanged: (String) -> Unit,
    onMaxUploadSizeBytesChanged: (String) -> Unit,
    onMaxPendingUploadsChanged: (String) -> Unit,
    onUpdateClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onConfirmDeleteClick: () -> Unit,
    onDismissDeleteDialog: () -> Unit,
    onDisableTotpClick: () -> Unit,
    onSessionsClick: () -> Unit,
    onIdentifiersClick: () -> Unit,
    onFilesClick: () -> Unit,
    onAuditEventsClick: () -> Unit
) {
    val userUi = LocalUserStrings.current.ui
    val commonUi = LocalCommonStrings.current.ui
    val storageUi = remember { StorageModuleStrings(KmpLocalization.resolver, KmpLocalization.currentLocale).ui }
    val notAvailableText = commonUi.denied
    val createdAtText = formatInstantToDateTime(state.user.createdAt) ?: state.user.createdAt.toString()
    val lastLoginText = state.user.lastLoginAt?.let { formatInstantToDateTime(it) } ?: notAvailableText
    val lastActiveText = state.user.lastActiveAt?.let { formatInstantToDateTime(it) } ?: notAvailableText

    CoreScrollableScreenContent {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            val avatarSourceUrl = state.selectedAvatarPayload?.localUri ?: state.avatarUrl
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

        Spacer(modifier = Modifier.height(CoreTheme.dimens.paddingSmall))

        CoreButton(
            text = "${userUi.editProfileTitle} (${userUi.avatar})",
            onClick = onChangeAvatarClick,
            enabled = !state.isSaving && !state.isDeleting,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(UserDetailTestTags.AVATAR_CHANGE_BUTTON)
        )

        CoreTitleText(
            text = "${commonUi.userId}: ${state.user.id.value}",
            style = MaterialTheme.typography.titleMedium
        )
        CoreBodyText(
            text = "${userUi.userRole}: ${state.user.role.name}"
        )
        CoreBodyText(
            text = userUi.totpEnabledLabel(state.user.isTotpEnabled.toString())
        )

        if (state.user.isTotpEnabled) {
            CoreButton(
                text = if (state.isDisablingTotp) userUi.disablingTotp else userUi.disableTotp,
                onClick = onDisableTotpClick,
                modifier = Modifier.testTag(UserDetailTestTags.DISABLE_TOTP_BUTTON),
                enabled = !state.isSaving && !state.isDeleting && !state.isDisablingTotp && !state.isSelf
            )

            state.disableTotpError?.let {
                CoreErrorText(
                    text = it.toLocalizedMessage(),
                    modifier = Modifier.testTag(UserDetailTestTags.DISABLE_TOTP_ERROR_TEXT)
                )
            }
        }
        CoreBodyText(
            text = userUi.createdAtLabel(createdAtText)
        )
        CoreBodyText(
            text = userUi.lastLoginAtLabel(lastLoginText)
        )
        CoreBodyText(
            text = userUi.lastActiveAtLabel(lastActiveText)
        )

        if (state.user.accountStatus == UserAccountStatus.PENDING_DELETION && state.user.scheduledPermanentDeletionAt != null) {
            val scheduledDeletionText = formatInstantToDateTime(state.user.scheduledPermanentDeletionAt)
                ?: state.user.scheduledPermanentDeletionAt.toString()
            CoreErrorText(
                text = userUi.scheduledDeletionAtLabel(scheduledDeletionText)
            )
        }

        CoreButton(
            text = userUi.userSessions,
            onClick = onSessionsClick,
            modifier = Modifier.testTag(UserDetailTestTags.SESSIONS_BUTTON)
        )

        CoreButton(
            text = userUi.userIdentifiers,
            onClick = onIdentifiersClick,
            modifier = Modifier.testTag(UserDetailTestTags.IDENTIFIERS_BUTTON)
        )

        CoreButton(
            text = storageUi.storageFileListTitle,
            onClick = onFilesClick,
            modifier = Modifier.testTag(UserDetailTestTags.FILES_BUTTON)
        )

        CoreButton(
            text = userUi.userDetailAuditEvents,
            onClick = onAuditEventsClick,
            modifier = Modifier.testTag(UserDetailTestTags.AUDIT_EVENTS_BUTTON),
            enabled = !state.isSaving && !state.isDeleting
        )

        CoreOutlinedTextField(
            value = state.nicknameInput,
            onValueChange = onNicknameChanged,
            label = { CoreBodyText(userUi.userLabel) },
            placeholder = { CoreBodyText(userUi.userLabel) },
            modifier = Modifier.testTag(UserDetailTestTags.NICKNAME_INPUT),
            enabled = !state.isSaving && !state.isDeleting
        )

        CoreOutlinedTextField(
            value = state.displayNameInput,
            onValueChange = onDisplayNameChanged,
            label = { CoreBodyText(userUi.user) },
            placeholder = { CoreBodyText(userUi.user) },
            modifier = Modifier.testTag(UserDetailTestTags.DISPLAY_NAME_INPUT),
            enabled = !state.isSaving && !state.isDeleting
        )

        CoreOutlinedTextField(
            value = state.birthdateInput,
            onValueChange = onBirthdateChanged,
            label = { CoreBodyText(userUi.birthdate) },
            placeholder = { CoreBodyText(userUi.birthdate) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(UserDetailTestTags.BIRTHDATE_INPUT),
            enabled = !state.isSaving && !state.isDeleting
        )

        AccountStatusDropdown(
            selectedAccountStatus = state.accountStatusInput,
            onAccountStatusSelected = onAccountStatusChanged,
            enabled = !state.isSaving && !state.isDeleting && !state.isSelf
        )

        CoreOutlinedTextField(
            value = state.authorityLevelInput,
            onValueChange = onAuthorityLevelChanged,
            label = { CoreBodyText(userUi.authorityLevel) },
            placeholder = { CoreBodyText(userUi.authorityLevel) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(UserDetailTestTags.AUTHORITY_LEVEL_INPUT),
            enabled = !state.isSaving && !state.isDeleting && !state.isSelf
        )

        LockoutTypeDropdown(
            selectedLockoutType = state.lockoutTypeInput,
            onLockoutTypeSelected = onLockoutTypeChanged,
            enabled = !state.isSaving && !state.isDeleting && !state.isSelf
        )

        CoreOutlinedTextField(
            value = state.temporaryLockoutUntilInput,
            onValueChange = onTemporaryLockoutUntilChanged,
            label = { CoreBodyText(commonUi.lockoutUntil) },
            placeholder = { CoreBodyText(commonUi.lockoutUntil) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(UserDetailTestTags.TEMPORARY_LOCKOUT_UNTIL_INPUT),
            enabled = !state.isSaving && !state.isDeleting && !state.isSelf
        )

        CoreTitleText(
            text = userUi.customStorageConfiguration,
            style = MaterialTheme.typography.titleMedium
        )

        CoreOutlinedTextField(
            value = state.quotaBytesInput,
            onValueChange = onQuotaBytesChanged,
            label = { CoreBodyText(storageUi.storageQuotaBytes) },
            placeholder = { CoreBodyText(storageUi.defaultNull) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(UserDetailTestTags.QUOTA_BYTES_INPUT),
            enabled = !state.isSaving && !state.isDeleting && !state.isSelf
        )

        CoreOutlinedTextField(
            value = state.maxUploadSizeBytesInput,
            onValueChange = onMaxUploadSizeBytesChanged,
            label = { CoreBodyText(storageUi.storageMaxUploadSizeBytes) },
            placeholder = { CoreBodyText(storageUi.defaultNull) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(UserDetailTestTags.MAX_UPLOAD_SIZE_BYTES_INPUT),
            enabled = !state.isSaving && !state.isDeleting && !state.isSelf
        )

        CoreOutlinedTextField(
            value = state.maxPendingUploadsInput,
            onValueChange = onMaxPendingUploadsChanged,
            label = { CoreBodyText(storageUi.storageMaxPendingUploads) },
            placeholder = { CoreBodyText(storageUi.defaultNull) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.testTag(UserDetailTestTags.MAX_PENDING_UPLOADS_INPUT),
            enabled = !state.isSaving && !state.isDeleting && !state.isSelf
        )

        state.saveError?.let {
            CoreErrorText(
                text = it.toLocalizedMessage(),
                modifier = Modifier.testTag(UserDetailTestTags.SAVE_ERROR_TEXT)
            )
        }

        CoreButton(
            text = if (state.isSaving) userUi.saving else userUi.updateUser,
            onClick = onUpdateClick,
            modifier = Modifier.testTag(UserDetailTestTags.UPDATE_BUTTON),
            enabled = state.hasChanges && !state.isSaving && !state.isDeleting
        )

        CoreButton(
            text = if (state.isDeleting) userUi.saving else userUi.deleteUser,
            onClick = onDeleteClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            ),
            modifier = Modifier.testTag(UserDetailTestTags.DELETE_BUTTON),
            enabled = !state.isSaving && !state.isDeleting && !state.isSelf
        )

        state.deleteError?.let {
            CoreErrorText(
                text = it.toLocalizedMessage(),
                modifier = Modifier.testTag(UserDetailTestTags.DELETE_ERROR_TEXT)
            )
        }

        if (state.isDeleteConfirmationVisible) {
            AlertDialog(
                onDismissRequest = onDismissDeleteDialog,
                title = { CoreTitleText(text = userUi.dialogConfirmTitle) },
                text = { CoreBodyText(text = userUi.deleteUser) },
                confirmButton = {
                    CoreTextButton(
                        text = userUi.dialogConfirm,
                        onClick = onConfirmDeleteClick,
                        colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.error)
                    )
                },
                dismissButton = {
                    CoreTextButton(
                        text = userUi.dialogCancel,
                        onClick = onDismissDeleteDialog
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountStatusDropdown(
    selectedAccountStatus: String,
    onAccountStatusSelected: (String) -> Unit,
    enabled: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (enabled) expanded = !expanded }
    ) {
        CoreOutlinedTextField(
            value = selectedAccountStatus,
            onValueChange = {},
            label = { CoreBodyText(LocalUserStrings.current.ui.userAccountStatus) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = enabled)
                .testTag(UserDetailTestTags.ACCOUNT_STATUS_DROPDOWN),
            enabled = enabled
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            UserAccountStatus.entries.forEach { status ->
                DropdownMenuItem(
                    text = { CoreBodyText(status.name) },
                    onClick = {
                        onAccountStatusSelected(status.name)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LockoutTypeDropdown(
    selectedLockoutType: String,
    onLockoutTypeSelected: (String) -> Unit,
    enabled: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (enabled) expanded = !expanded }
    ) {
        CoreOutlinedTextField(
            value = selectedLockoutType,
            onValueChange = {},
            label = { CoreBodyText(LocalCommonStrings.current.ui.lockoutType) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = enabled)
                .testTag(UserDetailTestTags.LOCKOUT_TYPE_DROPDOWN),
            enabled = enabled
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            AccountLockoutType.entries.forEach { lockoutType ->
                DropdownMenuItem(
                    text = { CoreBodyText(lockoutType.name) },
                    onClick = {
                        onLockoutTypeSelected(lockoutType.name)
                        expanded = false
                    }
                )
            }
        }
    }
}

@InternalApi
internal class UserDetailPreviewProvider : PreviewParameterProvider<UserDetailScreenState> {
    private val sampleUser = createTestUserPrivate()
    private val sampleContent = UserDetailScreenState.Content(
        user = sampleUser,
        authorityLevelInput = "10",
        accountStatusInput = "ACTIVE"
    )

    private val items: List<Pair<String, UserDetailScreenState>> = listOf(
        "Content" to sampleContent,
        "Self" to sampleContent.copy(isSelf = true),
        "Saving" to sampleContent.copy(isSaving = true),
        "Save Error" to sampleContent.copy(saveError = CommonError.Unknown()),
        "Deleting" to sampleContent.copy(isDeleting = true),
        "Delete Error" to sampleContent.copy(deleteError = CommonError.Unknown()),
        "Disabling TOTP" to sampleContent.copy(isDisablingTotp = true),
        "Disable TOTP Error" to sampleContent.copy(disableTotpError = CommonError.Unknown()),
        "Delete Dialog" to sampleContent.copy(isDeleteConfirmationVisible = true),
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
@ScreenSizePreviews
@Composable
private fun ScreenSizePreview(
    @PreviewParameter(UserDetailPreviewProvider::class) state: UserDetailScreenState
) {
    ScreenPreviewContainer {
        UserDetailScreenPreviewContent(state = state)
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview(
    @PreviewParameter(UserDetailPreviewProvider::class) state: UserDetailScreenState
) {
    ScreenPreviewContainer {
        UserDetailScreenPreviewContent(state = state)
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview(
    @PreviewParameter(UserDetailPreviewProvider::class) state: UserDetailScreenState
) {
    ScreenPreviewContainer {
        UserDetailScreenPreviewContent(state = state)
    }
}

object UserDetailTestTags {
    const val TITLE = "UserDetail_Title"
    const val BACK_BUTTON = "UserDetail_BackButton"
    const val GLOBAL_ERROR = "UserDetail_GlobalError"

    const val DISABLE_TOTP_BUTTON = "UserDetail_DisableTotpButton"
    const val DISABLE_TOTP_ERROR_TEXT = "UserDetail_DisableTotpErrorText"
    const val SESSIONS_BUTTON = "UserDetail_SessionsButton"
    const val IDENTIFIERS_BUTTON = "UserDetail_IdentifiersButton"
    const val FILES_BUTTON = "UserDetail_FilesButton"
    const val AUDIT_EVENTS_BUTTON = "UserDetail_AuditEventsButton"

    const val NICKNAME_INPUT = "UserDetail_NicknameInput"
    const val DISPLAY_NAME_INPUT = "UserDetail_DisplayNameInput"
    const val AVATAR_CHANGE_BUTTON = "UserDetail_AvatarChangeButton"
    const val BIRTHDATE_INPUT = "UserDetail_BirthdateInput"

    const val ACCOUNT_STATUS_DROPDOWN = "UserDetail_AccountStatusDropdown"
    const val AUTHORITY_LEVEL_INPUT = "UserDetail_AuthorityLevelInput"
    const val LOCKOUT_TYPE_DROPDOWN = "UserDetail_LockoutTypeDropdown"
    const val TEMPORARY_LOCKOUT_UNTIL_INPUT = "UserDetail_TemporaryLockoutUntilInput"

    const val QUOTA_BYTES_INPUT = "UserDetail_QuotaBytesInput"
    const val MAX_UPLOAD_SIZE_BYTES_INPUT = "UserDetail_MaxUploadSizeBytesInput"
    const val MAX_PENDING_UPLOADS_INPUT = "UserDetail_MaxPendingUploadsInput"

    const val SAVE_ERROR_TEXT = "UserDetail_SaveErrorText"
    const val UPDATE_BUTTON = "UserDetail_UpdateButton"
    const val DELETE_BUTTON = "UserDetail_DeleteButton"
    const val DELETE_ERROR_TEXT = "UserDetail_DeleteErrorText"
}
