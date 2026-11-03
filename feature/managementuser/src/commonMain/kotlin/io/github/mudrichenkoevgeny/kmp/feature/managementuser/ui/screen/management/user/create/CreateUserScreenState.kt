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
data class CreateUserScreenState(
    val email: String = "",
    val password: String = "",
    val nickname: String = "",
    val displayName: String = "",
    val avatarFileId: String = "",
    val selectedAvatarPayload: SelectedFilePayload? = null,
    val birthdate: String = "",
    val role: String = "USER",
    val status: String = "ACTIVE",
    val authorityLevel: String = "0",
    val quotaBytes: String = "",
    val maxUploadSizeBytes: String = "",
    val maxPendingUploads: String = "",
    val isLoading: Boolean = false,
    val error: AppError? = null
)
