package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackDispatcher
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.SelectedFilePayload
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base.UploadFileUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.CreateUserUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.category.StorageCategory
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.visibility.StorageFileVisibility
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.presigned.PresignedUrlRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.storage.UserStorageTargetScopes
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.user.UserStorageConfigurationPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.create.CreateByEmailRequest
import kotlinx.coroutines.launch

/**
 * Default [CreateUserComponent] implementation: manages user account creation form state and execution.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param createUserUseCase Use case to administratively create a new user account.
 * @param uploadFileUseCase Direct upload usecase for uploading initial avatar images.
 * @param onUserCreated Optional callback invoked when user creation succeeds to refresh parent list state.
 * @param onSuccess Callback invoked when user creation succeeds to dismiss/pop creation screen.
 * @param onBack Pops this screen from the navigation stack.
 */
public class CreateUserComponentImpl(
    componentContext: ComponentContext,
    private val createUserUseCase: CreateUserUseCase,
    private val uploadFileUseCase: UploadFileUseCase? = null,
    private val onUserCreated: (() -> Unit)? = null,
    private val onSuccess: () -> Unit,
    private val onBack: () -> Unit
) : CreateUserComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val _state = MutableValue(CreateUserScreenState())
    override val state: Value<CreateUserScreenState> = _state

    override fun onEmailChanged(value: String) {
        _state.value = _state.value.copy(email = value, error = null)
    }

    override fun onPasswordChanged(value: String) {
        _state.value = _state.value.copy(password = value, error = null)
    }

    override fun onNicknameChanged(value: String) {
        _state.value = _state.value.copy(nickname = value, error = null)
    }

    override fun onDisplayNameChanged(value: String) {
        _state.value = _state.value.copy(displayName = value, error = null)
    }

    override fun onAvatarFileSelected(payload: SelectedFilePayload) {
        _state.value = _state.value.copy(selectedAvatarPayload = payload, error = null)
    }

    override fun onBirthdateChanged(value: String) {
        _state.value = _state.value.copy(birthdate = value, error = null)
    }

    override fun onRoleChanged(value: String) {
        _state.value = _state.value.copy(role = value, error = null)
    }

    override fun onStatusChanged(value: String) {
        _state.value = _state.value.copy(status = value, error = null)
    }

    override fun onAuthorityLevelChanged(value: String) {
        _state.value = _state.value.copy(authorityLevel = value, error = null)
    }

    override fun onQuotaBytesChanged(value: String) {
        _state.value = _state.value.copy(quotaBytes = value, error = null)
    }

    override fun onMaxUploadSizeBytesChanged(value: String) {
        _state.value = _state.value.copy(maxUploadSizeBytes = value, error = null)
    }

    override fun onMaxPendingUploadsChanged(value: String) {
        _state.value = _state.value.copy(maxPendingUploads = value, error = null)
    }

    override fun onCreateClick() {
        val current = _state.value
        _state.value = current.copy(isLoading = true, error = null)

        scope.launch {
            var uploadedAvatarFileId = current.avatarFileId.takeIf { it.isNotBlank() }

            val avatarPayload = current.selectedAvatarPayload
            if (avatarPayload != null && uploadFileUseCase != null) {
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
                        uploadedAvatarFileId = uploadResult.data.id.asHexDashString()
                    }

                    is AppResult.Error -> {
                        _state.value = current.copy(
                            isLoading = false,
                            error = uploadResult.error
                        )
                        return@launch
                    }
                }
            }

            val authLevel = current.authorityLevel.toIntOrNull() ?: 0
            val quotaBytes = current.quotaBytes.toLongOrNull()
            val maxUploadSizeBytes = current.maxUploadSizeBytes.toLongOrNull()
            val maxPendingUploads = current.maxPendingUploads.toIntOrNull()
            val storageConfiguration =
                if (quotaBytes != null || maxUploadSizeBytes != null || maxPendingUploads != null) {
                    UserStorageConfigurationPayload(
                        quotaBytes = quotaBytes,
                        maxUploadSizeBytes = maxUploadSizeBytes,
                        maxPendingUploads = maxPendingUploads
                    )
                } else {
                    null
                }

            val birthdateMillis = current.birthdate.takeIf { it.isNotBlank() }?.toLongOrNull()

            createUserUseCase(
                CreateByEmailRequest(
                    email = current.email,
                    password = current.password.ifBlank { null },
                    role = current.role,
                    status = current.status,
                    authorityLevel = authLevel,
                    permissionCodes = emptySet(),
                    storageConfiguration = storageConfiguration,
                    nickname = current.nickname.ifBlank { null },
                    displayName = current.displayName.ifBlank { null },
                    avatarFileId = uploadedAvatarFileId,
                    birthdate = birthdateMillis
                )
            ).onSuccess {
                onUserCreated?.invoke()
                onSuccess()
            }.onError { error ->
                _state.value = current.copy(isLoading = false, error = error)
            }
        }
    }

    override fun onBackClick() {
        if ((backHandler as? BackDispatcher)?.back() != true) {
            onBack()
        }
    }
}
