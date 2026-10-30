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
class EditStorageSettingsComponentMock(
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
    override val state: Value<EditStorageSettingsScreenState> = _state

    override val dialogSlotNavigation = SlotNavigation<StorageSettingsDialogConfig>()
    override val dialogSlot: Value<ChildSlot<StorageSettingsDialogConfig, StorageSettingsDialogChild>> =
        MutableValue(initialDialogSlot ?: ChildSlot())

    var retryCalls = 0
    var saveCalls = 0
    var resetCalls = 0
    var confirmResetCalls = 0
    var dismissDialogCalls = 0
    var backCalls = 0

    override fun onRetry() {
        retryCalls++
    }

    override fun onMinUploadSizeBytesChanged(value: String) {
        updateContent { copy(minUploadSizeBytes = value) }
    }

    override fun onMaxUploadSizeBytesChanged(value: String) {
        updateContent { copy(maxUploadSizeBytes = value) }
    }

    override fun onTotalStorageQuotaForOpenUserBytesChanged(value: String) {
        updateContent { copy(totalStorageQuotaForOpenUserBytes = value) }
    }

    override fun onTotalStorageQuotaForManagementUserBytesChanged(value: String) {
        updateContent { copy(totalStorageQuotaForManagementUserBytes = value) }
    }

    override fun onPresignedUrlExpirationSecondsChanged(value: String) {
        updateContent { copy(presignedUrlExpirationSeconds = value) }
    }

    override fun onMaxPendingUploadsForOpenUserChanged(value: String) {
        updateContent { copy(maxPendingUploadsForOpenUser = value) }
    }

    override fun onMaxPendingUploadsForManagementUserChanged(value: String) {
        updateContent { copy(maxPendingUploadsForManagementUser = value) }
    }

    override fun onIsDeduplicationEnabledToggled(value: Boolean) {
        updateContent { copy(isDeduplicationEnabled = value) }
    }

    override fun onDeletedFilesCleanupIntervalSecondsChanged(value: String) {
        updateContent { copy(deletedFilesCleanupIntervalSeconds = value) }
    }

    override fun onSaveClick() {
        saveCalls++
    }

    override fun onResetClick() {
        resetCalls++
    }

    override fun onConfirmReset() {
        confirmResetCalls++
    }

    override fun onDismissDialog() {
        dismissDialogCalls++
    }

    override fun onBackClick() {
        backCalls++
    }

    fun emitState(newState: EditStorageSettingsScreenState) {
        _state.value = newState
    }

    private inline fun updateContent(transform: EditStorageSettingsScreenState.Content.() -> EditStorageSettingsScreenState.Content) {
        val current = _state.value as? EditStorageSettingsScreenState.Content ?: return
        _state.value = current.transform()
    }
}
