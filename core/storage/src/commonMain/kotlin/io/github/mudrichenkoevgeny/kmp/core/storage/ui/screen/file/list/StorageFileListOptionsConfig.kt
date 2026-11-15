package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.BooleanListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceFilterPresentationStyle
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterChoiceOption
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.NumberListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.TextListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.option.ListingOptionsConfig
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.localization.LocalCommonStrings
import io.github.mudrichenkoevgeny.kmp.core.storage.localization.LocalStorageStrings
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.category.StorageCategory
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.listing.StorageFileFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.listing.StorageFileSortBy
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.status.StorageFileStatus
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.visibility.StorageFileVisibility

/**
 * Constructs the listing options configuration (sort options and filter definitions) for storage file lists.
 *
 * @param includeOwnerFilter Whether to include the ownerUserId text filter for administrative views.
 * @return Configured [ListingOptionsConfig] instance for sorting and filtering storage files.
 */
@Composable
public fun getStorageFileListOptionsConfig(
    includeOwnerFilter: Boolean = false
): ListingOptionsConfig = ListingOptionsConfig(
    sortOptions = createStorageFileListSortOptions(),
    filters = createStorageFileListFilters(includeOwnerFilter = includeOwnerFilter)
)

@Composable
private fun createStorageFileListSortOptions(): List<ListingSortDefinition> {
    val commonUi = LocalCommonStrings.current.ui
    val storageUi = LocalStorageStrings.current.ui
    return listOf(
        ListingSortDefinition(id = StorageFileSortBy.CREATED_AT.value, title = commonUi.createdAt),
        ListingSortDefinition(id = StorageFileSortBy.UPDATED_AT.value, title = commonUi.updatedAt),
        ListingSortDefinition(id = StorageFileSortBy.FILE_NAME.value, title = storageUi.fileName),
        ListingSortDefinition(id = StorageFileSortBy.FILE_SIZE_BYTES.value, title = storageUi.fileSize)
    )
}

@Composable
private fun createStorageFileListFilters(
    includeOwnerFilter: Boolean
): List<ListingFilterDefinition> {
    val categoryOptions = createStorageFileListCategoryOptions()
    val visibilityOptions = createStorageFileListVisibilityOptions()
    val statusOptions = createStorageFileListStatusOptions()

    return buildList {
        addAll(createIdentityAndOwnerFilters(includeOwnerFilter = includeOwnerFilter))
        addAll(createFileDetailsAndSizeFilters())
        addAll(createCategoryVisibilityStatusFilters(categoryOptions, visibilityOptions, statusOptions))
        addAll(createScopeAndTimestampFilters())
    }
}

@Composable
private fun createStorageFileListCategoryOptions(): List<ListingFilterChoiceOption> {
    val storageUi = LocalStorageStrings.current.ui
    return listOf(
        ListingFilterChoiceOption(id = StorageCategory.IMAGE.value, title = storageUi.categoryImage),
        ListingFilterChoiceOption(id = StorageCategory.VIDEO.value, title = storageUi.categoryVideo),
        ListingFilterChoiceOption(id = StorageCategory.AUDIO.value, title = storageUi.categoryAudio),
        ListingFilterChoiceOption(id = StorageCategory.DOCUMENT.value, title = storageUi.categoryDocument),
        ListingFilterChoiceOption(id = StorageCategory.OTHER.value, title = storageUi.categoryOther)
    )
}

@Composable
private fun createStorageFileListVisibilityOptions(): List<ListingFilterChoiceOption> {
    val storageUi = LocalStorageStrings.current.ui
    return listOf(
        ListingFilterChoiceOption(id = StorageFileVisibility.PUBLIC.value, title = storageUi.visibilityPublic),
        ListingFilterChoiceOption(id = StorageFileVisibility.PRIVATE.value, title = storageUi.visibilityPrivate)
    )
}

@Composable
private fun createStorageFileListStatusOptions(): List<ListingFilterChoiceOption> {
    val storageUi = LocalStorageStrings.current.ui
    return listOf(
        ListingFilterChoiceOption(id = StorageFileStatus.PENDING_UPLOAD.value, title = storageUi.statusPendingUpload),
        ListingFilterChoiceOption(id = StorageFileStatus.READY.value, title = storageUi.statusReady),
        ListingFilterChoiceOption(id = StorageFileStatus.DELETED.value, title = storageUi.statusDeleted),
        ListingFilterChoiceOption(id = StorageFileStatus.PROCESSING.value, title = storageUi.statusProcessing)
    )
}

@Composable
private fun createIdentityAndOwnerFilters(
    includeOwnerFilter: Boolean
): List<ListingFilterDefinition> {
    val commonUi = LocalCommonStrings.current.ui
    val storageUi = LocalStorageStrings.current.ui

    return buildList {
        add(
            TextListingFilterDefinition(
                id = StorageFileFilterValues.FILE_ID,
                title = commonUi.resourceId,
                placeholder = commonUi.searchPlaceholder
            )
        )

        if (includeOwnerFilter) {
            add(
                TextListingFilterDefinition(
                    id = StorageFileFilterValues.OWNER_USER_ID,
                    title = storageUi.ownerUserId,
                    placeholder = commonUi.searchPlaceholder
                )
            )
            add(
                BooleanListingFilterDefinition(
                    id = StorageFileFilterValues.IS_OWNER_USER_ID_SET,
                    title = "${storageUi.ownerUserId} (${commonUi.isSet})"
                )
            )
        }
    }
}

