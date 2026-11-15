package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.datasource

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.BooleanListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.NumberListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.TextListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.category.StorageCategory
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.listing.StorageFileFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.listing.StorageFileSortBy
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
public data class StorageFileListQuery(
    public val pageNumber: Int,
    public val sortState: ListingSortState? = null,
    public val filterStates: Map<String, ListingFilterState> = emptyMap(),
    public val ownerUserId: String? = null
) {

    /** Parsed file ID filter text or null if blank. */
    public val fileIdText: String?
        get() = (filterStates[StorageFileFilterValues.FILE_ID] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed owner user ID filter text or null if blank. */
    public val ownerUserIdText: String?
        get() = (filterStates[StorageFileFilterValues.OWNER_USER_ID] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed boolean flag indicating whether owner user ID is set. */
    public val isOwnerUserIdSetChoice: Boolean?
        get() = (filterStates[StorageFileFilterValues.IS_OWNER_USER_ID_SET] as? BooleanListingFilterState)?.value

    /** Parsed category filter selection or null. */
    public val categoryChoice: StorageCategory?
        get() = (filterStates[StorageFileFilterValues.CATEGORY] as? ChoiceListingFilterState)
            ?.selectedIds?.firstOrNull()
            ?.let { raw ->
                StorageCategory.entries.firstOrNull {
                    it.name.equals(raw, true) || it.value.equals(raw, true)
                }
            }

    /** Parsed excluded categories filter selections. */
    public val excludeCategoriesChoices: List<StorageCategory>
        get() = (filterStates[StorageFileFilterValues.EXCLUDE_CATEGORY] as? ChoiceListingFilterState)
            ?.selectedIds
            ?.mapNotNull { raw ->
                StorageCategory.entries.firstOrNull {
                    it.name.equals(raw, true) || it.value.equals(raw, true)
                }
            }
            .orEmpty()

    /** Parsed visibility filter selection or null. */
    public val visibilityChoice: StorageFileVisibility?
        get() = (filterStates[StorageFileFilterValues.VISIBILITY] as? ChoiceListingFilterState)
            ?.selectedIds?.firstOrNull()
            ?.let { raw ->
                StorageFileVisibility.entries.firstOrNull {
                    it.name.equals(raw, true) || it.value.equals(raw, true)
                }
            }

    /** Parsed excluded visibilities filter selections. */
    public val excludeVisibilitiesChoices: List<StorageFileVisibility>
        get() = (filterStates[StorageFileFilterValues.EXCLUDE_VISIBILITY] as? ChoiceListingFilterState)
            ?.selectedIds
            ?.mapNotNull { raw ->
                StorageFileVisibility.entries.firstOrNull {
                    it.name.equals(raw, true) || it.value.equals(raw, true)
                }
            }
            .orEmpty()

    /** Parsed list of status filter selections. */
    public val statusChoices: List<StorageFileStatus>
        get() = (filterStates[StorageFileFilterValues.STATUS] as? ChoiceListingFilterState)
            ?.selectedIds
            ?.mapNotNull { raw ->
                StorageFileStatus.entries.firstOrNull {
                    it.name.equals(raw, true) || it.value.equals(raw, true)
                }
            }
            .orEmpty()

    /** Parsed list of excluded status filter selections. */
    public val excludeStatusesChoices: List<StorageFileStatus>
        get() = (filterStates[StorageFileFilterValues.EXCLUDE_STATUS] as? ChoiceListingFilterState)
            ?.selectedIds
            ?.mapNotNull { raw ->
                StorageFileStatus.entries.firstOrNull {
                    it.name.equals(raw, true) || it.value.equals(raw, true)
                }
            }
            .orEmpty()

    /** Parsed text value for MIME type filter or null if blank. */
    public val mimeTypeText: String?
        get() = (filterStates[StorageFileFilterValues.MIME_TYPE] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed text value for file name search filter or null if blank. */
    public val fileNameText: String?
        get() = (filterStates[StorageFileFilterValues.FILE_NAME] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed lower bound for file size in bytes. */
    public val fileSizeBytesFromNumber: Long?
        get() = (filterStates[StorageFileFilterValues.FILE_SIZE_BYTES_FROM] as? NumberListingFilterState)?.value

    /** Parsed upper bound for file size in bytes. */
    public val fileSizeBytesToNumber: Long?
        get() = (filterStates[StorageFileFilterValues.FILE_SIZE_BYTES_TO] as? NumberListingFilterState)?.value

    /** Parsed text value for target scope filter or null if blank. */
    public val targetScopeText: String?
        get() = (filterStates[StorageFileFilterValues.TARGET_SCOPE] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed boolean flag indicating whether target scope is assigned. */
    public val isTargetScopeSetChoice: Boolean?
        get() = (filterStates[StorageFileFilterValues.IS_TARGET_SCOPE_SET] as? BooleanListingFilterState)?.value

    /** Parsed lower bound for creation timestamp. */
    public val createdAtFromText: String?
        get() = (filterStates[StorageFileFilterValues.CREATED_AT_FROM] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed upper bound for creation timestamp. */
    public val createdAtToText: String?
        get() = (filterStates[StorageFileFilterValues.CREATED_AT_TO] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed lower bound for update timestamp. */
    public val updatedAtFromText: String?
        get() = (filterStates[StorageFileFilterValues.UPDATED_AT_FROM] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed upper bound for update timestamp. */
    public val updatedAtToText: String?
        get() = (filterStates[StorageFileFilterValues.UPDATED_AT_TO] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed boolean flag indicating whether record has been updated. */
    public val isUpdatedAtSetChoice: Boolean?
        get() = (filterStates[StorageFileFilterValues.IS_UPDATED_AT_SET] as? BooleanListingFilterState)?.value

    /** Parsed text value for search query filter or null if blank. */
    public val searchQueryText: String?
        get() = (filterStates[StorageFileFilterValues.SEARCH_QUERY] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed sort field option or default. */
    public val sortBy: StorageFileSortBy
        get() = sortState?.optionId?.let { id ->
            StorageFileSortBy.entries.firstOrNull { it.name.equals(id, true) || it.value.equals(id, true) }
        } ?: StorageFileSortBy.DEFAULT

    /** Parsed sort order or default. */
    public val sortOrder: SortOrder
        get() = sortState?.let { if (it.isAscending) SortOrder.ASC else SortOrder.DESC } ?: SortOrder.DEFAULT
}
