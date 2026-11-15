package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.SelectedFilePayload

/**
 * UI state for administrative user creation form.
 *
 * @param email Current email text field value.
 * @param password Current initial password text field value.
 * @param nickname Optional handle nickname.
 * @param displayName Optional display name.
 * @param avatarFileId Optional avatar file ID.
 * @param selectedAvatarPayload Picked avatar image file payload.
 * @param birthdate Optional birthdate epoch millis.
 * @param role Selected user role.
 * @param status Selected initial account status.
 * @param authorityLevel Current authority level text field value.
 * @param quotaBytes Optional custom storage quota in bytes text field value.
 * @param maxUploadSizeBytes Optional custom max upload file size in bytes text field value.
 * @param maxPendingUploads Optional custom max concurrent pending uploads text field value.
 * @param isLoading True while create user request is in progress.
 * @param error Error from the last create attempt, or `null`.
 */
public data class CreateUserScreenState(
    public val email: String = "",
    public val password: String = "",
    public val nickname: String = "",
    public val displayName: String = "",
    public val avatarFileId: String = "",
    public val selectedAvatarPayload: SelectedFilePayload? = null,
    public val birthdate: String = "",
    public val role: String = "USER",
    public val status: String = "ACTIVE",
    public val authorityLevel: String = "0",
    public val quotaBytes: String = "",
    public val maxUploadSizeBytes: String = "",
    public val maxPendingUploads: String = "",
    public val isLoading: Boolean = false,
    public val error: AppError? = null
)
