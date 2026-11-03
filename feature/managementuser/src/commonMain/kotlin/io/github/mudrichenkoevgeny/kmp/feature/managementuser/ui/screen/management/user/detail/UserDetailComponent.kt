package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail

import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.SelectedFilePayload

interface UserDetailComponent {
    val state: Value<UserDetailScreenState>

    fun onNicknameChanged(value: String)
    fun onDisplayNameChanged(value: String)
    fun onAvatarFileSelected(payload: SelectedFilePayload)
    fun onBirthdateChanged(value: String)
    fun onAuthorityLevelChanged(value: String)
    fun onAccountStatusChanged(value: String)
    fun onLockoutTypeChanged(value: String)
    fun onTemporaryLockoutUntilChanged(value: String)
    fun onQuotaBytesChanged(value: String)
    fun onMaxUploadSizeBytesChanged(value: String)
    fun onMaxPendingUploadsChanged(value: String)
    fun onUpdateClick()
    fun onDeleteClick()
    fun onConfirmDeleteClick()
    fun onDismissDeleteDialog()
    fun onDisableTotpClick()
    fun onSessionsClick()
    fun onIdentifiersClick()
    fun onFilesClick()
    fun onAuditEventsClick()
    fun onRetry()
    fun onBackClick()
}
