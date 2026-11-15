package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.edit

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
import io.github.mudrichenkoevgeny.kmp.feature.user.domain.model.user.UpdateUserParams
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.SelfUpdateUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.validation.ValidateDisplayNameUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.validation.ValidateNicknameUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.category.StorageCategory
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.FileId
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.FileUrlPresets
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.visibility.StorageFileVisibility
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.presigned.PresignedUrlRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.storage.UserStorageTargetScopes
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlin.time.Instant

/**
 * Default [UserEditComponent] implementation: manages loading current user profile,
 * validating inputs, uploading avatar image, and submitting changes.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param userRepository Source for reading current signed-in user snapshot.
 * @param selfUpdateUserUseCase Source for updating current signed-in user snapshot.
 * @param validateNicknameUseCase Validator for nickname constraints via remote settings.
 * @param validateDisplayNameUseCase Validator for display name constraints via remote settings.
 * @param uploadFileUseCase Optional direct file uploader for new avatar images.
 * @param onBack Callback invoked on back navigation or successful save.
 */
public class UserEditComponentImpl(
    componentContext: ComponentContext,
    private val userRepository: UserRepository,
    private val selfUpdateUserUseCase: SelfUpdateUserUseCase,
    private val validateNicknameUseCase: ValidateNicknameUseCase,
    private val validateDisplayNameUseCase: ValidateDisplayNameUseCase,
    private val uploadFileUseCase: UploadFileUseCase? = null,
    private val onBack: () -> Unit
) : UserEditComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<UserEditScreenState>(UserEditScreenState.Loading)
    public override val state: Value<UserEditScreenState> = _state

    init {
        loadData()
    }

    public override fun onNicknameChanged(value: String) {
        val currentContent = _state.value as? UserEditScreenState.Content ?: return
        _state.value = currentContent.copy(
            nicknameInput = value,
            nicknameError = null,
            actionError = null
        )
    }

    public override fun onDisplayNameChanged(value: String) {
        val currentContent = _state.value as? UserEditScreenState.Content ?: return
        _state.value = currentContent.copy(
            displayNameInput = value,
            displayNameError = null,
            actionError = null
        )
    }

    public override fun onAvatarFileSelected(payload: SelectedFilePayload) {
        val currentContent = _state.value as? UserEditScreenState.Content ?: return
        _state.value = currentContent.copy(
            selectedAvatarPayload = payload,
            actionError = null
        )
    }

    public override fun onBirthdateChanged(value: String) {
        val currentContent = _state.value as? UserEditScreenState.Content ?: return
        _state.value = currentContent.copy(
            birthdateInput = value,
            actionError = null
        )
    }

    public override fun onSaveClick() {
        val currentContent = _state.value as? UserEditScreenState.Content ?: return
        if (currentContent.isSaving) {
            return
        }

        scope.launch {
            val nicknameResult = validateNicknameUseCase(
                newNickname = currentContent.nicknameInput,
                currentNickname = currentContent.user.nickname
            )
            val displayNameResult = validateDisplayNameUseCase(
                newDisplayName = currentContent.displayNameInput
            )

            val nickError = (nicknameResult as? AppResult.Error)?.error
            val nameError = (displayNameResult as? AppResult.Error)?.error

            if (nickError != null || nameError != null) {
                _state.value = currentContent.copy(
                    nicknameError = nickError,
                    displayNameError = nameError,
                    actionError = null
                )
                return@launch
            }

            _state.value = currentContent.copy(isSaving = true, actionError = null)

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
                                actionError = uploadResult.error
                            )
                            return@launch
                        }
                    }
                }
            }

            val nicknameUpdateField = if (currentContent.nicknameInput != (currentContent.user.nickname ?: "")) {
                UpdateField.Set(currentContent.nicknameInput.takeIf { it.isNotBlank() })
            } else {
                UpdateField.Ignore
            }

            val displayNameUpdateField =
                if (currentContent.displayNameInput != (currentContent.user.displayName ?: "")) {
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
                nickname = nicknameUpdateField,
                displayName = displayNameUpdateField,
                avatarFileId = avatarUpdateField,
                birthdate = birthdateUpdateField
            )

            selfUpdateUserUseCase(updateParams)
                .onSuccess {
                    onBack()
                }
                .onError { error ->
                    val latest = _state.value as? UserEditScreenState.Content
                    if (latest != null) {
                        _state.value = latest.copy(isSaving = false, actionError = error)
                    }
                }
        }
    }

    public override fun onRetry() {
        loadData()
    }

    public override fun onBackClick() {
        onBack()
    }

    private fun loadData() {
        _state.value = UserEditScreenState.Loading

        scope.launch {
            userRepository.refreshCurrentUser()

            val user = userRepository.currentUser.firstOrNull()
            if (user == null) {
                _state.value = UserEditScreenState.Error(
                    CommonError.ContractViolation(throwable = IllegalStateException("User not found"))
                )
                return@launch
            }

            val avatarUrl = user.avatarUrls?.urlPresets?.get(FileUrlPresets.AVATAR_MD)
                ?: user.avatarUrls?.originalUrl

            _state.value = UserEditScreenState.Content(
                user = user,
                nicknameInput = user.nickname ?: "",
                displayNameInput = user.displayName ?: "",
                avatarUrl = avatarUrl,
                birthdateInput = user.birthdate?.toEpochMilliseconds()?.toString() ?: ""
            )
        }
    }
}
