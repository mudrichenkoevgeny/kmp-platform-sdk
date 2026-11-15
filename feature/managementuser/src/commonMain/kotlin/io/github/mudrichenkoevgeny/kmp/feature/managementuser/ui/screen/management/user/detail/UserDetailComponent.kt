package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail

import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.SelectedFilePayload

public interface UserDetailComponent {
    public val state: Value<UserDetailScreenState>

    public fun onNicknameChanged(value: String)
    public fun onDisplayNameChanged(value: String)
    public fun onAvatarFileSelected(payload: SelectedFilePayload)
    public fun onBirthdateChanged(value: String)
    public fun onAuthorityLevelChanged(value: String)
    public fun onAccountStatusChanged(value: String)
    public fun onLockoutTypeChanged(value: String)
    public fun onTemporaryLockoutUntilChanged(value: String)
    public fun onQuotaBytesChanged(value: String)
    public fun onMaxUploadSizeBytesChanged(value: String)
    public fun onMaxPendingUploadsChanged(value: String)
    public fun onUpdateClick()
    public fun onDeleteClick()
    public fun onConfirmDeleteClick()
    public fun onDismissDeleteDialog()
    public fun onDisableTotpClick()
    public fun onSessionsClick()
    public fun onIdentifiersClick()
    public fun onFilesClick()
    public fun onAuditEventsClick()
    public fun onRetry()
    public fun onBackClick()
}
