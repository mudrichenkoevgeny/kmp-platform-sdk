package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list.datasource

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.BooleanListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.TextListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserIdentifierFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserIdentifierSortBy

/**
 * Query parameters passed from UI to identifier list data sources.
 *
 * @property pageNumber Page number to fetch (1-based).
 * @property sortState Active listing sort state or null for default sorting.
 * @property filterStates Map of filter ID to active [ListingFilterState].
 * @property targetUserId Optional target user ID for scope-constrained queries.
 */
data class IdentifierListQuery(
    val pageNumber: Int,
    val sortState: ListingSortState? = null,
    val filterStates: Map<String, ListingFilterState> = emptyMap(),
    val targetUserId: String? = null
) {

    /** Parsed identifier ID text or null if blank. */
    val identifierIdText: String?
        get() = (filterStates[UserIdentifierFilterValues.IDENTIFIER_ID] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed user ID text or null if blank. */
    val userIdText: String?
        get() = (filterStates[UserIdentifierFilterValues.USER_ID] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed identifier text or null if blank. */
    val identifierText: String?
        get() = (filterStates[UserIdentifierFilterValues.IDENTIFIER] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed auth provider choices. */
    val authProvidersChoices: List<UserAuthProvider>
        get() = (filterStates[UserIdentifierFilterValues.USER_AUTH_PROVIDER] as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { UserAuthProvider.fromValueOrNull(it) }.orEmpty()

    /** Parsed excluded auth provider choices. */
    val excludeAuthProvidersChoices: List<UserAuthProvider>
        get() = (filterStates[UserIdentifierFilterValues.EXCLUDE_USER_AUTH_PROVIDER] as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { UserAuthProvider.fromValueOrNull(it) }.orEmpty()

    /** Parsed display name text or null if blank. */
    val displayNameText: String?
        get() = (filterStates[UserIdentifierFilterValues.DISPLAY_NAME] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed external provider email text or null if blank. */
    val externalProviderEmailText: String?
        get() = (filterStates[UserIdentifierFilterValues.EXTERNAL_PROVIDER_EMAIL] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed boolean flag indicating whether external provider email is set. */
    val isExternalProviderEmailSetChoice: Boolean?
        get() = (filterStates[UserIdentifierFilterValues.IS_EXTERNAL_PROVIDER_EMAIL_SET] as? BooleanListingFilterState)?.value

    /** Parsed creation lower bound timestamp text. */
    val createdAtFromText: String?
        get() = (filterStates[UserIdentifierFilterValues.CREATED_AT_FROM] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed creation upper bound timestamp text. */
    val createdAtToText: String?
        get() = (filterStates[UserIdentifierFilterValues.CREATED_AT_TO] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed update lower bound timestamp text. */
    val updatedAtFromText: String?
        get() = (filterStates[UserIdentifierFilterValues.UPDATED_AT_FROM] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed update upper bound timestamp text. */
    val updatedAtToText: String?
        get() = (filterStates[UserIdentifierFilterValues.UPDATED_AT_TO] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed boolean flag indicating whether record has been updated. */
    val isUpdatedAtSetChoice: Boolean?
        get() = (filterStates[UserIdentifierFilterValues.IS_UPDATED_AT_SET] as? BooleanListingFilterState)?.value

    /** Parsed search query filter text or null if blank. */
    val searchQueryText: String?
        get() = (filterStates[UserIdentifierFilterValues.SEARCH_QUERY] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed sort field option or default. */
    val sortBy: UserIdentifierSortBy
        get() = sortState?.optionId?.let { id -> UserIdentifierSortBy.fromValueOrNull(id) } ?: UserIdentifierSortBy.DEFAULT

    /** Parsed sort order or default. */
    val sortOrder: SortOrder
        get() = sortState?.let { if (it.isAscending) SortOrder.ASC else SortOrder.DESC } ?: SortOrder.DEFAULT
}
