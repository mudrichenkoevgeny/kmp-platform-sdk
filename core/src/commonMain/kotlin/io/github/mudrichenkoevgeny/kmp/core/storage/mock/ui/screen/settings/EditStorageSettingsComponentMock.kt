package io.github.mudrichenkoevgeny.kmp.core.storage.mock.ui.screen.settings

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.settings.EditStorageSettingsComponent
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.settings.EditStorageSettingsScreenState
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.settings.dialog.StorageSettingsDialogChild
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.settings.dialog.StorageSettingsDialogConfig
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.domain.model.storagesettings.createTestManagementStorageSettings

/**
 * Mock implementation of [EditStorageSettingsComponent] for testing and Compose previews.
 */
@InternalApi
public class EditStorageSettingsComponentMock(
    initialState: EditStorageSettingsScreenState = EditStorageSettingsScreenState.Content(
        initialSettings = createTestManagementStorageSettings(),
        minUploadSizeBytes = "1024",
        maxUploadSizeBytes = "104857600",
        totalStorageQuotaForOpenUserBytes = "1073741824",
        totalStorageQuotaForManagementUserBytes = "10737418240",
        presignedUrlExpirationSeconds = "3600",
        maxPendingUploadsForOpenUser = "10",
        maxPendingUploadsForManagementUser = "50",
        isDeduplicationEnabled = true,
        deletedFilesCleanupIntervalSeconds = "86400"
    ),
    initialDialogSlot: ChildSlot<StorageSettingsDialogConfig, StorageSettingsDialogChild>? = null
) : EditStorageSettingsComponent {

    private val _state = MutableValue(initialState)
    public override val state: Value<EditStorageSettingsScreenState> = _state

    public override val dialogSlotNavigation: SlotNavigation<StorageSettingsDialogConfig> =
        SlotNavigation<StorageSettingsDialogConfig>()
    public override val dialogSlot: Value<ChildSlot<StorageSettingsDialogConfig, StorageSettingsDialogChild>> =
        MutableValue(initialDialogSlot ?: ChildSlot())

    public var retryCalls: Int = 0
    public var saveCalls: Int = 0
    public var resetCalls: Int = 0
    public var confirmResetCalls: Int = 0
    public var dismissDialogCalls: Int = 0
    public var backCalls: Int = 0

    public override fun onRetry() {
        retryCalls++
    }

    public override fun onMinUploadSizeBytesChanged(value: String) {
        updateContent { copy(minUploadSizeBytes = value) }
    }

    public override fun onMaxUploadSizeBytesChanged(value: String) {
        updateContent { copy(maxUploadSizeBytes = value) }
    }

    public override fun onTotalStorageQuotaForOpenUserBytesChanged(value: String) {
        updateContent { copy(totalStorageQuotaForOpenUserBytes = value) }
    }

    public override fun onTotalStorageQuotaForManagementUserBytesChanged(value: String) {
        updateContent { copy(totalStorageQuotaForManagementUserBytes = value) }
    }

    public override fun onPresignedUrlExpirationSecondsChanged(value: String) {
        updateContent { copy(presignedUrlExpirationSeconds = value) }
    }

    public override fun onMaxPendingUploadsForOpenUserChanged(value: String) {
        updateContent { copy(maxPendingUploadsForOpenUser = value) }
    }

    public override fun onMaxPendingUploadsForManagementUserChanged(value: String) {
        updateContent { copy(maxPendingUploadsForManagementUser = value) }
    }

    public override fun onIsDeduplicationEnabledToggled(value: Boolean) {
        updateContent { copy(isDeduplicationEnabled = value) }
    }

    public override fun onDeletedFilesCleanupIntervalSecondsChanged(value: String) {
        updateContent { copy(deletedFilesCleanupIntervalSeconds = value) }
    }

    public override fun onSaveClick() {
        saveCalls++
    }

    public override fun onResetClick() {
        resetCalls++
    }

    public override fun onConfirmReset() {
        confirmResetCalls++
    }

    public override fun onDismissDialog() {
        dismissDialogCalls++
    }

    public override fun onBackClick() {
        backCalls++
    }

    /**
     * Emits a new [newState] to [state] for testing purposes.
     */
    public fun emitState(newState: EditStorageSettingsScreenState) {
        _state.value = newState
    }

    private inline fun updateContent(
        transform: EditStorageSettingsScreenState.Content.() -> EditStorageSettingsScreenState.Content
    ) {
        val current = _state.value as? EditStorageSettingsScreenState.Content ?: return
        _state.value = current.transform()
    }
}
