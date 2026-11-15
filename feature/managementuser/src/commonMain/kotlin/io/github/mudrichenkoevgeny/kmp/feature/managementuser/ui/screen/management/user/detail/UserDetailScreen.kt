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
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewFontScales
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewScreenSizes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewThemes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
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
public fun UserDetailScreen(component: UserDetailComponent) {
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
    val avatarSourceUrl = state.selectedAvatarPayload?.localUri ?: state.avatarUrl

    CoreScrollableScreenContent {
        UserDetailAvatarHeader(
            avatarSourceUrl = avatarSourceUrl,
            onChangeAvatarClick = onChangeAvatarClick,
            enabled = !state.isSaving && !state.isDeleting
        )

        UserDetailOverviewSection(
            state = state,
            onDisableTotpClick = onDisableTotpClick
        )

        UserDetailNavigationActions(
            onSessionsClick = onSessionsClick,
            onIdentifiersClick = onIdentifiersClick,
            onFilesClick = onFilesClick,
            onAuditEventsClick = onAuditEventsClick,
            auditEventsEnabled = !state.isSaving && !state.isDeleting
        )

        UserDetailEditableFieldsSection(
            state = state,
            onNicknameChanged = onNicknameChanged,
            onDisplayNameChanged = onDisplayNameChanged,
            onBirthdateChanged = onBirthdateChanged,
            onAuthorityLevelChanged = onAuthorityLevelChanged,
            onAccountStatusChanged = onAccountStatusChanged,
            onLockoutTypeChanged = onLockoutTypeChanged,
            onTemporaryLockoutUntilChanged = onTemporaryLockoutUntilChanged,
            onQuotaBytesChanged = onQuotaBytesChanged,
            onMaxUploadSizeBytesChanged = onMaxUploadSizeBytesChanged,
            onMaxPendingUploadsChanged = onMaxPendingUploadsChanged
        )

        UserDetailActionsSection(
            state = state,
            onUpdateClick = onUpdateClick,
            onDeleteClick = onDeleteClick,
            onConfirmDeleteClick = onConfirmDeleteClick,
            onDismissDeleteDialog = onDismissDeleteDialog
        )
    }
}

@Composable
private fun UserDetailAvatarHeader(
    avatarSourceUrl: String?,
    onChangeAvatarClick: () -> Unit,
    enabled: Boolean
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

    Spacer(modifier = Modifier.height(CoreTheme.dimens.paddingSmall))

    CoreButton(
        text = "${userUi.editProfileTitle} (${userUi.avatar})",
        onClick = onChangeAvatarClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .testTag(UserDetailTestTags.AVATAR_CHANGE_BUTTON)
    )
}

@Composable
private fun UserDetailOverviewSection(
    state: UserDetailScreenState.Content,
    onDisableTotpClick: () -> Unit
) {
    val userUi = LocalUserStrings.current.ui
    val commonUi = LocalCommonStrings.current.ui
    val notAvailableText = commonUi.denied
    val createdAtText = formatInstantToDateTime(state.user.createdAt) ?: state.user.createdAt.toString()
    val lastLoginText = state.user.lastLoginAt?.let { formatInstantToDateTime(it) } ?: notAvailableText
    val lastActiveText = state.user.lastActiveAt?.let { formatInstantToDateTime(it) } ?: notAvailableText

    CoreTitleText(
        text = "${commonUi.userId}: ${state.user.id.value}",
        style = MaterialTheme.typography.titleMedium
    )
    CoreBodyText(
        text = "${userUi.userRole}: ${state.user.role.value}"
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

    if (state.user.accountStatus == UserAccountStatus.PENDING_DELETION &&
        state.user.scheduledPermanentDeletionAt != null
    ) {
        val scheduledDeletionText = formatInstantToDateTime(state.user.scheduledPermanentDeletionAt)
            ?: state.user.scheduledPermanentDeletionAt.toString()
        CoreErrorText(
            text = userUi.scheduledDeletionAtLabel(scheduledDeletionText)
        )
    }
}

@Composable
private fun UserDetailNavigationActions(
    onSessionsClick: () -> Unit,
    onIdentifiersClick: () -> Unit,
    onFilesClick: () -> Unit,
    onAuditEventsClick: () -> Unit,
    auditEventsEnabled: Boolean
) {
    val userUi = LocalUserStrings.current.ui
    val storageUi = remember { StorageModuleStrings(KmpLocalization.resolver, KmpLocalization.currentLocale).ui }

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
        enabled = auditEventsEnabled
    )
}

