package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list

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
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.ListingConstants.INITIAL_PAGE_NUMBER
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.appendResult
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.toNextPageLoading
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.datasource.StorageFileListDataSource
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.datasource.StorageFileListQuery
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.dialog.StorageFileListDialogChild
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.dialog.StorageFileListDialogConfig
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFileSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.listing.StorageFilterValues.StorageFileFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.status.StorageFileStatus
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.file.DeleteFilesBatchRequest
import kotlinx.coroutines.launch

/**
 * Universal Decompose component implementation for storage file listing screens (client & management).
 *
 * @param componentContext Decompose context for lifecycle management.
 * @param dataSource Data source contract for file listing and deletion operations.
 * @param includeOwnerFilter Whether the ownerUserId filter is included (administrative mode).
 * @param initialUserId Optional target user or owner ID for filtering user-specific files.
 * @param initialUserIdProvider Optional provider to lazily resolve target owner user ID.
 * @param initialDialogConfig Optional initial modal dialog configuration.
 * @param onNavigateToUpload Optional callback to navigate to upload screen.
 * @param onNavigateToFileDetail Optional callback to navigate to file detail screen.
 * @param onNavigateBack Callback for back navigation.
 */
class StorageFileListComponentImpl(
    componentContext: ComponentContext,
    private val dataSource: StorageFileListDataSource,
    override val includeOwnerFilter: Boolean = false,
    private val initialUserId: String? = null,
    private val initialUserIdProvider: (suspend () -> String?)? = null,
    initialFilterStates: Map<String, ListingFilterState>? = null,
    initialDialogConfig: StorageFileListDialogConfig? = null,
    private val onNavigateToUpload: (() -> Unit)? = null,
    private val onNavigateToFileDetail: ((String) -> Unit)? = null,
    private val onNavigateBack: () -> Unit
) : StorageFileListComponent, ComponentContext by componentContext {

    override val isSingleDeleteAvailable: Boolean
        get() = dataSource.isSingleDeleteAvailable

    override val isBatchDeleteAvailable: Boolean
        get() = dataSource.isBatchDeleteAvailable

    override val isUploadAvailable: Boolean
        get() = onNavigateToUpload != null

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<StorageFileListScreenState>(StorageFileListScreenState.Loading)
    override val state: Value<StorageFileListScreenState> = _state

    override val dialogSlotNavigation = SlotNavigation<StorageFileListDialogConfig>()

    override val dialogSlot: Value<ChildSlot<StorageFileListDialogConfig, StorageFileListDialogChild>> =
        childSlot(
            source = dialogSlotNavigation,
            serializer = StorageFileListDialogConfig.serializer(),
            handleBackButton = true,
            childFactory = { config, _ ->
                when (config) {
                    is StorageFileListDialogConfig.ConfirmDeleteFile ->
                        StorageFileListDialogChild.ConfirmDeleteFile(config.fileId)
                    is StorageFileListDialogConfig.ConfirmDeleteBatchFiles ->
                        StorageFileListDialogChild.ConfirmDeleteBatchFiles
                }
            }
        )

    private var isFetching = false
    private var activeSortState: ListingSortState? = null
    private var activeFilterStates: Map<String, ListingFilterState> = initialFilterStates ?: getDefaultFilterStates()
    private var isFilterPanelExpanded: Boolean = false

    companion object {
        /**
         * Returns default listing filter states excluding [StorageFileStatus.DELETED].
         */
        fun getDefaultFilterStates(): Map<String, ListingFilterState> {
            val nonDeletedStatusIds = StorageFileStatus.entries
                .filter { it != StorageFileStatus.DELETED }
                .map { it.name }
                .toSet()
            return mapOf(
                StorageFileFilterValues.STATUS to ChoiceListingFilterState(selectedIds = nonDeletedStatusIds)
            )
        }
    }

    init {
        lifecycle.doOnCreate {
            initScreen()
        }
        initialDialogConfig?.let {
            dialogSlotNavigation.activate(it)
        }
    }

    private fun initScreen() {
        if (isFetching || _state.value is StorageFileListScreenState.Content) {
            return
        }
        _state.value = StorageFileListScreenState.Loading
        fetchPage(pageNumber = INITIAL_PAGE_NUMBER)
    }

    override fun onBackClick() {
        onNavigateBack()
    }

    override fun onToggleFilterPanel() {
        val current = _state.value
        if (current is StorageFileListScreenState.Content) {
            val expanded = !current.isFilterPanelExpanded
            isFilterPanelExpanded = expanded
            _state.value = current.copy(isFilterPanelExpanded = expanded)
        }
    }

    override fun onRefresh() {
        _state.value = StorageFileListScreenState.Loading
        fetchPage(pageNumber = INITIAL_PAGE_NUMBER)
    }

    override fun onLoadNextPage() {
        val current = _state.value
        if (current !is StorageFileListScreenState.Content || !current.paging.hasMorePages) {
            return
        }
        _state.value = current.copy(paging = current.paging.toNextPageLoading())
        fetchPage(pageNumber = current.paging.nextPageNumber)
    }

    override fun onSortChanged(sortState: ListingSortState?) {
        activeSortState = sortState
        val current = _state.value
        if (current is StorageFileListScreenState.Content) {
            _state.value = current.copy(sortState = sortState)
        }
    }

    override fun onFilterChanged(filterId: String, filterState: ListingFilterState?) {
        val newFilters = activeFilterStates.toMutableMap()
        if (filterState == null) {
            newFilters.remove(filterId)
        } else {
            newFilters[filterId] = filterState
        }
        activeFilterStates = newFilters

        val current = _state.value
        if (current is StorageFileListScreenState.Content) {
            _state.value = current.copy(filterStates = newFilters)
        }
    }

    override fun onApplyFilters() {
        _state.value = StorageFileListScreenState.Loading
        fetchPage(pageNumber = INITIAL_PAGE_NUMBER)
    }

    override fun onUploadClick() {
        onNavigateToUpload?.invoke()
    }

    override fun onFileClick(file: StorageFileSummary) {
        onNavigateToFileDetail?.invoke(file.id.asHexDashString())
    }

    override fun toggleSelectFile(fileId: String) {
        val current = _state.value
        if (current is StorageFileListScreenState.Content) {
            val isSelected = current.selectedFileIds.contains(fileId)
            val newSelection = if (isSelected) {
                current.selectedFileIds - fileId
            } else {
                current.selectedFileIds + fileId
            }
            _state.value = current.copy(selectedFileIds = newSelection)
        }
    }

    override fun selectAllFiles() {
        val current = _state.value
        if (current is StorageFileListScreenState.Content) {
            val allIds = current.paging.items
                .filter { it.status != StorageFileStatus.DELETED }
                .map { it.id.asHexDashString() }
                .toSet()
            _state.value = current.copy(selectedFileIds = allIds)
        }
    }

    override fun clearFileSelection() {
        val current = _state.value
        if (current is StorageFileListScreenState.Content) {
            _state.value = current.copy(selectedFileIds = emptySet())
        }
    }

    override fun onDeleteFileClick(fileId: String) {
        dialogSlotNavigation.activate(StorageFileListDialogConfig.ConfirmDeleteFile(fileId))
    }

    override fun onDeleteSelectedFilesClick() {
        dialogSlotNavigation.activate(StorageFileListDialogConfig.ConfirmDeleteBatchFiles)
    }

    override fun onDismissDeleteDialog() {
        dialogSlotNavigation.dismiss()
    }

    override fun onConfirmDeleteFile() {
        val activeChild = dialogSlot.value.child?.instance as? StorageFileListDialogChild.ConfirmDeleteFile
        val fileId = activeChild?.fileId
        dialogSlotNavigation.dismiss()

        if (fileId == null || !dataSource.isSingleDeleteAvailable) {
            return
        }

        val current = _state.value as? StorageFileListScreenState.Content ?: return
        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            val deleteResult = dataSource.deleteFile(fileId)
            when (deleteResult) {
                is AppResult.Success -> {
                    val latest = _state.value
                    if (latest is StorageFileListScreenState.Content) {
                        _state.value = applyLocalDeletion(latest, listOf(fileId))
                    }
                }
                is AppResult.Error -> {
                    val latest = _state.value
                    if (latest is StorageFileListScreenState.Content) {
                        _state.value = latest.copy(
                            actionLoading = false,
                            actionError = deleteResult.error
                        )
                    }
                }
            }
        }
    }

    override fun onConfirmBatchDeleteFiles() {
        dialogSlotNavigation.dismiss()
        val current = _state.value as? StorageFileListScreenState.Content ?: return
        val fileIds = current.selectedFileIds.toList()

        if (fileIds.isEmpty() || !dataSource.isBatchDeleteAvailable) {
            return
        }

        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            val request = DeleteFilesBatchRequest(fileIds = fileIds)
            val batchDeleteResult = dataSource.deleteFilesBatch(request)
            when (batchDeleteResult) {
                is AppResult.Success -> {
                    val latest = _state.value
                    if (latest is StorageFileListScreenState.Content) {
                        _state.value = applyLocalDeletion(latest, fileIds)
                    }
                }
                is AppResult.Error -> {
                    val latest = _state.value
                    if (latest is StorageFileListScreenState.Content) {
                        _state.value = latest.copy(
                            actionLoading = false,
                            actionError = batchDeleteResult.error
                        )
                    }
                }
            }
        }
    }

    private fun fetchPage(pageNumber: Int) {
        if (isFetching) {
            return
        }
        isFetching = true

        scope.launch {
            try {
                val targetOwnerUserId = initialUserId ?: initialUserIdProvider?.invoke()

                val query = StorageFileListQuery(
                    pageNumber = pageNumber,
                    sortState = activeSortState,
                    filterStates = activeFilterStates,
                    ownerUserId = targetOwnerUserId
                )

                val filesResult = dataSource.getFiles(query)
                val current = _state.value

                when (filesResult) {
                    is AppResult.Success -> {
                        val currentPaging = if (current is StorageFileListScreenState.Content) {
                            current.paging
                        } else {
                            PaginationState()
                        }
                        val nextPaging = currentPaging.appendResult(filesResult.data)

                        when (current) {
                            is StorageFileListScreenState.Content -> {
                                _state.value = current.copy(
                                    paging = nextPaging,
                                    actionLoading = false
                                )
                            }
                            is StorageFileListScreenState.Loading,
                            is StorageFileListScreenState.Error -> {
                                _state.value = StorageFileListScreenState.Content(
                                    paging = nextPaging,
                                    selectedFileIds = emptySet(),
                                    isFilterPanelExpanded = isFilterPanelExpanded,
                                    sortState = activeSortState,
                                    filterStates = activeFilterStates,
                                    actionLoading = false,
                                    actionError = null
                                )
                            }
                        }
                    }
                    is AppResult.Error -> {
                        when (current) {
                            is StorageFileListScreenState.Content -> {
                                _state.value = current.copy(
                                    actionLoading = false,
                                    actionError = filesResult.error
                                )
                            }
                            is StorageFileListScreenState.Loading,
                            is StorageFileListScreenState.Error -> {
                                _state.value = StorageFileListScreenState.Error(
                                    error = filesResult.error
                                )
                            }
                        }
                    }
                }
            } finally {
                isFetching = false
            }
        }
    }

    private fun applyLocalDeletion(
        current: StorageFileListScreenState.Content,
        deletedIds: List<String>
    ): StorageFileListScreenState.Content {
        val statusFilter = current.filterStates[StorageFileFilterValues.STATUS] as? ChoiceListingFilterState
        val isDeletedStatusIncluded = statusFilter?.selectedIds?.any { raw ->
            raw.equals(StorageFileStatus.DELETED.name, ignoreCase = true) ||
                raw.equals(StorageFileStatus.DELETED.value, ignoreCase = true)
        } == true

        val deletedSet = deletedIds.toSet()
        val remainingSelected = current.selectedFileIds - deletedSet

        if (isDeletedStatusIncluded) {
            val updatedItems = current.paging.items.map { item ->
                if (deletedSet.contains(item.id.asHexDashString())) {
                    item.copy(status = StorageFileStatus.DELETED)
                } else {
                    item
                }
            }
            return current.copy(
                actionLoading = false,
                actionError = null,
                selectedFileIds = remainingSelected,
                paging = current.paging.copy(items = updatedItems)
            )
        }

        val filteredItems = current.paging.items.filter { !deletedSet.contains(it.id.asHexDashString()) }
        val removedCount = current.paging.items.size - filteredItems.size

        return current.copy(
            actionLoading = false,
            actionError = null,
            selectedFileIds = remainingSelected,
            paging = current.paging.copy(
                items = filteredItems,
                totalCount = maxOf(0, current.paging.totalCount - removedCount)
            )
        )
    }
}
