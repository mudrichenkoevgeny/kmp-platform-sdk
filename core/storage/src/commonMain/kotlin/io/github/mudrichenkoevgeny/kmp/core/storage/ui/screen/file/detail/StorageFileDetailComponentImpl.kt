package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnCreate
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail.datasource.StorageFileDetailDataSource
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail.dialog.StorageFileDetailDialogChild
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail.dialog.StorageFileDetailDialogConfig
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.navigation.screen.base.detail.StorageFileDetailParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.toUserIdOrNull
import kotlinx.coroutines.launch

/**
 * Universal Decompose component implementation for storage file detail screen.
 *
 * @param componentContext Decompose context for lifecycle management.
 * @param params Parsed screen parameters including file ID.
 * @param dataSource Data source contract for file detail operations.
 * @param onNavigateToUserDetail Optional callback to navigate to owner user detail.
 * @param onNavigateToUserMain Optional callback to navigate to user main.
 * @param onNavigateBack Callback for back navigation.
 * @param initialDialogConfig Optional initial modal dialog configuration.
 */
class StorageFileDetailComponentImpl(
    componentContext: ComponentContext,
    private val params: StorageFileDetailParams,
    private val dataSource: StorageFileDetailDataSource,
    private val onNavigateToUserDetail: ((userId: String) -> Unit)? = null,
    private val onNavigateToUserMain: (() -> Unit)? = null,
    private val onNavigateBack: () -> Unit,
    initialDialogConfig: StorageFileDetailDialogConfig? = null
) : StorageFileDetailComponent, ComponentContext by componentContext {

    override val isDeleteAvailable: Boolean
        get() = dataSource.isDeleteAvailable

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<StorageFileDetailScreenState>(StorageFileDetailScreenState.Loading)
    override val state: Value<StorageFileDetailScreenState> = _state

    override val dialogSlotNavigation = SlotNavigation<StorageFileDetailDialogConfig>()

    override val dialogSlot: Value<ChildSlot<StorageFileDetailDialogConfig, StorageFileDetailDialogChild>> =
        childSlot(
            source = dialogSlotNavigation,
            serializer = StorageFileDetailDialogConfig.serializer(),
            handleBackButton = true,
            childFactory = { config, _ ->
                when (config) {
                    is StorageFileDetailDialogConfig.ConfirmDeleteFile ->
                        StorageFileDetailDialogChild.ConfirmDeleteFile(config.fileId)
                }
            }
        )

    private var isFetching = false

    init {
        lifecycle.doOnCreate {
            initScreen()
        }
        initialDialogConfig?.let {
            dialogSlotNavigation.activate(it)
        }
    }

    override fun onRetry() {
        initScreen()
    }

    override fun onDeleteClick() {
        val current = _state.value as? StorageFileDetailScreenState.Content ?: return
        if (current.isDeleting || !isDeleteAvailable) {
            return
        }
        dialogSlotNavigation.activate(StorageFileDetailDialogConfig.ConfirmDeleteFile(params.fileId))
    }

    override fun onDismissDeleteDialog() {
        dialogSlotNavigation.dismiss()
    }

    override fun onConfirmDelete() {
        dialogSlotNavigation.dismiss()
        val current = _state.value as? StorageFileDetailScreenState.Content ?: return
        if (current.isDeleting || !isDeleteAvailable) {
            return
        }

        _state.value = current.copy(isDeleting = true, deleteError = null)

        scope.launch {
            when (val deleteResult = dataSource.deleteFile(params.fileId)) {
                is AppResult.Success -> {
                    onNavigateBack()
                }
                is AppResult.Error -> {
                    val latest = _state.value as? StorageFileDetailScreenState.Content ?: return@launch
                    _state.value = latest.copy(
                        isDeleting = false,
                        deleteError = deleteResult.error
                    )
                }
            }
        }
    }

    override fun onUserClick() {
        val current = _state.value as? StorageFileDetailScreenState.Content ?: return
        val rawOwnerUserId = current.file.ownerUserId?.trim() ?: return
        val typedOwnerUserId = rawOwnerUserId.toUserIdOrNull() ?: return

        if (onNavigateToUserDetail != null) {
            onNavigateToUserDetail.invoke(typedOwnerUserId.asHexDashString())
        } else if (onNavigateToUserMain != null) {
            onNavigateToUserMain.invoke()
        }
    }

    override fun onBackClick() {
        onNavigateBack()
    }

    private fun initScreen() {
        if (isFetching) {
            return
        }
        isFetching = true
        _state.value = StorageFileDetailScreenState.Loading

        scope.launch {
            try {
                when (val result = dataSource.getFileDetail(params.fileId)) {
                    is AppResult.Success -> {
                        _state.value = StorageFileDetailScreenState.Content(
                            file = result.data,
                            isDeleting = false,
                            deleteError = null
                        )
                    }
                    is AppResult.Error -> {
                        _state.value = StorageFileDetailScreenState.Error(result.error)
                    }
                }
            } finally {
                isFetching = false
            }
        }
    }
}
