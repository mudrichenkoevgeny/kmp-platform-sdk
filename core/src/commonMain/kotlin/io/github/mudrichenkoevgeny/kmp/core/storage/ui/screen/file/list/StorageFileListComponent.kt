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
public interface StorageFileListComponent {

    /** Observable screen state flow. */
    public val state: Value<StorageFileListScreenState>

    /** Decompose child slot for file list confirmation dialogs. */
    public val dialogSlot: Value<ChildSlot<StorageFileListDialogConfig, StorageFileListDialogChild>>

    /** Slot navigator for file list confirmation dialogs. */
    public val dialogSlotNavigation: SlotNavigation<StorageFileListDialogConfig>

    /** Whether single file delete action is enabled for this contour. */
    public val isSingleDeleteAvailable: Boolean

    /** Whether batch file delete action is enabled for this contour. */
    public val isBatchDeleteAvailable: Boolean

    /** Whether the upload action is available for this contour. */
    public val isUploadAvailable: Boolean

    /** Whether the ownerUserId filter is included (administrative mode). */
    public val includeOwnerFilter: Boolean

    /** Triggers navigation back to previous screen. */
    public fun onBackClick()

    /** Toggles the expansion state of the filter options panel. */
    public fun onToggleFilterPanel()

    /** Refreshes current file list page. */
    public fun onRefresh()

    /** Triggers loading the next page. */
    public fun onLoadNextPage()

    /** Called when active sort state changes. */
    public fun onSortChanged(sortState: ListingSortState?)

    /** Called when an individual filter state changes. */
    public fun onFilterChanged(
        filterId: String,
        filterState: ListingFilterState?
    )

    /** Applies selected filters and re-fetches list. */
    public fun onApplyFilters()

    /** Navigates to file upload screen. */
    public fun onUploadClick()

    /** Navigates to detail view for the specified file. */
    public fun onFileClick(file: StorageFileSummary)

    /** Toggles multi-selection state for a file ID. */
    public fun toggleSelectFile(fileId: String)

    /** Selects all files in current page. */
    public fun selectAllFiles()

    /** Clears selection. */
    public fun clearFileSelection()

    /** Opens single file deletion confirmation dialog. */
    public fun onDeleteFileClick(fileId: String)

    /** Opens batch deletion confirmation dialog. */
    public fun onDeleteSelectedFilesClick()

    /** Dismisses delete confirmation dialogs. */
    public fun onDismissDeleteDialog()

    /** Confirms single file deletion. */
    public fun onConfirmDeleteFile()

    /** Confirms batch file deletion. */
    public fun onConfirmBatchDeleteFiles()
}
