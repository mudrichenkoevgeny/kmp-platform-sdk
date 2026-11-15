package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.settings

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.ManagementStorageSettings

/**
 * UI state for editing platform storage settings.
 */
public sealed interface EditStorageSettingsScreenState {
    /** Initial loading state. */
    public data object Loading : EditStorageSettingsScreenState

    /** Loading error state. */
    public data class Error(public val error: AppError) : EditStorageSettingsScreenState

    /** Form content state. */
    public data class Content(
        public val initialSettings: ManagementStorageSettings,
        public val minUploadSizeBytes: String,
        public val maxUploadSizeBytes: String,
        public val totalStorageQuotaForOpenUserBytes: String,
        public val totalStorageQuotaForManagementUserBytes: String,
        public val presignedUrlExpirationSeconds: String,
        public val maxPendingUploadsForOpenUser: String,
        public val maxPendingUploadsForManagementUser: String,
        public val isDeduplicationEnabled: Boolean,
        public val deletedFilesCleanupIntervalSeconds: String,
        public val isSaving: Boolean = false,
        public val saveError: AppError? = null
    ) : EditStorageSettingsScreenState {
        public val hasChanges: Boolean
            get() = minUploadSizeBytes != initialSettings.minUploadSizeBytes.toString() ||
                maxUploadSizeBytes != initialSettings.maxUploadSizeBytes.toString() ||
                totalStorageQuotaForOpenUserBytes != (
                initialSettings.totalStorageQuotaForOpenUserBytes?.toString()
                    ?: ""
                ) ||
                totalStorageQuotaForManagementUserBytes !=
                (
                    initialSettings.totalStorageQuotaForManagementUserBytes?.toString()
                        ?: ""
                    ) ||
                presignedUrlExpirationSeconds != initialSettings.presignedUrlExpirationSeconds.toString() ||
                maxPendingUploadsForOpenUser != (initialSettings.maxPendingUploadsForOpenUser.toString()) ||
                maxPendingUploadsForManagementUser != (initialSettings.maxPendingUploadsForManagementUser.toString()) ||
                isDeduplicationEnabled != initialSettings.isDeduplicationEnabled ||
                deletedFilesCleanupIntervalSeconds != initialSettings.deletedFilesCleanupIntervalSeconds.toString()
    }
}
