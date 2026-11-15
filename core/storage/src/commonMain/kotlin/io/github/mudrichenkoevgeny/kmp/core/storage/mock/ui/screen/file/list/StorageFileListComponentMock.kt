package io.github.mudrichenkoevgeny.kmp.core.storage.mock.ui.screen.file.list

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.StorageFileListComponent
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.StorageFileListScreenState
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.dialog.StorageFileListDialogChild
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.dialog.StorageFileListDialogConfig
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFileSummary

/**
 * Mock implementation of [StorageFileListComponent] for testing and Compose previews.
 */
@InternalApi
public class StorageFileListComponentMock(
    initialState: StorageFileListScreenState = StorageFileListScreenState.Loading,
    public override val isSingleDeleteAvailable: Boolean = true,
    public override val isBatchDeleteAvailable: Boolean = true,
    public override val isUploadAvailable: Boolean = true,
    public override val includeOwnerFilter: Boolean = false,
    initialDialogSlot: ChildSlot<StorageFileListDialogConfig, StorageFileListDialogChild>? = null
) : StorageFileListComponent {

    private val _state = MutableValue(initialState)
    public override val state: Value<StorageFileListScreenState> = _state

    public override val dialogSlotNavigation: SlotNavigation<StorageFileListDialogConfig> =
        SlotNavigation<StorageFileListDialogConfig>()
    public override val dialogSlot: Value<ChildSlot<StorageFileListDialogConfig, StorageFileListDialogChild>> =
        MutableValue(initialDialogSlot ?: ChildSlot())

    public override fun onBackClick(): Unit = Unit
    public override fun onToggleFilterPanel(): Unit = Unit
    public override fun onRefresh(): Unit = Unit
    public override fun onLoadNextPage(): Unit = Unit
    public override fun onSortChanged(sortState: ListingSortState?): Unit = Unit
    public override fun onFilterChanged(
        filterId: String,
        filterState: ListingFilterState?
    ): Unit = Unit

    public override fun onApplyFilters(): Unit = Unit
    public override fun onUploadClick(): Unit = Unit
    public override fun onFileClick(file: StorageFileSummary): Unit = Unit
    public override fun toggleSelectFile(fileId: String): Unit = Unit
    public override fun selectAllFiles(): Unit = Unit
    public override fun clearFileSelection(): Unit = Unit
    public override fun onDeleteFileClick(fileId: String): Unit = Unit
    public override fun onDeleteSelectedFilesClick(): Unit = Unit
    public override fun onDismissDeleteDialog(): Unit = Unit
    public override fun onConfirmDeleteFile(): Unit = Unit
    public override fun onConfirmBatchDeleteFiles(): Unit = Unit

    /**
     * Emits a new [newState] to [state] for testing purposes.
     */
    public fun emitState(newState: StorageFileListScreenState) {
        _state.value = newState
    }
}
