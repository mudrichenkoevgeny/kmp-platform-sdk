package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
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
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.detail.UserDetailComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.localization.LocalUserStrings
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.accountlockout.AccountLockoutType
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.localization.StorageModuleStrings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.accountstatus.UserAccountStatus
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.user.createTestUserPrivate

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

        CoreButton(
            text = if (state.isSaving) userUi.saving else userUi.updateUser,
            onClick = onUpdateClick,
            modifier = Modifier.testTag(UserDetailTestTags.UPDATE_BUTTON),
            enabled = state.hasChanges && !state.isSaving && !state.isDeleting && !state.isSelf
        )

        state.saveError?.let {
            CoreErrorText(
                text = it.toLocalizedMessage(),
                modifier = Modifier.testTag(UserDetailTestTags.SAVE_ERROR_TEXT)
            )
        }

        CoreButton(
            text = userUi.deleteUser,
            onClick = onDeleteClick,
            modifier = Modifier.testTag(UserDetailTestTags.DELETE_BUTTON),
            enabled = !state.isSaving && !state.isDeleting && !state.isSelf,
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

    if (state.isDeleteConfirmationVisible) {
        AlertDialog(
            onDismissRequest = onDismissDeleteDialog,
            title = { CoreTitleText(userUi.dialogConfirmTitle) },
            text = { CoreBodyText(userUi.deleteUserConfirmationDesc) },
            confirmButton = {
                CoreTextButton(
                    text = userUi.dialogConfirm,
                    onClick = onConfirmDeleteClick,
                    enabled = !state.isDeleting
                )
            },
            dismissButton = {
                CoreTextButton(
                    text = userUi.dialogCancel,
                    onClick = onDismissDeleteDialog,
                    enabled = !state.isDeleting
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
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    val commonUi = LocalCommonStrings.current.ui
    val userUi = LocalUserStrings.current.ui

    val options = listOf(
        UserAccountStatus.ACTIVE to commonUi.active,
        UserAccountStatus.READ_ONLY to commonUi.readOnly,
        UserAccountStatus.BANNED to commonUi.banned,
        UserAccountStatus.SECURITY_HOLD to commonUi.securityHold,
        UserAccountStatus.PENDING_DELETION to commonUi.pendingDeletion
    )

    val currentDisplayText = options.firstOrNull { it.first.name.equals(selectedAccountStatus, ignoreCase = true) }?.second
        ?: selectedAccountStatus

    ExposedDropdownMenuBox(
        expanded = isExpanded && enabled,
        onExpandedChange = {
            if (enabled) {
                isExpanded = it
            }
        },
        modifier = modifier.fillMaxWidth()
    ) {
        CoreOutlinedTextField(
            value = currentDisplayText,
            onValueChange = {},
            label = { CoreBodyText(userUi.userAccountStatus) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = enabled)
                .testTag(UserDetailTestTags.ACCOUNT_STATUS_INPUT)
        )

        ExposedDropdownMenu(
            expanded = isExpanded && enabled,
            onDismissRequest = { isExpanded = false }
        ) {
            options.forEach { (status, label) ->
                DropdownMenuItem(
                    text = { CoreBodyText(label) },
                    onClick = {
                        isExpanded = false
                        onAccountStatusSelected(status.name)
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
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    val commonUi = LocalCommonStrings.current.ui

    val options = listOf(
        AccountLockoutType.NONE to commonUi.lockoutNone,
        AccountLockoutType.INDEFINITE to commonUi.lockoutIndefinite,
        AccountLockoutType.TEMPORARY to commonUi.lockoutTemporary
    )

    val resolvedType = AccountLockoutType.fromValueOrNull(selectedLockoutType)
    val currentDisplayText = options.firstOrNull { it.first == resolvedType }?.second
        ?: selectedLockoutType

    ExposedDropdownMenuBox(
        expanded = isExpanded && enabled,
        onExpandedChange = {
            if (enabled) {
                isExpanded = it
            }
        },
        modifier = modifier.fillMaxWidth()
    ) {
        CoreOutlinedTextField(
            value = currentDisplayText,
            onValueChange = {},
            label = { CoreBodyText(commonUi.lockoutType) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = enabled)
                .testTag(UserDetailTestTags.LOCKOUT_TYPE_INPUT)
        )

        ExposedDropdownMenu(
            expanded = isExpanded && enabled,
            onDismissRequest = { isExpanded = false }
        ) {
            options.forEach { (type, label) ->
                DropdownMenuItem(
                    text = { CoreBodyText(label) },
                    onClick = {
                        isExpanded = false
                        onLockoutTypeSelected(type.name)
                    }
                )
            }
        }
    }
}

@InternalApi
internal class UserDetailPreviewProvider : PreviewParameterProvider<UserDetailScreenState> {
    private val items: List<Pair<String, UserDetailScreenState>> = listOf(
        "Content" to UserDetailScreenState.Content(
            user = createTestUserPrivate(),
            authorityLevelInput = "0",
            accountStatusInput = "ACTIVE",
            lockoutTypeInput = "NONE",
            temporaryLockoutUntilInput = ""
        ),
        "Pending Deletion" to UserDetailScreenState.Content(
            user = createTestUserPrivate().copy(accountStatus = UserAccountStatus.PENDING_DELETION),
            authorityLevelInput = "0",
            accountStatusInput = "PENDING_DELETION",
            lockoutTypeInput = "NONE",
            temporaryLockoutUntilInput = ""
        ),
        "Saving" to UserDetailScreenState.Content(
            user = createTestUserPrivate(),
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
    user = createTestUserPrivate(),
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

/**
 * Test tags for UI nodes on [UserDetailScreen].
 */
object UserDetailTestTags {
    const val TITLE = "UserDetail_Title"
    const val BACK_BUTTON = "UserDetail_BackButton"
    const val GLOBAL_ERROR = "UserDetail_GlobalError"
    const val ACCOUNT_STATUS_INPUT = "UserDetail_AccountStatusInput"
    const val AUTHORITY_LEVEL_INPUT = "UserDetail_AuthorityLevelInput"
    const val LOCKOUT_TYPE_INPUT = "UserDetail_LockoutTypeInput"
    const val TEMPORARY_LOCKOUT_UNTIL_INPUT = "UserDetail_TemporaryLockoutUntilInput"
    const val QUOTA_BYTES_INPUT = "UserDetail_QuotaBytesInput"
    const val MAX_UPLOAD_SIZE_BYTES_INPUT = "UserDetail_MaxUploadSizeBytesInput"
    const val MAX_PENDING_UPLOADS_INPUT = "UserDetail_MaxPendingUploadsInput"
    const val UPDATE_BUTTON = "UserDetail_UpdateButton"
    const val DELETE_BUTTON = "UserDetail_DeleteButton"
    const val DISABLE_TOTP_BUTTON = "UserDetail_DisableTotpErrorText"
    const val DISABLE_TOTP_ERROR_TEXT = "UserDetail_DisableTotpErrorText"
    const val SESSIONS_BUTTON = "UserDetail_SessionsButton"
    const val IDENTIFIERS_BUTTON = "UserDetail_IdentifiersButton"
    const val FILES_BUTTON = "UserDetail_FilesButton"
    const val AUDIT_EVENTS_BUTTON = "UserDetail_AuditEventsButton"
    const val SAVE_ERROR_TEXT = "UserDetail_SaveErrorText"
    const val DELETE_ERROR_TEXT = "UserDetail_DeleteErrorText"
}
