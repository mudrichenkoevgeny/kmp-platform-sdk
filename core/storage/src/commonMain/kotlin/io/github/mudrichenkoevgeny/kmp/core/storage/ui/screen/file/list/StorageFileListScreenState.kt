package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFileSummary

/**
 * Discriminated union representing the active UI state for [StorageFileListScreen].
 *
 * State transitions:
 * - Starts in [Loading] while initial page of files is retrieved.
 * - Transitions to [Content] on initial fetch success, or [Error] on failure.
 * - Subsequent page loads or local actions update fields in [Content] without dropping state.
 */
sealed interface StorageFileListScreenState {

    /** Loading state while initial file list is being fetched. */
    data object Loading : StorageFileListScreenState

    /**
     * Error state when loading initial file list failed.
     *
     * @param error Underlying application error.
     */
    data class Error(val error: AppError) : StorageFileListScreenState

    /**
     * Content state holding paged file items and active filters/selections.
     *
     * @param paging Active pagination state container holding file items and page metadata.
     * @param selectedFileIds Set of selected file IDs for batch operations.
     * @param isFilterPanelExpanded Whether the filter options panel is expanded.
     * @param sortState Active sorting configuration.
     * @param filterStates Active filters mapped by filter identifier.
     * @param actionLoading Whether an inline deletion or batch action is currently in flight.
     * @param actionError Error encountered during a deletion or batch action attempt, if any.
     * @param fileIdToDelete File ID pending single deletion confirmation dialog, or null if none.
     * @param showBatchDeleteConfirmation Whether the batch delete confirmation dialog is shown.
     */
    data class Content(
        val paging: PaginationState<StorageFileSummary> = PaginationState(),
        val selectedFileIds: Set<String> = emptySet(),
        val isFilterPanelExpanded: Boolean = false,
        val sortState: ListingSortState? = null,
        val filterStates: Map<String, ListingFilterState> = emptyMap(),
        val actionLoading: Boolean = false,
        val actionError: AppError? = null,
        val fileIdToDelete: String? = null,
        val showBatchDeleteConfirmation: Boolean = false
    ) : StorageFileListScreenState
}
