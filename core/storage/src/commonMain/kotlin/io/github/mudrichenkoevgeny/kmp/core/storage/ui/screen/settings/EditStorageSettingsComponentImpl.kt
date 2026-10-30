package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.settings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnDestroy
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.settings.dialog.StorageSettingsDialogChild
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.settings.dialog.StorageSettingsDialogConfig
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.GetManagementStorageSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.ResetRemoteManagementStorageSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.UpdateManagementStorageSettingsUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.ManagementStorageSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Default implementation of [EditStorageSettingsComponent].
 *
 * @param componentContext Decompose component context.
 * @param initialDialogConfig Optional initial modal dialog configuration.
 * @param getSettingsUseCase Use case to retrieve storage settings.
 * @param updateSettingsUseCase Use case to save storage settings.
 * @param resetSettingsUseCase Use case to reset storage settings.
 * @param onBack Callback invoked on back navigation request.
 */
class EditStorageSettingsComponentImpl(
    componentContext: ComponentContext,
    initialDialogConfig: StorageSettingsDialogConfig? = null,
    private val getSettingsUseCase: GetManagementStorageSettingsUseCase,
    private val updateSettingsUseCase: UpdateManagementStorageSettingsUseCase,
    private val resetSettingsUseCase: ResetRemoteManagementStorageSettingsUseCase,
    private val onBack: () -> Unit
) : EditStorageSettingsComponent, ComponentContext by componentContext {

    private val _state = MutableValue<EditStorageSettingsScreenState>(EditStorageSettingsScreenState.Loading)
    override val state: Value<EditStorageSettingsScreenState> = _state

    override val dialogSlotNavigation = SlotNavigation<StorageSettingsDialogConfig>()

    override val dialogSlot: Value<ChildSlot<StorageSettingsDialogConfig, StorageSettingsDialogChild>> =
        childSlot(
            source = dialogSlotNavigation,
            serializer = StorageSettingsDialogConfig.serializer(),
            handleBackButton = true,
            childFactory = { config, _ ->
                when (config) {
                    is StorageSettingsDialogConfig.ConfirmReset -> StorageSettingsDialogChild.ConfirmReset
                }
            }
        )

    private val componentScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    init {
        lifecycle.doOnDestroy {
            componentScope.cancel()
        }
        loadSettings()
        initialDialogConfig?.let {
            dialogSlotNavigation.activate(it)
        }
    }

    override fun onRetry() {
        loadSettings()
    }

    override fun onMinUploadSizeBytesChanged(value: String) {
        updateContent { copy(minUploadSizeBytes = value, saveError = null) }
    }

    override fun onMaxUploadSizeBytesChanged(value: String) {
        updateContent { copy(maxUploadSizeBytes = value, saveError = null) }
    }

    override fun onTotalStorageQuotaForOpenUserBytesChanged(value: String) {
        updateContent { copy(totalStorageQuotaForOpenUserBytes = value, saveError = null) }
    }

    override fun onTotalStorageQuotaForManagementUserBytesChanged(value: String) {
        updateContent { copy(totalStorageQuotaForManagementUserBytes = value, saveError = null) }
    }

    override fun onPresignedUrlExpirationSecondsChanged(value: String) {
        updateContent { copy(presignedUrlExpirationSeconds = value, saveError = null) }
    }

    override fun onMaxPendingUploadsForOpenUserChanged(value: String) {
        updateContent { copy(maxPendingUploadsForOpenUser = value, saveError = null) }
    }

    override fun onMaxPendingUploadsForManagementUserChanged(value: String) {
        updateContent { copy(maxPendingUploadsForManagementUser = value, saveError = null) }
    }

    override fun onIsDeduplicationEnabledToggled(value: Boolean) {
        updateContent { copy(isDeduplicationEnabled = value, saveError = null) }
    }

    override fun onDeletedFilesCleanupIntervalSecondsChanged(value: String) {
        updateContent { copy(deletedFilesCleanupIntervalSeconds = value, saveError = null) }
    }

    override fun onSaveClick() {
        val current = _state.value as? EditStorageSettingsScreenState.Content ?: return
        if (!current.hasChanges || current.isSaving) {
            return
        }

        _state.value = current.copy(isSaving = true, saveError = null)

        componentScope.launch {
            val updatedSettings = ManagementStorageSettings(
                minUploadSizeBytes = current.minUploadSizeBytes.toLongOrNull() ?: 0L,
                maxUploadSizeBytes = current.maxUploadSizeBytes.toLongOrNull() ?: 0L,
                totalStorageQuotaForOpenUserBytes = current.totalStorageQuotaForOpenUserBytes.toLongOrNull(),
                totalStorageQuotaForManagementUserBytes = current.totalStorageQuotaForManagementUserBytes.toLongOrNull(),
                presignedUrlExpirationSeconds = current.presignedUrlExpirationSeconds.toIntOrNull() ?: 0,
                maxPendingUploadsForOpenUser = current.maxPendingUploadsForOpenUser.toIntOrNull() ?: 0,
                maxPendingUploadsForManagementUser = current.maxPendingUploadsForManagementUser.toIntOrNull() ?: 0,
                isDeduplicationEnabled = current.isDeduplicationEnabled,
                deletedFilesCleanupIntervalSeconds = current.deletedFilesCleanupIntervalSeconds.toIntOrNull() ?: 0
            )

            val saveResult = updateSettingsUseCase(updatedSettings)
            val nextState = when (saveResult) {
                is AppResult.Success -> {
                    onBack()
                    current.copy(isSaving = false)
                }
                is AppResult.Error -> current.copy(isSaving = false, saveError = saveResult.error)
            }
            _state.value = nextState
        }
    }

    override fun onResetClick() {
        dialogSlotNavigation.activate(StorageSettingsDialogConfig.ConfirmReset)
    }

    override fun onConfirmReset() {
        dialogSlotNavigation.dismiss()
        val current = _state.value as? EditStorageSettingsScreenState.Content ?: return
        if (current.isSaving) {
            return
        }

        _state.value = current.copy(isSaving = true, saveError = null)

        componentScope.launch {
            val result = resetSettingsUseCase()
            val nextState = when (result) {
                is AppResult.Success -> {
                    onBack()
                    current.copy(isSaving = false)
                }
                is AppResult.Error -> current.copy(isSaving = false, saveError = result.error)
            }
            _state.value = nextState
        }
    }

    override fun onDismissDialog() {
        dialogSlotNavigation.dismiss()
    }

    override fun onBackClick() {
        onBack()
    }

    private fun loadSettings() {
        _state.value = EditStorageSettingsScreenState.Loading
        componentScope.launch {
            val getSettingsResult = getSettingsUseCase()
            when (getSettingsResult) {
                is AppResult.Success -> {
                    val settings = getSettingsResult.data
                    _state.value = EditStorageSettingsScreenState.Content(
                        initialSettings = settings,
                        minUploadSizeBytes = settings.minUploadSizeBytes.toString(),
                        maxUploadSizeBytes = settings.maxUploadSizeBytes.toString(),
                        totalStorageQuotaForOpenUserBytes = settings.totalStorageQuotaForOpenUserBytes?.toString().orEmpty(),
                        totalStorageQuotaForManagementUserBytes = settings.totalStorageQuotaForManagementUserBytes?.toString().orEmpty(),
                        presignedUrlExpirationSeconds = settings.presignedUrlExpirationSeconds.toString(),
                        maxPendingUploadsForOpenUser = settings.maxPendingUploadsForOpenUser.toString(),
                        maxPendingUploadsForManagementUser = settings.maxPendingUploadsForManagementUser.toString(),
                        isDeduplicationEnabled = settings.isDeduplicationEnabled,
                        deletedFilesCleanupIntervalSeconds = settings.deletedFilesCleanupIntervalSeconds.toString(),
                        isSaving = false,
                        saveError = null
                    )
                }
                is AppResult.Error -> {
                    _state.value = EditStorageSettingsScreenState.Error(getSettingsResult.error)
                }
            }
        }
    }

    private inline fun updateContent(transform: EditStorageSettingsScreenState.Content.() -> EditStorageSettingsScreenState.Content) {
        val current = _state.value as? EditStorageSettingsScreenState.Content ?: return
        _state.value = current.transform()
    }
}
