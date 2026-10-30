package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.settings

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.ManagementStorageSettings

/**
 * UI state for editing platform storage settings.
 */
sealed interface EditStorageSettingsScreenState {
    /** Initial loading state. */
    data object Loading : EditStorageSettingsScreenState

    /** Loading error state. */
    data class Error(val error: AppError) : EditStorageSettingsScreenState

    /** Form content state. */
    data class Content(
        val initialSettings: ManagementStorageSettings,
        val minUploadSizeBytes: String,
        val maxUploadSizeBytes: String,
        val totalStorageQuotaForOpenUserBytes: String,
        val totalStorageQuotaForManagementUserBytes: String,
        val presignedUrlExpirationSeconds: String,
        val maxPendingUploadsForOpenUser: String,
        val maxPendingUploadsForManagementUser: String,
        val isDeduplicationEnabled: Boolean,
        val deletedFilesCleanupIntervalSeconds: String,
        val isSaving: Boolean = false,
        val saveError: AppError? = null
    ) : EditStorageSettingsScreenState {
        val hasChanges: Boolean
            get() = minUploadSizeBytes != initialSettings.minUploadSizeBytes.toString() ||
                maxUploadSizeBytes != initialSettings.maxUploadSizeBytes.toString() ||
                totalStorageQuotaForOpenUserBytes != (initialSettings.totalStorageQuotaForOpenUserBytes?.toString() ?: "") ||
                totalStorageQuotaForManagementUserBytes != (initialSettings.totalStorageQuotaForManagementUserBytes?.toString() ?: "") ||
                presignedUrlExpirationSeconds != initialSettings.presignedUrlExpirationSeconds.toString() ||
                maxPendingUploadsForOpenUser != (initialSettings.maxPendingUploadsForOpenUser.toString()) ||
                maxPendingUploadsForManagementUser != (initialSettings.maxPendingUploadsForManagementUser.toString()) ||
                isDeduplicationEnabled != initialSettings.isDeduplicationEnabled ||
                deletedFilesCleanupIntervalSeconds != initialSettings.deletedFilesCleanupIntervalSeconds.toString()
    }
}
