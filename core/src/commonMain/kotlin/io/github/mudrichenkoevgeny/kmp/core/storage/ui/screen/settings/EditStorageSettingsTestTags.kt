package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.settings

/**
 * Test tags dictionary for [EditStorageSettingsScreen].
 */
public object EditStorageSettingsTestTags {
    /** Test tag for the screen back button element. */
    public const val BACK_BUTTON: String = "EditStorageSettings_BackButton"

    /** Test tag for the screen title element. */
    public const val TITLE: String = "EditStorageSettings_Title"

    /** Test tag for the reset settings button element. */
    public const val RESET_BUTTON: String = "EditStorageSettings_ResetButton"

    /** Test tag for the save settings button element. */
    public const val SAVE_BUTTON: String = "EditStorageSettings_SaveButton"

    /** Test tag for the error message container element. */
    public const val ERROR_TEXT: String = "EditStorageSettings_ErrorText"

    /** Test tag for minimum upload size text field element. */
    public const val MIN_UPLOAD_SIZE_BYTES_INPUT: String = "EditStorageSettings_MinUploadSizeBytesInput"

    /** Test tag for maximum upload size text field element. */
    public const val MAX_UPLOAD_SIZE_BYTES_INPUT: String = "EditStorageSettings_MaxUploadSizeBytesInput"

    /** Test tag for total open user storage quota text field element. */
    public const val TOTAL_STORAGE_QUOTA_FOR_OPEN_USER_BYTES_INPUT: String =
        "EditStorageSettings_TotalStorageQuotaForOpenUserBytesInput"

    /** Test tag for total management user storage quota text field element. */
    public const val TOTAL_STORAGE_QUOTA_FOR_MANAGEMENT_USER_BYTES_INPUT: String =
        "EditStorageSettings_TotalStorageQuotaForManagementUserBytesInput"

    /** Test tag for presigned URL expiration text field element. */
    public const val PRESIGNED_URL_EXPIRATION_SECONDS_INPUT: String =
        "EditStorageSettings_PresignedUrlExpirationSecondsInput"

    /** Test tag for max pending uploads for open users text field element. */
    public const val MAX_PENDING_UPLOADS_FOR_OPEN_USER_INPUT: String =
        "EditStorageSettings_MaxPendingUploadsForOpenUserInput"

    /** Test tag for max pending uploads for management users text field element. */
    public const val MAX_PENDING_UPLOADS_FOR_MANAGEMENT_USER_INPUT: String =
        "EditStorageSettings_MaxPendingUploadsForManagementUserInput"

    /** Test tag for deduplication toggle checkbox element. */
    public const val IS_DEDUPLICATION_ENABLED_CHECKBOX: String = "EditStorageSettings_IsDeduplicationEnabledCheckbox"

    /** Test tag for deleted files cleanup interval text field element. */
    public const val DELETED_FILES_CLEANUP_INTERVAL_SECONDS_INPUT: String =
        "EditStorageSettings_DeletedFilesCleanupIntervalSecondsInput"
}
