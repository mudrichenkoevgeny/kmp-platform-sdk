package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.model.UpdateField
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.SelectedFilePayload
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base.UploadFileUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.DeleteUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.GetUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.UpdateUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.security.ManagementDisableTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.domain.model.user.UpdateUserParams
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.accountlockout.AccountLockoutType
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.category.StorageCategory
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.FileId
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.FileUrlPresets
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.toFileIdOrNull
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.visibility.StorageFileVisibility
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.presigned.PresignedUrlRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.accountstatus.UserAccountStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.storage.UserStorageTargetScopes
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.user.detail.UserDetailParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.user.UserStorageConfigurationPayload
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlin.time.Instant

/**
 * Default [UserDetailComponent] implementation: manages single user account detail inspection and administrative updates.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param params Screen destination parameters containing the target user ID.
 * @param getUserUseCase Use case to fetch target user data.
 * @param updateUserUseCase Use case to update target user account status and settings.
 * @param deleteUserUseCase Use case to administratively delete target user account.
 * @param managementDisableTotpUseCase Use case to administratively disable TOTP 2FA for target user.
 * @param uploadFileUseCase Direct upload usecase for avatar file updates.
 * @param onNavigateToSessions Callback to navigate to the user's sessions list.
 * @param onNavigateToIdentifiers Callback to navigate to the user's identifiers list.
 * @param onNavigateToAuditEvents Callback to navigate to audit events filtered by the user's ID.
 * @param onUserDeleted Callback invoked when the user account is deleted.
 * @param onBack Pops this screen from the navigation stack.
 */
