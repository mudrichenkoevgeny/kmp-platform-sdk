package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create

import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.SelectedFilePayload

public interface CreateUserComponent {
    public val state: Value<CreateUserScreenState>

    public fun onEmailChanged(value: String)
    public fun onPasswordChanged(value: String)
    public fun onNicknameChanged(value: String)
    public fun onDisplayNameChanged(value: String)
    public fun onAvatarFileSelected(payload: SelectedFilePayload)
    public fun onBirthdateChanged(value: String)
    public fun onRoleChanged(value: String)
    public fun onStatusChanged(value: String)
    public fun onAuthorityLevelChanged(value: String)
    public fun onQuotaBytesChanged(value: String)
    public fun onMaxUploadSizeBytesChanged(value: String)
    public fun onMaxPendingUploadsChanged(value: String)
    public fun onCreateClick()
    public fun onBackClick()
}
