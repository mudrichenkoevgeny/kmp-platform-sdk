package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.BooleanListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceFilterPresentationStyle
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterChoiceOption
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
fun getStorageFileListOptionsConfig(
    includeOwnerFilter: Boolean = false
): ListingOptionsConfig {
    val commonUi = LocalCommonStrings.current.ui
    val storageUi = LocalStorageStrings.current.ui

    val categoryOptions = listOf(
        ListingFilterChoiceOption(id = StorageCategory.IMAGE.name, title = storageUi.categoryImage),
        ListingFilterChoiceOption(id = StorageCategory.VIDEO.name, title = storageUi.categoryVideo),
        ListingFilterChoiceOption(id = StorageCategory.AUDIO.name, title = storageUi.categoryAudio),
        ListingFilterChoiceOption(id = StorageCategory.DOCUMENT.name, title = storageUi.categoryDocument),
        ListingFilterChoiceOption(id = StorageCategory.OTHER.name, title = storageUi.categoryOther)
    )

    val visibilityOptions = listOf(
        ListingFilterChoiceOption(id = StorageFileVisibility.PUBLIC.name, title = storageUi.visibilityPublic),
        ListingFilterChoiceOption(id = StorageFileVisibility.PRIVATE.name, title = storageUi.visibilityPrivate)
    )

    val statusOptions = listOf(
        ListingFilterChoiceOption(id = StorageFileStatus.PENDING_UPLOAD.name, title = storageUi.statusPendingUpload),
        ListingFilterChoiceOption(id = StorageFileStatus.READY.name, title = storageUi.statusReady),
        ListingFilterChoiceOption(id = StorageFileStatus.DELETED.name, title = storageUi.statusDeleted),
        ListingFilterChoiceOption(id = StorageFileStatus.PROCESSING.name, title = storageUi.statusProcessing)
    )

    return ListingOptionsConfig(
        sortOptions = listOf(
            ListingSortDefinition(id = StorageFileSortBy.CREATED_AT.name, title = commonUi.createdAt),
            ListingSortDefinition(id = StorageFileSortBy.UPDATED_AT.name, title = commonUi.updatedAt),
            ListingSortDefinition(id = StorageFileSortBy.FILE_NAME.name, title = storageUi.fileName),
            ListingSortDefinition(id = StorageFileSortBy.FILE_SIZE_BYTES.name, title = storageUi.fileSize)
        ),
        filters = buildList {
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

            add(
                TextListingFilterDefinition(
                    id = StorageFileFilterValues.FILE_NAME,
                    title = storageUi.fileName,
                    placeholder = commonUi.searchPlaceholder
                )
            )

            add(
                NumberListingFilterDefinition(
                    id = StorageFileFilterValues.FILE_SIZE_BYTES_FROM,
                    title = "${storageUi.fileSize} (${commonUi.from})",
                    placeholder = "0"
                )
            )

            add(
                NumberListingFilterDefinition(
                    id = StorageFileFilterValues.FILE_SIZE_BYTES_TO,
                    title = "${storageUi.fileSize} (${commonUi.to})",
                    placeholder = "100000000"
                )
            )

            add(
                TextListingFilterDefinition(
                    id = StorageFileFilterValues.MIME_TYPE,
                    title = storageUi.mimeType,
                    placeholder = commonUi.searchPlaceholder
                )
            )

            add(
                ChoiceListingFilterDefinition(
                    id = StorageFileFilterValues.CATEGORY,
                    title = storageUi.category,
                    options = categoryOptions,
                    isMultiple = false,
                    presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
                )
            )

            add(
                ChoiceListingFilterDefinition(
                    id = StorageFileFilterValues.EXCLUDE_CATEGORY,
                    title = "${storageUi.category} (${commonUi.exclude})",
                    options = categoryOptions,
                    isMultiple = true,
                    presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
                )
            )

            add(
                ChoiceListingFilterDefinition(
                    id = StorageFileFilterValues.VISIBILITY,
                    title = storageUi.visibility,
                    options = visibilityOptions,
                    isMultiple = false,
                    presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
                )
            )

            add(
                ChoiceListingFilterDefinition(
                    id = StorageFileFilterValues.EXCLUDE_VISIBILITY,
                    title = "${storageUi.visibility} (${commonUi.exclude})",
                    options = visibilityOptions,
                    isMultiple = true,
                    presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
                )
            )

            add(
                ChoiceListingFilterDefinition(
                    id = StorageFileFilterValues.STATUS,
                    title = commonUi.status,
                    options = statusOptions,
                    isMultiple = true,
                    presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
                )
            )

            add(
                ChoiceListingFilterDefinition(
                    id = StorageFileFilterValues.EXCLUDE_STATUS,
                    title = "${commonUi.status} (${commonUi.exclude})",
                    options = statusOptions,
                    isMultiple = true,
                    presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
                )
            )

            add(
                TextListingFilterDefinition(
                    id = StorageFileFilterValues.TARGET_SCOPE,
                    title = storageUi.targetScope,
                    placeholder = commonUi.searchPlaceholder
                )
            )

            add(
                BooleanListingFilterDefinition(
                    id = StorageFileFilterValues.IS_TARGET_SCOPE_SET,
                    title = "${storageUi.targetScope} (${commonUi.isSet})"
                )
            )

            add(
                TextListingFilterDefinition(
                    id = StorageFileFilterValues.CREATED_AT_FROM,
                    title = "${commonUi.createdAt} (${commonUi.from})",
                    placeholder = commonUi.searchPlaceholder
                )
            )

            add(
                TextListingFilterDefinition(
                    id = StorageFileFilterValues.CREATED_AT_TO,
                    title = "${commonUi.createdAt} (${commonUi.to})",
                    placeholder = commonUi.searchPlaceholder
                )
            )

            add(
                TextListingFilterDefinition(
                    id = StorageFileFilterValues.UPDATED_AT_FROM,
                    title = "${commonUi.updatedAt} (${commonUi.from})",
                    placeholder = commonUi.searchPlaceholder
                )
            )

            add(
                TextListingFilterDefinition(
                    id = StorageFileFilterValues.UPDATED_AT_TO,
                    title = "${commonUi.updatedAt} (${commonUi.to})",
                    placeholder = commonUi.searchPlaceholder
                )
            )

            add(
                BooleanListingFilterDefinition(
                    id = StorageFileFilterValues.IS_UPDATED_AT_SET,
                    title = "${commonUi.updatedAt} (${commonUi.isSet})"
                )
            )

            add(
                TextListingFilterDefinition(
                    id = StorageFileFilterValues.SEARCH_QUERY,
                    title = commonUi.searchPlaceholder,
                    placeholder = commonUi.searchPlaceholder
                )
            )
        }
    )
}