class UserDetailComponentImpl(
    componentContext: ComponentContext,
    private val params: UserDetailParams,
    private val getUserUseCase: GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val managementDisableTotpUseCase: ManagementDisableTotpUseCase,
    private val uploadFileUseCase: UploadFileUseCase? = null,
    private val userRepository: UserRepository? = null,
    private val onNavigateToSessions: (UserId) -> Unit,
    private val onNavigateToIdentifiers: (UserId) -> Unit,
    private val onNavigateToFilesList: ((UserId) -> Unit)? = null,
    private val onNavigateToAuditEvents: (UserId) -> Unit,
    private val onNavigateToUserMain: (() -> Unit)? = null,
    private val onUserDeleted: ((UserId) -> Unit)? = null,
    private val onBack: () -> Unit
) : UserDetailComponent, ComponentContext by componentContext {

    private val userId: UserId? get() = params.typedUserId

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<UserDetailScreenState>(UserDetailScreenState.Loading)
    override val state: Value<UserDetailScreenState> = _state

    init {
        loadUser()
    }

    override fun onNicknameChanged(value: String) {
        val currentContent = _state.value as? UserDetailScreenState.Content ?: return
        _state.value = currentContent.copy(nicknameInput = value, saveError = null)
    }

    override fun onDisplayNameChanged(value: String) {
        val currentContent = _state.value as? UserDetailScreenState.Content ?: return
        _state.value = currentContent.copy(displayNameInput = value, saveError = null)
    }

    override fun onAvatarFileSelected(payload: SelectedFilePayload) {
        val currentContent = _state.value as? UserDetailScreenState.Content ?: return
        _state.value = currentContent.copy(selectedAvatarPayload = payload, saveError = null)
    }

    override fun onBirthdateChanged(value: String) {
        val currentContent = _state.value as? UserDetailScreenState.Content ?: return
        _state.value = currentContent.copy(birthdateInput = value, saveError = null)
    }

    override fun onAuthorityLevelChanged(value: String) {
        val currentContent = _state.value as? UserDetailScreenState.Content ?: return
        _state.value = currentContent.copy(authorityLevelInput = value, saveError = null)
    }

    override fun onAccountStatusChanged(value: String) {
        val currentContent = _state.value as? UserDetailScreenState.Content ?: return
        _state.value = currentContent.copy(accountStatusInput = value, saveError = null)
    }

    override fun onLockoutTypeChanged(value: String) {
        val currentContent = _state.value as? UserDetailScreenState.Content ?: return
        _state.value = currentContent.copy(lockoutTypeInput = value, saveError = null)
    }

    override fun onTemporaryLockoutUntilChanged(value: String) {
        val currentContent = _state.value as? UserDetailScreenState.Content ?: return
        _state.value = currentContent.copy(temporaryLockoutUntilInput = value, saveError = null)
    }

    override fun onQuotaBytesChanged(value: String) {
        val currentContent = _state.value as? UserDetailScreenState.Content ?: return
        _state.value = currentContent.copy(quotaBytesInput = value, saveError = null)
    }

    override fun onMaxUploadSizeBytesChanged(value: String) {
        val currentContent = _state.value as? UserDetailScreenState.Content ?: return
        _state.value = currentContent.copy(maxUploadSizeBytesInput = value, saveError = null)
    }

    override fun onMaxPendingUploadsChanged(value: String) {
        val currentContent = _state.value as? UserDetailScreenState.Content ?: return
        _state.value = currentContent.copy(maxPendingUploadsInput = value, saveError = null)
    }

    override fun onUpdateClick() {
        val targetId = userId ?: return
        val currentContent = _state.value as? UserDetailScreenState.Content ?: return
        if (currentContent.isSaving) {
            return
        }

        _state.value = currentContent.copy(isSaving = true, saveError = null)

        scope.launch {
            var avatarUpdateField: UpdateField<FileId> = UpdateField.Ignore

            val avatarPayload = currentContent.selectedAvatarPayload
            if (avatarPayload != null) {
                if (uploadFileUseCase != null) {
                    val presignedRequest = PresignedUrlRequest(
                        fileName = avatarPayload.fileName,
                        fileSizeBytes = avatarPayload.fileSizeBytes,
                        mimeType = avatarPayload.mimeType,
                        category = StorageCategory.IMAGE.value,
                        visibility = StorageFileVisibility.PUBLIC.value,
                        targetScope = UserStorageTargetScopes.AVATAR
                    )

                    val uploadResult = uploadFileUseCase(
                        request = presignedRequest,
                        openReadChannel = avatarPayload.openReadChannel
                    )

                    when (uploadResult) {
                        is AppResult.Success -> {
                            avatarUpdateField = UpdateField.Set(uploadResult.data.id)
                        }
                        is AppResult.Error -> {
                            _state.value = currentContent.copy(
                                isSaving = false,
                                saveError = uploadResult.error
                            )
                            return@launch
                        }
                    }
                }
            } else if (currentContent.avatarFileIdInput != (currentContent.user.avatarFileId?.value?.toHexDashString() ?: "")) {
                avatarUpdateField = UpdateField.Set(currentContent.avatarFileIdInput.takeIf { it.isNotBlank() }?.toFileIdOrNull())
            }

            val authorityLevel = currentContent.authorityLevelInput.toIntOrNull()
            val accountStatusEnum = currentContent.accountStatusInput.takeIf { it.isNotBlank() }
                ?.let { UserAccountStatus.fromValueOrNull(it) }
            val lockoutTypeEnum = currentContent.lockoutTypeInput.takeIf { it.isNotBlank() }
                ?.let { AccountLockoutType.fromValueOrNull(it) }
            val lockoutUntil = currentContent.temporaryLockoutUntilInput.toLongOrNull()

            val quotaBytes = currentContent.quotaBytesInput.toLongOrNull()
            val maxUploadSizeBytes = currentContent.maxUploadSizeBytesInput.toLongOrNull()
            val maxPendingUploads = currentContent.maxPendingUploadsInput.toIntOrNull()

            val storageConfiguration = UserStorageConfigurationPayload(
                quotaBytes = quotaBytes,
                maxUploadSizeBytes = maxUploadSizeBytes,
                maxPendingUploads = maxPendingUploads
            )

            val nicknameUpdateField = if (currentContent.nicknameInput != (currentContent.user.nickname ?: "")) {
                UpdateField.Set(currentContent.nicknameInput.takeIf { it.isNotBlank() })
            } else {
                UpdateField.Ignore
            }

            val displayNameUpdateField = if (currentContent.displayNameInput != (currentContent.user.displayName ?: "")) {
                UpdateField.Set(currentContent.displayNameInput.takeIf { it.isNotBlank() })
            } else {
                UpdateField.Ignore
            }

            val initialBirthdateString = currentContent.user.birthdate?.toEpochMilliseconds()?.toString() ?: ""
            val birthdateUpdateField = if (currentContent.birthdateInput != initialBirthdateString) {
                val birthdateInstant = currentContent.birthdateInput.takeIf { it.isNotBlank() }
                    ?.toLongOrNull()
                    ?.let { Instant.fromEpochMilliseconds(it) }
                UpdateField.Set(birthdateInstant)
            } else {
                UpdateField.Ignore
            }

            val updateParams = UpdateUserParams(
                accountStatus = if (accountStatusEnum != null && currentContent.accountStatusInput != currentContent.user.accountStatus.name) UpdateField.Set(accountStatusEnum) else UpdateField.Ignore,
                authorityLevel = if (authorityLevel != null && authorityLevel != currentContent.user.authorityLevel) UpdateField.Set(authorityLevel) else UpdateField.Ignore,
                lockoutType = if (lockoutTypeEnum != null && currentContent.lockoutTypeInput != currentContent.user.lockoutType.name) UpdateField.Set(lockoutTypeEnum) else UpdateField.Ignore,
                temporaryLockoutUntil = if (currentContent.temporaryLockoutUntilInput != (currentContent.user.temporaryLockoutUntil?.toEpochMilliseconds()?.toString() ?: "")) UpdateField.Set(lockoutUntil?.let { Instant.fromEpochMilliseconds(it) }) else UpdateField.Ignore,
                storageConfiguration = if (quotaBytes != null || maxUploadSizeBytes != null || maxPendingUploads != null) UpdateField.Set(storageConfiguration) else UpdateField.Ignore,
                nickname = nicknameUpdateField,
                displayName = displayNameUpdateField,
                avatarFileId = avatarUpdateField,
                birthdate = birthdateUpdateField
            )

            updateUserUseCase(targetId, updateParams)
                .onSuccess {
                    loadUser()
                }
                .onError { error ->
                    val content = _state.value as? UserDetailScreenState.Content
                    if (content != null) {
                        _state.value = content.copy(isSaving = false, saveError = error)
                    }
                }
        }
    }

    override fun onDeleteClick() {
        val currentContent = _state.value as? UserDetailScreenState.Content ?: return
        _state.value = currentContent.copy(isDeleteConfirmationVisible = true)
    }

    override fun onConfirmDeleteClick() {
        val targetId = userId ?: return
        val currentContent = _state.value as? UserDetailScreenState.Content ?: return
        _state.value = currentContent.copy(isDeleting = true, deleteError = null, isDeleteConfirmationVisible = false)

        scope.launch {
            deleteUserUseCase(targetId)
                .onSuccess {
                    onUserDeleted?.invoke(targetId)
                    onBack()
                }
                .onError { error ->
                    val content = _state.value as? UserDetailScreenState.Content
                    if (content != null) {
                        _state.value = content.copy(isDeleting = false, deleteError = error)
                    }
                }
        }
    }

    override fun onDismissDeleteDialog() {
        val currentContent = _state.value as? UserDetailScreenState.Content ?: return
        _state.value = currentContent.copy(isDeleteConfirmationVisible = false)
    }

    override fun onDisableTotpClick() {
        val targetId = userId ?: return
        val currentContent = _state.value as? UserDetailScreenState.Content ?: return
        _state.value = currentContent.copy(isDisablingTotp = true, disableTotpError = null)

        scope.launch {
            managementDisableTotpUseCase(targetId)
                .onSuccess {
                    loadUser()
                }
                .onError { error ->
                    val content = _state.value as? UserDetailScreenState.Content
                    if (content != null) {
                        _state.value = content.copy(isDisablingTotp = false, disableTotpError = error)
                    }
                }
        }
    }

    override fun onSessionsClick() {
        val targetId = userId ?: return
        onNavigateToSessions(targetId)
    }

    override fun onIdentifiersClick() {
        val targetId = userId ?: return
        onNavigateToIdentifiers(targetId)
    }

    override fun onFilesClick() {
        val targetId = userId ?: return
        onNavigateToFilesList?.invoke(targetId)
    }

    override fun onAuditEventsClick() {
        val targetId = userId ?: return
        onNavigateToAuditEvents(targetId)
    }

    override fun onRetry() {
        loadUser()
    }

    override fun onBackClick() {
        onBack()
    }

    private fun loadUser() {
        val targetId = userId
        if (targetId == null) {
            _state.value = UserDetailScreenState.Error(
                CommonError.ContractViolation(throwable = IllegalStateException("Invalid userId in params"))
            )
            return
        }

        _state.value = UserDetailScreenState.Loading

        scope.launch {
            val selfUserId = userRepository?.currentUser?.firstOrNull()?.id

            getUserUseCase(targetId)
                .onSuccess { user ->
                    val isSelfUser = selfUserId != null && user.id == selfUserId
                    val avatarUrl = user.avatarUrls?.urlPresets?.get(FileUrlPresets.AVATAR_MD)
                        ?: user.avatarUrls?.originalUrl

                    _state.value = UserDetailScreenState.Content(
                        user = user,
                        nicknameInput = user.nickname ?: "",
                        displayNameInput = user.displayName ?: "",
                        avatarFileIdInput = user.avatarFileId?.value?.toHexDashString() ?: "",
                        avatarUrl = avatarUrl,
                        birthdateInput = user.birthdate?.toEpochMilliseconds()?.toString() ?: "",
                        authorityLevelInput = user.authorityLevel.toString(),
                        accountStatusInput = user.accountStatus.name,
                        lockoutTypeInput = user.lockoutType.name,
                        temporaryLockoutUntilInput = user.temporaryLockoutUntil?.toEpochMilliseconds()?.toString() ?: "",
                        quotaBytesInput = user.storageConfiguration?.quotaBytes?.toString() ?: "",
                        maxUploadSizeBytesInput = user.storageConfiguration?.maxUploadSizeBytes?.toString() ?: "",
                        maxPendingUploadsInput = user.storageConfiguration?.maxPendingUploads?.toString() ?: "",
                        isSelf = isSelfUser
                    )
                }
                .onError { error ->
                    _state.value = UserDetailScreenState.Error(error)
                }
        }
    }
}
