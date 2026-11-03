package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.edit

import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.SelectedFilePayload

/**
 * Interface for the user edit component allowing users to modify profile details (nickname, display name, avatar, birthdate).
 */
interface UserEditComponent {
    /** Reactive state of the user edit screen. */
    val state: Value<UserEditScreenState>

    /** Invoked when the nickname input changes. */
    fun onNicknameChanged(value: String)

    /** Invoked when the display name input changes. */
    fun onDisplayNameChanged(value: String)

    /** Invoked when a new avatar file is picked from device storage. */
    fun onAvatarFileSelected(payload: SelectedFilePayload)

    /** Invoked when the birthdate input changes. */
    fun onBirthdateChanged(value: String)

    /** Triggers user update operation. */
    fun onSaveClick()

    /** Retries loading current user and settings on error. */
    fun onRetry()

    /** Navigates back. */
    fun onBackClick()
}
