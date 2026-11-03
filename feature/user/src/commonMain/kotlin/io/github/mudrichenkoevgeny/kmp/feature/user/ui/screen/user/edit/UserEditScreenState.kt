package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.edit

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.SelectedFilePayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserPrivate

/**
 * State hierarchy for the user edit screen.
 */
sealed interface UserEditScreenState {
    /** Loading state while initial user details and settings are fetched. */
    data object Loading : UserEditScreenState

    /** Content state displaying edit fields and validation errors. */
    data class Content(
        val user: UserPrivate,
        val nicknameInput: String,
        val displayNameInput: String,
        val selectedAvatarPayload: SelectedFilePayload? = null,
        val avatarUrl: String? = null,
        val birthdateInput: String = "",
        val nicknameError: AppError? = null,
        val displayNameError: AppError? = null,
        val isSaving: Boolean = false,
        val actionError: AppError? = null
    ) : UserEditScreenState

    /** Error state when loading initial user fails. */
    data class Error(val error: AppError) : UserEditScreenState
}
