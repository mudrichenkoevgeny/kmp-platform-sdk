package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.SelectedFilePayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserPrivate

/**
 * UI state for administrative single user account detail screen.
 */
public sealed interface UserDetailScreenState {

    /** Initial loading of user data. */
    public object Loading : UserDetailScreenState

    /** Critical error during state initialization. */
    public data class Error(public val error: AppError) : UserDetailScreenState

    /**
     * User details and account management input form state.
     *
     * @param user The target user entity.
     * @param nicknameInput Current nickname handle text field value.
     * @param displayNameInput Current display name text field value.
     * @param avatarFileIdInput Current avatar file ID text field value.
     * @param avatarUrl Current avatar URL string.
     * @param selectedAvatarPayload Picked local avatar file payload.
     * @param birthdateInput Current birthdate epoch millis text value.
     * @param authorityLevelInput Current authority level text field value.
     * @param accountStatusInput Current account status dropdown selection.
     * @param lockoutTypeInput Current lockout type dropdown selection.
     * @param temporaryLockoutUntilInput Current temporary lockout expiry timestamp text value.
     * @param quotaBytesInput Current custom storage quota bytes text field value.
     * @param maxUploadSizeBytesInput Current custom max upload size bytes text field value.
     * @param maxPendingUploadsInput Current custom max pending uploads count text field value.
     * @param isSelf True if this detail screen belongs to the logged-in manager's own account.
     * @param isSaving True while update changes request is in progress.
     * @param saveError Server error from the last update attempt.
     * @param isDeleting True while account deletion request is in progress.
     * @param deleteError Server error from the last deletion attempt.
     * @param isDisablingTotp True while TOTP disable request is in progress.
     * @param disableTotpError Server error from the last TOTP disable attempt.
     * @param isDeleteConfirmationVisible True when the deletion confirmation dialog is shown.
     */
    public data class Content(
        public val user: UserPrivate,
        public val nicknameInput: String = "",
        public val displayNameInput: String = "",
        public val avatarFileIdInput: String = "",
        public val avatarUrl: String? = null,
        public val selectedAvatarPayload: SelectedFilePayload? = null,
        public val birthdateInput: String = "",
        public val authorityLevelInput: String = "0",
        public val accountStatusInput: String = "ACTIVE",
        public val lockoutTypeInput: String = "NONE",
        public val temporaryLockoutUntilInput: String = "",
        public val quotaBytesInput: String = "",
        public val maxUploadSizeBytesInput: String = "",
        public val maxPendingUploadsInput: String = "",
        public val isSelf: Boolean = false,
        public val isSaving: Boolean = false,
        public val saveError: AppError? = null,
        public val isDeleting: Boolean = false,
        public val deleteError: AppError? = null,
        public val isDisablingTotp: Boolean = false,
        public val disableTotpError: AppError? = null,
        public val isDeleteConfirmationVisible: Boolean = false
    ) : UserDetailScreenState {
        public val hasChanges: Boolean
            get() {
                val initialAuthLevel = user.authorityLevel.toString()
                val currentAuthLevel = authorityLevelInput.ifBlank { "0" }
                val initialAccountStatus = user.accountStatus.value
                val initialLockoutType = user.lockoutType.value
                val initialTempLockout = user.temporaryLockoutUntil?.toEpochMilliseconds()?.toString() ?: ""
                val initialQuotaBytes = user.storageConfiguration?.quotaBytes?.toString() ?: ""
                val initialMaxUploadSizeBytes = user.storageConfiguration?.maxUploadSizeBytes?.toString() ?: ""
                val initialMaxPendingUploads = user.storageConfiguration?.maxPendingUploads?.toString() ?: ""
                val initialNickname = user.nickname ?: ""
                val initialDisplayName = user.displayName ?: ""
                val initialAvatarFileId = user.avatarFileId?.value?.toHexDashString() ?: ""
                val initialBirthdate = user.birthdate?.toEpochMilliseconds()?.toString() ?: ""

                return currentAuthLevel != initialAuthLevel ||
                    !accountStatusInput.equals(initialAccountStatus, ignoreCase = true) ||
                    (
                        !lockoutTypeInput.equals(initialLockoutType, ignoreCase = true) &&
                            !lockoutTypeInput.equals(user.lockoutType.value, ignoreCase = true)
                        ) ||
                    temporaryLockoutUntilInput != initialTempLockout ||
                    quotaBytesInput != initialQuotaBytes ||
                    maxUploadSizeBytesInput != initialMaxUploadSizeBytes ||
                    maxPendingUploadsInput != initialMaxPendingUploads ||
                    nicknameInput != initialNickname ||
                    displayNameInput != initialDisplayName ||
                    avatarFileIdInput != initialAvatarFileId ||
                    selectedAvatarPayload != null ||
                    birthdateInput != initialBirthdate
            }
    }
}
