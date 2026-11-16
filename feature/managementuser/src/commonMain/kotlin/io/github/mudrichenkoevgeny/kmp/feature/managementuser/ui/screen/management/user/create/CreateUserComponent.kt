package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create

import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.SelectedFilePayload

/**
 * Component contract for creating a new user (administrative operation).
 */
public interface CreateUserComponent {
    /** Reactive screen state. */
    public val state: Value<CreateUserScreenState>

    /** Updates email input. */
    public fun onEmailChanged(value: String)

    /** Updates password input. */
    public fun onPasswordChanged(value: String)

    /** Updates nickname input. */
    public fun onNicknameChanged(value: String)

    /** Updates display name input. */
    public fun onDisplayNameChanged(value: String)

    /** Called when an avatar file is selected. */
    public fun onAvatarFileSelected(payload: SelectedFilePayload)

    /** Updates birthdate input. */
    public fun onBirthdateChanged(value: String)

    /** Updates role input. */
    public fun onRoleChanged(value: String)

    /** Updates status input. */
    public fun onStatusChanged(value: String)

    /** Updates authority level input. */
    public fun onAuthorityLevelChanged(value: String)

    /** Updates quota bytes input. */
    public fun onQuotaBytesChanged(value: String)

    /** Updates max upload size bytes input. */
    public fun onMaxUploadSizeBytesChanged(value: String)

    /** Updates max pending uploads input. */
    public fun onMaxPendingUploadsChanged(value: String)

    /** Submits user creation. */
    public fun onCreateClick()

    /** Navigates back. */
    public fun onBackClick()
}
