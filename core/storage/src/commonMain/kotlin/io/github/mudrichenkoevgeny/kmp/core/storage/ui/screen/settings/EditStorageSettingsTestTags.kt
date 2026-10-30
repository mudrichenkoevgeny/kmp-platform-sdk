package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.settings

/**
 * Test tags dictionary for [EditStorageSettingsScreen].
 */
object EditStorageSettingsTestTags {
    /** Test tag for the screen back button element. */
    const val BACK_BUTTON = "EditStorageSettings_BackButton"

    /** Test tag for the screen title element. */
    const val TITLE = "EditStorageSettings_Title"

    /** Test tag for the reset settings button element. */
    const val RESET_BUTTON = "EditStorageSettings_ResetButton"

    /** Test tag for the save settings button element. */
    const val SAVE_BUTTON = "EditStorageSettings_SaveButton"

    /** Test tag for the error message container element. */
    const val ERROR_TEXT = "EditStorageSettings_ErrorText"

    /** Test tag for minimum upload size text field element. */
    const val MIN_UPLOAD_SIZE_BYTES_INPUT = "EditStorageSettings_MinUploadSizeBytesInput"

    /** Test tag for maximum upload size text field element. */
    const val MAX_UPLOAD_SIZE_BYTES_INPUT = "EditStorageSettings_MaxUploadSizeBytesInput"

    /** Test tag for total open user storage quota text field element. */
    const val TOTAL_STORAGE_QUOTA_FOR_OPEN_USER_BYTES_INPUT = "EditStorageSettings_TotalStorageQuotaForOpenUserBytesInput"

    /** Test tag for total management user storage quota text field element. */
    const val TOTAL_STORAGE_QUOTA_FOR_MANAGEMENT_USER_BYTES_INPUT = "EditStorageSettings_TotalStorageQuotaForManagementUserBytesInput"

    /** Test tag for presigned URL expiration text field element. */
    const val PRESIGNED_URL_EXPIRATION_SECONDS_INPUT = "EditStorageSettings_PresignedUrlExpirationSecondsInput"

    /** Test tag for max pending uploads for open users text field element. */
    const val MAX_PENDING_UPLOADS_FOR_OPEN_USER_INPUT = "EditStorageSettings_MaxPendingUploadsForOpenUserInput"

    /** Test tag for max pending uploads for management users text field element. */
    const val MAX_PENDING_UPLOADS_FOR_MANAGEMENT_USER_INPUT = "EditStorageSettings_MaxPendingUploadsForManagementUserInput"

    /** Test tag for deduplication toggle checkbox element. */
    const val IS_DEDUPLICATION_ENABLED_CHECKBOX = "EditStorageSettings_IsDeduplicationEnabledCheckbox"

    /** Test tag for deleted files cleanup interval text field element. */
    const val DELETED_FILES_CLEANUP_INTERVAL_SECONDS_INPUT = "EditStorageSettings_DeletedFilesCleanupIntervalSecondsInput"
}
