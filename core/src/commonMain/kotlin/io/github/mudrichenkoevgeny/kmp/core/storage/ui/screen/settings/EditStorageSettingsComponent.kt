package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.settings

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.settings.dialog.StorageSettingsDialogChild
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.settings.dialog.StorageSettingsDialogConfig

/**
 * Decompose controller for managing platform storage settings screen.
 */
public interface EditStorageSettingsComponent {
    /** Observable state of the edit storage settings screen. */
    public val state: Value<EditStorageSettingsScreenState>

    /** Decompose child slot for storage settings confirmation dialogs. */
    public val dialogSlot: Value<ChildSlot<StorageSettingsDialogConfig, StorageSettingsDialogChild>>

    /** Slot navigator for storage settings dialogs. */
    public val dialogSlotNavigation: SlotNavigation<StorageSettingsDialogConfig>

    /** Retries initializing screen data after an error. */
    public fun onRetry()

    /** Callback invoked when min upload size in bytes changes. */
    public fun onMinUploadSizeBytesChanged(value: String)

    /** Callback invoked when max upload size in bytes changes. */
    public fun onMaxUploadSizeBytesChanged(value: String)

    /** Callback invoked when total quota for open users changes. */
    public fun onTotalStorageQuotaForOpenUserBytesChanged(value: String)

    /** Callback invoked when total quota for management users changes. */
    public fun onTotalStorageQuotaForManagementUserBytesChanged(value: String)

    /** Callback invoked when presigned URL expiration in seconds changes. */
    public fun onPresignedUrlExpirationSecondsChanged(value: String)

    /** Callback invoked when max pending uploads for open users changes. */
    public fun onMaxPendingUploadsForOpenUserChanged(value: String)

    /** Callback invoked when max pending uploads for management users changes. */
    public fun onMaxPendingUploadsForManagementUserChanged(value: String)

    /** Callback invoked when deduplication setting is toggled. */
    public fun onIsDeduplicationEnabledToggled(value: Boolean)

    /** Callback invoked when deleted files cleanup interval in seconds changes. */
    public fun onDeletedFilesCleanupIntervalSecondsChanged(value: String)

    /** Submits modified storage settings. */
    public fun onSaveClick()

    /** Shows reset confirmation dialog. */
    public fun onResetClick()

    /** Confirms resetting storage settings to defaults. */
    public fun onConfirmReset()

    /** Dismisses active confirmation dialog. */
    public fun onDismissDialog()

    /** Handles back navigation action. */
    public fun onBackClick()
}
