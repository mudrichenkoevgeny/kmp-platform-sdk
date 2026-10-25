package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError

/**
 * UI state for administrative user creation form.
 *
 * @param email Current email text field value.
 * @param password Current initial password text field value.
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
    val role: String = "USER",
    val status: String = "ACTIVE",
    val authorityLevel: String = "0",
    val quotaBytes: String = "",
    val maxUploadSizeBytes: String = "",
    val maxPendingUploads: String = "",
    val isLoading: Boolean = false,
    val error: AppError? = null
)