@Composable
private fun UserDetailEditableFieldsSection(
    state: UserDetailScreenState.Content,
    onNicknameChanged: (String) -> Unit,
    onDisplayNameChanged: (String) -> Unit,
    onBirthdateChanged: (String) -> Unit,
    onAuthorityLevelChanged: (String) -> Unit,
    onAccountStatusChanged: (String) -> Unit,
    onLockoutTypeChanged: (String) -> Unit,
    onTemporaryLockoutUntilChanged: (String) -> Unit,
    onQuotaBytesChanged: (String) -> Unit,
    onMaxUploadSizeBytesChanged: (String) -> Unit,
    onMaxPendingUploadsChanged: (String) -> Unit
) {
    val userUi = LocalUserStrings.current.ui
    val commonUi = LocalCommonStrings.current.ui
    val storageUi = remember { StorageModuleStrings(KmpLocalization.resolver, KmpLocalization.currentLocale).ui }
    val isEditingEnabled = !state.isSaving && !state.isDeleting
    val isNonSelfEditingEnabled = isEditingEnabled && !state.isSelf

    CoreOutlinedTextField(
        value = state.nicknameInput,
        onValueChange = onNicknameChanged,
        label = { CoreBodyText(userUi.userLabel) },
        placeholder = { CoreBodyText(userUi.userLabel) },
        modifier = Modifier.testTag(UserDetailTestTags.NICKNAME_INPUT),
        enabled = isEditingEnabled
    )

    CoreOutlinedTextField(
        value = state.displayNameInput,
        onValueChange = onDisplayNameChanged,
        label = { CoreBodyText(userUi.user) },
        placeholder = { CoreBodyText(userUi.user) },
        modifier = Modifier.testTag(UserDetailTestTags.DISPLAY_NAME_INPUT),
        enabled = isEditingEnabled
    )

    CoreOutlinedTextField(
        value = state.birthdateInput,
        onValueChange = onBirthdateChanged,
        label = { CoreBodyText(userUi.birthdate) },
        placeholder = { CoreBodyText(userUi.birthdate) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.testTag(UserDetailTestTags.BIRTHDATE_INPUT),
        enabled = isEditingEnabled
    )

    AccountStatusDropdown(
        selectedAccountStatus = state.accountStatusInput,
        onAccountStatusSelected = onAccountStatusChanged,
        enabled = isNonSelfEditingEnabled
    )

    CoreOutlinedTextField(
        value = state.authorityLevelInput,
        onValueChange = onAuthorityLevelChanged,
        label = { CoreBodyText(userUi.authorityLevel) },
        placeholder = { CoreBodyText(userUi.authorityLevel) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.testTag(UserDetailTestTags.AUTHORITY_LEVEL_INPUT),
        enabled = isNonSelfEditingEnabled
    )

    LockoutTypeDropdown(
        selectedLockoutType = state.lockoutTypeInput,
        onLockoutTypeSelected = onLockoutTypeChanged,
        enabled = isNonSelfEditingEnabled
    )

    CoreOutlinedTextField(
        value = state.temporaryLockoutUntilInput,
        onValueChange = onTemporaryLockoutUntilChanged,
        label = { CoreBodyText(commonUi.lockoutUntil) },
        placeholder = { CoreBodyText(commonUi.lockoutUntil) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.testTag(UserDetailTestTags.TEMPORARY_LOCKOUT_UNTIL_INPUT),
        enabled = isNonSelfEditingEnabled
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
        enabled = isNonSelfEditingEnabled
    )

    CoreOutlinedTextField(
        value = state.maxUploadSizeBytesInput,
        onValueChange = onMaxUploadSizeBytesChanged,
        label = { CoreBodyText(storageUi.storageMaxUploadSizeBytes) },
        placeholder = { CoreBodyText(storageUi.defaultNull) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.testTag(UserDetailTestTags.MAX_UPLOAD_SIZE_BYTES_INPUT),
        enabled = isNonSelfEditingEnabled
    )

    CoreOutlinedTextField(
        value = state.maxPendingUploadsInput,
        onValueChange = onMaxPendingUploadsChanged,
        label = { CoreBodyText(storageUi.storageMaxPendingUploads) },
        placeholder = { CoreBodyText(storageUi.defaultNull) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.testTag(UserDetailTestTags.MAX_PENDING_UPLOADS_INPUT),
        enabled = isNonSelfEditingEnabled
    )
}

@Composable
private fun UserDetailActionsSection(
    state: UserDetailScreenState.Content,
    onUpdateClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onConfirmDeleteClick: () -> Unit,
    onDismissDeleteDialog: () -> Unit
) {
    val userUi = LocalUserStrings.current.ui

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
@PreviewScreenSizes
@Composable
private fun ScreenSizePreview(
    @PreviewParameter(UserDetailPreviewProvider::class) state: UserDetailScreenState
) {
    ScreenPreviewContainer {
        UserDetailScreenPreviewContent(state = state)
    }
}

@InternalApi
@PreviewThemes
@Composable
private fun ThemePreview(
    @PreviewParameter(UserDetailPreviewProvider::class) state: UserDetailScreenState
) {
    ScreenPreviewContainer {
        UserDetailScreenPreviewContent(state = state)
    }
}

@InternalApi
@PreviewFontScales
@Composable
private fun FontScalePreview(
    @PreviewParameter(UserDetailPreviewProvider::class) state: UserDetailScreenState
) {
    ScreenPreviewContainer {
        UserDetailScreenPreviewContent(state = state)
    }
}

public object UserDetailTestTags {
    public const val TITLE: String = "UserDetail_Title"
    public const val BACK_BUTTON: String = "UserDetail_BackButton"
    public const val GLOBAL_ERROR: String = "UserDetail_GlobalError"

    public const val DISABLE_TOTP_BUTTON: String = "UserDetail_DisableTotpButton"
    public const val DISABLE_TOTP_ERROR_TEXT: String = "UserDetail_DisableTotpErrorText"
    public const val SESSIONS_BUTTON: String = "UserDetail_SessionsButton"
    public const val IDENTIFIERS_BUTTON: String = "UserDetail_IdentifiersButton"
    public const val FILES_BUTTON: String = "UserDetail_FilesButton"
    public const val AUDIT_EVENTS_BUTTON: String = "UserDetail_AuditEventsButton"

    public const val NICKNAME_INPUT: String = "UserDetail_NicknameInput"
    public const val DISPLAY_NAME_INPUT: String = "UserDetail_DisplayNameInput"
    public const val AVATAR_CHANGE_BUTTON: String = "UserDetail_AvatarChangeButton"
    public const val BIRTHDATE_INPUT: String = "UserDetail_BirthdateInput"

    public const val ACCOUNT_STATUS_DROPDOWN: String = "UserDetail_AccountStatusDropdown"
    public const val AUTHORITY_LEVEL_INPUT: String = "UserDetail_AuthorityLevelInput"
    public const val LOCKOUT_TYPE_DROPDOWN: String = "UserDetail_LockoutTypeDropdown"
    public const val TEMPORARY_LOCKOUT_UNTIL_INPUT: String = "UserDetail_TemporaryLockoutUntilInput"

    public const val QUOTA_BYTES_INPUT: String = "UserDetail_QuotaBytesInput"
    public const val MAX_UPLOAD_SIZE_BYTES_INPUT: String = "UserDetail_MaxUploadSizeBytesInput"
    public const val MAX_PENDING_UPLOADS_INPUT: String = "UserDetail_MaxPendingUploadsInput"

    public const val SAVE_ERROR_TEXT: String = "UserDetail_SaveErrorText"
    public const val UPDATE_BUTTON: String = "UserDetail_UpdateButton"
    public const val DELETE_BUTTON: String = "UserDetail_DeleteButton"
    public const val DELETE_ERROR_TEXT: String = "UserDetail_DeleteErrorText"
}
