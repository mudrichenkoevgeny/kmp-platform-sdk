package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.datasource

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.BooleanListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.TextListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSessionFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSessionSortBy
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole

/**
 * Query parameters passed from UI to session list data sources.
 *
 * @property pageNumber Page number to fetch (1-based).
 * @property sortState Active listing sort state or null for default sorting.
 * @property filterStates Map of filter ID to active [ListingFilterState].
 * @property targetUserId Optional target user ID for scope-constrained queries.
 */
public data class SessionListQuery(
    val pageNumber: Int,
    val sortState: ListingSortState? = null,
    val filterStates: Map<String, ListingFilterState> = emptyMap(),
    val targetUserId: String? = null
) {

    /** Parsed session ID filter text or null if blank. */
    public val sessionIdText: String?
        get() = (filterStates[UserSessionFilterValues.SESSION_ID] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed user ID filter text or null if blank. */
    public val userIdText: String?
        get() = (filterStates[UserSessionFilterValues.USER_ID] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed user roles filter selections. */
    public val userRolesChoices: List<UserRole>
        get() = (filterStates[UserSessionFilterValues.USER_ROLE] as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { UserRole.fromValueOrNull(it) }.orEmpty()

    /** Parsed excluded user roles filter selections. */
    public val excludeUserRolesChoices: List<UserRole>
        get() = (filterStates[UserSessionFilterValues.EXCLUDE_USER_ROLE] as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { UserRole.fromValueOrNull(it) }.orEmpty()

    /** Parsed identifier text or null if blank. */
    public val identifierText: String?
        get() = (filterStates[UserSessionFilterValues.IDENTIFIER] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed identifier ID text or null if blank. */
    public val identifierIdText: String?
        get() = (filterStates[UserSessionFilterValues.IDENTIFIER_ID] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed identifier display name or null if blank. */
    public val identifierDisplayNameText: String?
        get() = (filterStates[UserSessionFilterValues.IDENTIFIER_DISPLAY_NAME] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed auth provider choices. */
    public val authProvidersChoices: List<UserAuthProvider>
        get() = (filterStates[UserSessionFilterValues.IDENTIFIER_AUTH_PROVIDER] as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { UserAuthProvider.fromValueOrNull(it) }.orEmpty()

    /** Parsed excluded auth provider choices. */
    public val excludeAuthProvidersChoices: List<UserAuthProvider>
        get() = (filterStates[UserSessionFilterValues.EXCLUDE_IDENTIFIER_AUTH_PROVIDER] as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { UserAuthProvider.fromValueOrNull(it) }.orEmpty()

    /** Parsed device ID text or null if blank. */
    public val deviceIdText: String?
        get() = (filterStates[UserSessionFilterValues.DEVICE_ID] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed boolean flag indicating whether device ID is set. */
    public val isDeviceIdSetChoice: Boolean?
        get() = (filterStates[UserSessionFilterValues.IS_DEVICE_ID_SET] as? BooleanListingFilterState)?.value

    /** Parsed device name text or null if blank. */
    public val deviceNameText: String?
        get() = (filterStates[UserSessionFilterValues.DEVICE_NAME] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed boolean flag indicating whether device name is set. */
    public val isDeviceNameSetChoice: Boolean?
        get() = (filterStates[UserSessionFilterValues.IS_DEVICE_NAME_SET] as? BooleanListingFilterState)?.value

    /** Parsed client types filter selections. */
    public val clientTypesChoices: List<ClientType>
        get() = (filterStates[UserSessionFilterValues.CLIENT_TYPE] as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { ClientType.fromValueOrNull(it) }.orEmpty()

    /** Parsed excluded client types filter selections. */
    public val excludeClientTypesChoices: List<ClientType>
        get() = (filterStates[UserSessionFilterValues.EXCLUDE_CLIENT_TYPE] as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { ClientType.fromValueOrNull(it) }.orEmpty()

    /** Parsed boolean flag indicating whether client type is set. */
    public val isClientTypeSetChoice: Boolean?
        get() = (filterStates[UserSessionFilterValues.IS_CLIENT_TYPE_SET] as? BooleanListingFilterState)?.value

    /** Parsed user agent text or null if blank. */
    public val userAgentText: String?
        get() = (filterStates[UserSessionFilterValues.USER_AGENT] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed boolean flag indicating whether user agent is set. */
    public val isUserAgentSetChoice: Boolean?
        get() = (filterStates[UserSessionFilterValues.IS_USER_AGENT_SET] as? BooleanListingFilterState)?.value

    /** Parsed IP address text or null if blank. */
    public val ipAddressText: String?
        get() = (filterStates[UserSessionFilterValues.IP_ADDRESS] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed boolean flag indicating whether IP address is set. */
    public val isIpAddressSetChoice: Boolean?
        get() = (filterStates[UserSessionFilterValues.IS_IP_ADDRESS_SET] as? BooleanListingFilterState)?.value

    /** Parsed language text or null if blank. */
    public val languageText: String?
        get() = (filterStates[UserSessionFilterValues.LANGUAGE] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed boolean flag indicating whether language is set. */
    public val isLanguageSetChoice: Boolean?
        get() = (filterStates[UserSessionFilterValues.IS_LANGUAGE_SET] as? BooleanListingFilterState)?.value

    /** Parsed app version text or null if blank. */
    public val appVersionText: String?
        get() = (filterStates[UserSessionFilterValues.APP_VERSION] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed boolean flag indicating whether app version is set. */
    public val isAppVersionSetChoice: Boolean?
        get() = (filterStates[UserSessionFilterValues.IS_APP_VERSION_SET] as? BooleanListingFilterState)?.value

    /** Parsed OS version text or null if blank. */
    public val osVersionText: String?
        get() = (filterStates[UserSessionFilterValues.OPERATION_SYSTEM_VERSION] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed boolean flag indicating whether OS version is set. */
    public val isOsVersionSetChoice: Boolean?
        get() = (
            filterStates[UserSessionFilterValues.IS_OPERATION_SYSTEM_VERSION_SET]
                as? BooleanListingFilterState
            )?.value

    /** Parsed expiration lower bound timestamp text. */
    public val expiresAtFromText: String?
        get() = (filterStates[UserSessionFilterValues.EXPIRES_AT_FROM] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed expiration upper bound timestamp text. */
    public val expiresAtToText: String?
        get() = (filterStates[UserSessionFilterValues.EXPIRES_AT_TO] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed last accessed lower bound timestamp text. */
    public val lastAccessedAtFromText: String?
        get() = (filterStates[UserSessionFilterValues.LAST_ACCESSED_AT_FROM] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed last accessed upper bound timestamp text. */
    public val lastAccessedAtToText: String?
        get() = (filterStates[UserSessionFilterValues.LAST_ACCESSED_AT_TO] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed last reauthenticated lower bound timestamp text. */
    public val lastReauthenticatedAtFromText: String?
        get() = (filterStates[UserSessionFilterValues.LAST_REAUTHENTICATED_AT_FROM] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed last reauthenticated upper bound timestamp text. */
    public val lastReauthenticatedAtToText: String?
        get() = (filterStates[UserSessionFilterValues.LAST_REAUTHENTICATED_AT_TO] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed creation lower bound timestamp text. */
    public val createdAtFromText: String?
        get() = (filterStates[UserSessionFilterValues.CREATED_AT_FROM] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed creation upper bound timestamp text. */
    public val createdAtToText: String?
        get() = (filterStates[UserSessionFilterValues.CREATED_AT_TO] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed update lower bound timestamp text. */
    public val updatedAtFromText: String?
        get() = (filterStates[UserSessionFilterValues.UPDATED_AT_FROM] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed update upper bound timestamp text. */
    public val updatedAtToText: String?
        get() = (filterStates[UserSessionFilterValues.UPDATED_AT_TO] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed boolean flag indicating whether record has been updated. */
    public val isUpdatedAtSetChoice: Boolean?
        get() = (filterStates[UserSessionFilterValues.IS_UPDATED_AT_SET] as? BooleanListingFilterState)?.value

    /** Parsed search query filter text or null if blank. */
    public val searchQueryText: String?
        get() = (filterStates[UserSessionFilterValues.SEARCH_QUERY] as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }

    /** Parsed sort field option or default. */
    public val sortBy: UserSessionSortBy
        get() = sortState?.optionId?.let { id -> UserSessionSortBy.fromValueOrNull(id) } ?: UserSessionSortBy.DEFAULT

    /** Parsed sort order or default. */
    public val sortOrder: SortOrder
        get() = sortState?.let { if (it.isAscending) SortOrder.ASC else SortOrder.DESC } ?: SortOrder.DEFAULT
}
