package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.datasource

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.TextListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.category.StorageCategory
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.listing.StorageFilterValues.StorageFileFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.listing.StorageSortValues.StorageFileSortBy
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.status.StorageFileStatus
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.visibility.StorageFileVisibility

/**
 * Query parameters passed from UI to storage file list data sources.
 *
 * @property pageNumber Page number to fetch (1-based).
 * @property sortState Active listing sort state or null for default sorting.
 * @property filterStates Map of filter ID to active [ListingFilterState].
 * @property ownerUserId Optional owner user ID filter for scope-constrained queries.
 */
data class StorageFileListQuery(
    val pageNumber: Int,
    val sortState: ListingSortState? = null,
    val filterStates: Map<String, ListingFilterState> = emptyMap(),
    val ownerUserId: String? = null
) {

    /** Parsed category filter selection or null. */
    val categoryChoice: StorageCategory?
        get() = (filterStates[StorageFileFilterValues.CATEGORY] as? ChoiceListingFilterState)
            ?.selectedIds?.firstOrNull()
            ?.let { raw -> StorageCategory.entries.firstOrNull { it.name.equals(raw, true) || it.value.equals(raw, true) } }

    /** Parsed visibility filter selection or null. */
    val visibilityChoice: StorageFileVisibility?
        get() = (filterStates[StorageFileFilterValues.VISIBILITY] as? ChoiceListingFilterState)
            ?.selectedIds?.firstOrNull()
            ?.let { raw -> StorageFileVisibility.entries.firstOrNull { it.name.equals(raw, true) || it.value.equals(raw, true) } }

    /** Parsed list of status filter selections. */
    val statusChoices: List<StorageFileStatus>
        get() = (filterStates[StorageFileFilterValues.STATUS] as? ChoiceListingFilterState)
            ?.selectedIds
            ?.mapNotNull { raw -> StorageFileStatus.entries.firstOrNull { it.name.equals(raw, true) || it.value.equals(raw, true) } }
            .orEmpty()

    /** Parsed text value for MIME type filter or null if blank. */
    val mimeTypeText: String?
        get() = (filterStates[StorageFileFilterValues.MIME_TYPE] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed text value for file name search filter or null if blank. */
    val fileNameText: String?
        get() = (filterStates[StorageFileFilterValues.FILE_NAME] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed sort field option or default. */
    val sortBy: StorageFileSortBy
        get() = sortState?.optionId?.let { id ->
            StorageFileSortBy.entries.firstOrNull { it.name.equals(id, true) || it.value.equals(id, true) }
        } ?: StorageFileSortBy.DEFAULT

    /** Parsed sort order or default. */
    val sortOrder: SortOrder
        get() = sortState?.let { if (it.isAscending) SortOrder.ASC else SortOrder.DESC } ?: SortOrder.DEFAULT
}
