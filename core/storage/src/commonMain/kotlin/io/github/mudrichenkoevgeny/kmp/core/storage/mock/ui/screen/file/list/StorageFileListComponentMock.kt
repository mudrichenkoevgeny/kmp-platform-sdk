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

@InternalApi
class StorageFileListComponentMock(
    initialState: StorageFileListScreenState = StorageFileListScreenState.Loading,
    override val isSingleDeleteAvailable: Boolean = true,
    override val isBatchDeleteAvailable: Boolean = true,
    override val isUploadAvailable: Boolean = true,
    override val includeOwnerFilter: Boolean = false,
    initialDialogSlot: ChildSlot<StorageFileListDialogConfig, StorageFileListDialogChild>? = null
) : StorageFileListComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<StorageFileListScreenState> = _state

    override val dialogSlotNavigation = SlotNavigation<StorageFileListDialogConfig>()
    override val dialogSlot: Value<ChildSlot<StorageFileListDialogConfig, StorageFileListDialogChild>> =
        MutableValue(initialDialogSlot ?: ChildSlot())

    override fun onBackClick() {}
    override fun onToggleFilterPanel() {}
    override fun onRefresh() {}
    override fun onLoadNextPage() {}
    override fun onSortChanged(sortState: ListingSortState?) {}
    override fun onFilterChanged(filterId: String, filterState: ListingFilterState?) {}
    override fun onApplyFilters() {}
    override fun onUploadClick() {}
    override fun onFileClick(file: StorageFileSummary) {}
    override fun toggleSelectFile(fileId: String) {}
    override fun selectAllFiles() {}
    override fun clearFileSelection() {}
    override fun onDeleteFileClick(fileId: String) {}
    override fun onDeleteSelectedFilesClick() {}
    override fun onDismissDeleteDialog() {}
    override fun onConfirmDeleteFile() {}
    override fun onConfirmBatchDeleteFiles() {}
}
