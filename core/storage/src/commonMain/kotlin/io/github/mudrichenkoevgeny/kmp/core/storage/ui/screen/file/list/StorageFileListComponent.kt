package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.dialog.StorageFileListDialogChild
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.dialog.StorageFileListDialogConfig
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFileSummary

/**
 * Universal Decompose component contract for storage file listing screens (client & management).
 */
interface StorageFileListComponent {

    /** Observable screen state flow. */
    val state: Value<StorageFileListScreenState>

    /** Decompose child slot for file list confirmation dialogs. */
    val dialogSlot: Value<ChildSlot<StorageFileListDialogConfig, StorageFileListDialogChild>>

    /** Slot navigator for file list confirmation dialogs. */
    val dialogSlotNavigation: SlotNavigation<StorageFileListDialogConfig>

    /** Whether single file delete action is enabled for this contour. */
    val isSingleDeleteAvailable: Boolean

    /** Whether batch file delete action is enabled for this contour. */
    val isBatchDeleteAvailable: Boolean

    /** Whether the upload action is available for this contour. */
    val isUploadAvailable: Boolean

    /** Whether the ownerUserId filter is included (administrative mode). */
    val includeOwnerFilter: Boolean

    /** Triggers navigation back to previous screen. */
    fun onBackClick()

    /** Toggles the expansion state of the filter options panel. */
    fun onToggleFilterPanel()

    /** Refreshes current file list page. */
    fun onRefresh()

    /** Triggers loading the next page. */
    fun onLoadNextPage()

    /** Called when active sort state changes. */
    fun onSortChanged(sortState: ListingSortState?)

    /** Called when an individual filter state changes. */
    fun onFilterChanged(filterId: String, filterState: ListingFilterState?)

    /** Applies selected filters and re-fetches list. */
    fun onApplyFilters()

    /** Navigates to file upload screen. */
    fun onUploadClick()

    /** Navigates to detail view for the specified file. */
    fun onFileClick(file: StorageFileSummary)

    /** Toggles multi-selection state for a file ID. */
    fun toggleSelectFile(fileId: String)

    /** Selects all files in current page. */
    fun selectAllFiles()

    /** Clears selection. */
    fun clearFileSelection()

    /** Opens single file deletion confirmation dialog. */
    fun onDeleteFileClick(fileId: String)

    /** Opens batch deletion confirmation dialog. */
    fun onDeleteSelectedFilesClick()

    /** Dismisses delete confirmation dialogs. */
    fun onDismissDeleteDialog()

    /** Confirms single file deletion. */
    fun onConfirmDeleteFile()

    /** Confirms batch file deletion. */
    fun onConfirmBatchDeleteFiles()
}