@Composable
private fun createFileDetailsAndSizeFilters(): List<ListingFilterDefinition> {
    val commonUi = LocalCommonStrings.current.ui
    val storageUi = LocalStorageStrings.current.ui

    return listOf(
        TextListingFilterDefinition(
            id = StorageFileFilterValues.FILE_NAME,
            title = storageUi.fileName,
            placeholder = commonUi.searchPlaceholder
        ),
        NumberListingFilterDefinition(
            id = StorageFileFilterValues.FILE_SIZE_BYTES_FROM,
            title = "${storageUi.fileSize} (${commonUi.from})",
            placeholder = "0"
        ),
        NumberListingFilterDefinition(
            id = StorageFileFilterValues.FILE_SIZE_BYTES_TO,
            title = "${storageUi.fileSize} (${commonUi.to})",
            placeholder = "100000000"
        ),
        TextListingFilterDefinition(
            id = StorageFileFilterValues.MIME_TYPE,
            title = storageUi.mimeType,
            placeholder = commonUi.searchPlaceholder
        )
    )
}

@Composable
private fun createCategoryVisibilityStatusFilters(
    categoryOptions: List<ListingFilterChoiceOption>,
    visibilityOptions: List<ListingFilterChoiceOption>,
    statusOptions: List<ListingFilterChoiceOption>
): List<ListingFilterDefinition> {
    val commonUi = LocalCommonStrings.current.ui
    val storageUi = LocalStorageStrings.current.ui

    return listOf(
        ChoiceListingFilterDefinition(
            id = StorageFileFilterValues.CATEGORY,
            title = storageUi.category,
            options = categoryOptions,
            isMultiple = false,
            presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
        ),
        ChoiceListingFilterDefinition(
            id = StorageFileFilterValues.EXCLUDE_CATEGORY,
            title = "${storageUi.category} (${commonUi.exclude})",
            options = categoryOptions,
            isMultiple = true,
            presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
        ),
        ChoiceListingFilterDefinition(
            id = StorageFileFilterValues.VISIBILITY,
            title = storageUi.visibility,
            options = visibilityOptions,
            isMultiple = false,
            presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
        ),
        ChoiceListingFilterDefinition(
            id = StorageFileFilterValues.EXCLUDE_VISIBILITY,
            title = "${storageUi.visibility} (${commonUi.exclude})",
            options = visibilityOptions,
            isMultiple = true,
            presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
        ),
        ChoiceListingFilterDefinition(
            id = StorageFileFilterValues.STATUS,
            title = commonUi.status,
            options = statusOptions,
            isMultiple = true,
            presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
        ),
        ChoiceListingFilterDefinition(
            id = StorageFileFilterValues.EXCLUDE_STATUS,
            title = "${commonUi.status} (${commonUi.exclude})",
            options = statusOptions,
            isMultiple = true,
            presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
        )
    )
}

@Composable
private fun createScopeAndTimestampFilters(): List<ListingFilterDefinition> {
    val commonUi = LocalCommonStrings.current.ui
    val storageUi = LocalStorageStrings.current.ui

    return listOf(
        TextListingFilterDefinition(
            id = StorageFileFilterValues.TARGET_SCOPE,
            title = storageUi.targetScope,
            placeholder = commonUi.searchPlaceholder
        ),
        BooleanListingFilterDefinition(
            id = StorageFileFilterValues.IS_TARGET_SCOPE_SET,
            title = "${storageUi.targetScope} (${commonUi.isSet})"
        ),
        TextListingFilterDefinition(
            id = StorageFileFilterValues.CREATED_AT_FROM,
            title = "${commonUi.createdAt} (${commonUi.from})",
            placeholder = commonUi.searchPlaceholder
        ),
        TextListingFilterDefinition(
            id = StorageFileFilterValues.CREATED_AT_TO,
            title = "${commonUi.createdAt} (${commonUi.to})",
            placeholder = commonUi.searchPlaceholder
        ),
        TextListingFilterDefinition(
            id = StorageFileFilterValues.UPDATED_AT_FROM,
            title = "${commonUi.updatedAt} (${commonUi.from})",
            placeholder = commonUi.searchPlaceholder
        ),
        TextListingFilterDefinition(
            id = StorageFileFilterValues.UPDATED_AT_TO,
            title = "${commonUi.updatedAt} (${commonUi.to})",
            placeholder = commonUi.searchPlaceholder
        ),
        BooleanListingFilterDefinition(
            id = StorageFileFilterValues.IS_UPDATED_AT_SET,
            title = "${commonUi.updatedAt} (${commonUi.isSet})"
        ),
        TextListingFilterDefinition(
            id = StorageFileFilterValues.SEARCH_QUERY,
            title = commonUi.searchPlaceholder,
            placeholder = commonUi.searchPlaceholder
        )
    )
}
