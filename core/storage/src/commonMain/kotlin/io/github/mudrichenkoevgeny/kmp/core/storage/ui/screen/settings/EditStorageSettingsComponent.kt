package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.settings

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.settings.dialog.StorageSettingsDialogChild
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.settings.dialog.StorageSettingsDialogConfig

/**
 * Decompose controller for managing platform storage settings screen.
 */
interface EditStorageSettingsComponent {
    /** Observable state of the edit storage settings screen. */
    val state: Value<EditStorageSettingsScreenState>

    /** Decompose child slot for storage settings confirmation dialogs. */
    val dialogSlot: Value<ChildSlot<StorageSettingsDialogConfig, StorageSettingsDialogChild>>

    /** Slot navigator for storage settings dialogs. */
    val dialogSlotNavigation: SlotNavigation<StorageSettingsDialogConfig>

    /** Retries initializing screen data after an error. */
    fun onRetry()

    /** Callback invoked when min upload size in bytes changes. */
    fun onMinUploadSizeBytesChanged(value: String)

    /** Callback invoked when max upload size in bytes changes. */
    fun onMaxUploadSizeBytesChanged(value: String)

    /** Callback invoked when total quota for open users changes. */
    fun onTotalStorageQuotaForOpenUserBytesChanged(value: String)

    /** Callback invoked when total quota for management users changes. */
    fun onTotalStorageQuotaForManagementUserBytesChanged(value: String)

    /** Callback invoked when presigned URL expiration in seconds changes. */
    fun onPresignedUrlExpirationSecondsChanged(value: String)

    /** Callback invoked when max pending uploads for open users changes. */
    fun onMaxPendingUploadsForOpenUserChanged(value: String)

    /** Callback invoked when max pending uploads for management users changes. */
    fun onMaxPendingUploadsForManagementUserChanged(value: String)

    /** Callback invoked when deduplication setting is toggled. */
    fun onIsDeduplicationEnabledToggled(value: Boolean)

    /** Callback invoked when deleted files cleanup interval in seconds changes. */
    fun onDeletedFilesCleanupIntervalSecondsChanged(value: String)

    /** Submits modified storage settings. */
    fun onSaveClick()

    /** Shows reset confirmation dialog. */
    fun onResetClick()

    /** Confirms resetting storage settings to defaults. */
    fun onConfirmReset()

    /** Dismisses active confirmation dialog. */
    fun onDismissDialog()

    /** Handles back navigation action. */
    fun onBackClick()
}
