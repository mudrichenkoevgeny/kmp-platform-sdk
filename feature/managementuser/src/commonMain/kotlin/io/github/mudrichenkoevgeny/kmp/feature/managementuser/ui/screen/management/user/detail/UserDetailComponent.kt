package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail

import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.SelectedFilePayload
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserAccountStatus

/**
 * Component contract for inspecting and managing detailed user profile and administrative actions.
 */
public interface UserDetailComponent {
    /** Reactive screen state. */
    public val state: Value<UserDetailScreenState>

    /** Updates nickname. */
    public fun onNicknameChanged(value: String)

    /** Updates display name. */
    public fun onDisplayNameChanged(value: String)

    /** Called when avatar file is selected. */
    public fun onAvatarFileSelected(payload: SelectedFilePayload)

    /** Updates birthdate. */
    public fun onBirthdateChanged(value: String)

    /** Updates authority level. */
    public fun onAuthorityLevelChanged(value: String)

    /** Updates account status. */
    public fun onAccountStatusChanged(value: UserAccountStatus)

    /** Updates lockout type. */
    public fun onLockoutTypeChanged(value: String)

    /** Updates temporary lockout timestamp. */
    public fun onTemporaryLockoutUntilChanged(value: String)

    /** Updates storage quota bytes. */
    public fun onQuotaBytesChanged(value: String)

    /** Updates max upload size bytes. */
    public fun onMaxUploadSizeBytesChanged(value: String)

    /** Updates max pending uploads. */
    public fun onMaxPendingUploadsChanged(value: String)

    /** Submits profile update. */
    public fun onUpdateClick()

    /** Requests user deletion. */
    public fun onDeleteClick()

    /** Confirms user deletion. */
    public fun onConfirmDeleteClick()

    /** Dismisses delete dialog. */
    public fun onDismissDeleteDialog()

    /** Disables TOTP for user. */
    public fun onDisableTotpClick()

    /** Navigates to user sessions screen. */
    public fun onSessionsClick()

    /** Navigates to user identifiers screen. */
    public fun onIdentifiersClick()

    /** Navigates to user storage files screen. */
    public fun onFilesClick()

    /** Navigates to user audit events screen. */
    public fun onAuditEventsClick()

    /** Retries failed data loading. */
    public fun onRetry()

    /** Navigates back. */
    public fun onBackClick()
}
