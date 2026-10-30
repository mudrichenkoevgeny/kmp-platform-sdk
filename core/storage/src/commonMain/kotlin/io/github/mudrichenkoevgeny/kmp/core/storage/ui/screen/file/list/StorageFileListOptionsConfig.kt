package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceFilterPresentationStyle
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterChoiceOption
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.TextListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.option.ListingOptionsConfig
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.localization.LocalCommonStrings
import io.github.mudrichenkoevgeny.kmp.core.storage.localization.LocalStorageStrings
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.category.StorageCategory
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.listing.StorageFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.listing.StorageSortValues
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.status.StorageFileStatus
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.visibility.StorageFileVisibility

/**
 * Constructs the listing options configuration (sort options and filter definitions) for storage file lists.
 *
 * @param includeOwnerFilter Whether to include the ownerUserId text filter for administrative views.
 * @return Configured [ListingOptionsConfig] instance for sorting and filtering storage files.
 */
@Composable
fun getStorageFileListOptionsConfig(
    includeOwnerFilter: Boolean = false
): ListingOptionsConfig {
    val commonUi = LocalCommonStrings.current.ui
    val storageUi = LocalStorageStrings.current.ui

    return ListingOptionsConfig(
        sortOptions = listOf(
            ListingSortDefinition(id = StorageSortValues.StorageFileSortBy.CREATED_AT.name, title = commonUi.createdAt),
            ListingSortDefinition(id = StorageSortValues.StorageFileSortBy.UPDATED_AT.name, title = commonUi.updatedAt),
            ListingSortDefinition(id = StorageSortValues.StorageFileSortBy.FILE_NAME.name, title = storageUi.fileName),
            ListingSortDefinition(id = StorageSortValues.StorageFileSortBy.FILE_SIZE_BYTES.name, title = storageUi.fileSize)
        ),
        filters = buildList {
            if (includeOwnerFilter) {
                add(
                    TextListingFilterDefinition(
                        id = StorageFilterValues.StorageFileFilterValues.OWNER_USER_ID,
                        title = storageUi.ownerUserId,
                        placeholder = commonUi.searchPlaceholder
                    )
                )
            }

            add(
                TextListingFilterDefinition(
                    id = StorageFilterValues.StorageFileFilterValues.FILE_NAME,
                    title = storageUi.fileName,
                    placeholder = commonUi.searchPlaceholder
                )
            )

            add(
                TextListingFilterDefinition(
                    id = StorageFilterValues.StorageFileFilterValues.MIME_TYPE,
                    title = storageUi.mimeType,
                    placeholder = commonUi.searchPlaceholder
                )
            )

            add(
                ChoiceListingFilterDefinition(
                    id = StorageFilterValues.StorageFileFilterValues.CATEGORY,
                    title = storageUi.category,
                    options = listOf(
                        ListingFilterChoiceOption(id = StorageCategory.IMAGE.name, title = storageUi.categoryImage),
                        ListingFilterChoiceOption(id = StorageCategory.VIDEO.name, title = storageUi.categoryVideo),
                        ListingFilterChoiceOption(id = StorageCategory.AUDIO.name, title = storageUi.categoryAudio),
                        ListingFilterChoiceOption(id = StorageCategory.DOCUMENT.name, title = storageUi.categoryDocument),
                        ListingFilterChoiceOption(id = StorageCategory.OTHER.name, title = storageUi.categoryOther)
                    ),
                    isMultiple = false,
                    presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
                )
            )

            add(
                ChoiceListingFilterDefinition(
                    id = StorageFilterValues.StorageFileFilterValues.VISIBILITY,
                    title = storageUi.visibility,
                    options = listOf(
                        ListingFilterChoiceOption(id = StorageFileVisibility.PUBLIC.name, title = storageUi.visibilityPublic),
                        ListingFilterChoiceOption(id = StorageFileVisibility.PRIVATE.name, title = storageUi.visibilityPrivate)
                    ),
                    isMultiple = false,
                    presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
                )
            )

            add(
                ChoiceListingFilterDefinition(
                    id = StorageFilterValues.StorageFileFilterValues.STATUS,
                    title = commonUi.status,
                    options = listOf(
                        ListingFilterChoiceOption(id = StorageFileStatus.PENDING_UPLOAD.name, title = storageUi.statusPendingUpload),
                        ListingFilterChoiceOption(id = StorageFileStatus.READY.name, title = storageUi.statusReady),
                        ListingFilterChoiceOption(id = StorageFileStatus.DELETED.name, title = storageUi.statusDeleted),
                        ListingFilterChoiceOption(id = StorageFileStatus.PROCESSING.name, title = storageUi.statusProcessing)
                    ),
                    isMultiple = true,
                    presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
                )
            )
        }
    )
}
