package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.BooleanListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceFilterPresentationStyle
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterChoiceOption
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.TextListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.option.ListingOptionsConfig
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.localization.LocalCommonStrings
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.core.common.localization.UiStrings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSessionFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSessionSortBy
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole

/**
 * Creates a [ListingOptionsConfig] for user session listing screens.
 *
 * @param includeUserFilter Whether to include the user ID filter field.
 * @return Configured [ListingOptionsConfig] for filtering and sorting user sessions.
 */
@Composable
public fun getSessionListingOptionsConfig(includeUserFilter: Boolean = true): ListingOptionsConfig {
    val commonUi = LocalCommonStrings.current.ui
    return ListingOptionsConfig(
        sortOptions = createSessionListingSortOptions(commonUi),
        filters = createSessionListingFilters(includeUserFilter, commonUi)
    )
}

private fun createSessionListingSortOptions(commonUi: UiStrings): List<ListingSortDefinition> = listOf(
    ListingSortDefinition(
        id = UserSessionSortBy.LAST_ACCESSED_AT.value,
        title = commonUi.lastAccessedAt
    ),
    ListingSortDefinition(
        id = UserSessionSortBy.LAST_REAUTHENTICATED_AT.value,
        title = commonUi.lastReauthenticatedAt
    ),
    ListingSortDefinition(
        id = UserSessionSortBy.EXPIRES_AT.value,
        title = commonUi.expiresAt
    ),
    ListingSortDefinition(
        id = UserSessionSortBy.CREATED_AT.value,
        title = commonUi.createdAt
    ),
    ListingSortDefinition(
        id = UserSessionSortBy.UPDATED_AT.value,
        title = commonUi.updatedAt
    )
)

private fun createUserRoleOptions(commonUi: UiStrings): List<ListingFilterChoiceOption> = listOf(
    ListingFilterChoiceOption(UserRole.STAFF.value, commonUi.staff),
    ListingFilterChoiceOption(UserRole.ADMIN.value, commonUi.admin)
)

private fun createAuthProviderOptions(commonUi: UiStrings): List<ListingFilterChoiceOption> = listOf(
    ListingFilterChoiceOption(UserAuthProvider.EMAIL.value, commonUi.email),
    ListingFilterChoiceOption(UserAuthProvider.PHONE.value, commonUi.phone),
    ListingFilterChoiceOption(UserAuthProvider.GOOGLE.value, commonUi.google),
    ListingFilterChoiceOption(UserAuthProvider.APPLE.value, commonUi.apple)
)

private fun createClientTypeOptions(commonUi: UiStrings): List<ListingFilterChoiceOption> = listOf(
    ListingFilterChoiceOption(ClientType.ANDROID.value, commonUi.android),
    ListingFilterChoiceOption(ClientType.IOS.value, commonUi.ios),
    ListingFilterChoiceOption(ClientType.WEB.value, commonUi.web),
    ListingFilterChoiceOption(ClientType.DESKTOP.value, commonUi.desktop)
)

private fun createSessionListingFilters(
    includeUserFilter: Boolean,
    commonUi: UiStrings
): List<ListingFilterDefinition> {
    val filters = mutableListOf<ListingFilterDefinition>(
        TextListingFilterDefinition(
            id = UserSessionFilterValues.SESSION_ID,
            title = commonUi.resourceId,
            placeholder = commonUi.searchPlaceholder
        )
    )

    if (includeUserFilter) {
        filters.add(
            TextListingFilterDefinition(
                id = UserSessionFilterValues.USER_ID,
                title = commonUi.userId,
                placeholder = commonUi.searchPlaceholder
            )
        )
    }

    filters.addAll(createSessionRoleAndIdentifierFilters(commonUi))
    filters.addAll(createSessionDeviceAndClientFilters(commonUi))
    filters.addAll(createSessionTimestampAndSearchFilters(commonUi))

    return filters
}

private fun createSessionRoleAndIdentifierFilters(commonUi: UiStrings): List<ListingFilterDefinition> {
    val userRoleOptions = createUserRoleOptions(commonUi)
    val authProviderOptions = createAuthProviderOptions(commonUi)
    return listOf(
        ChoiceListingFilterDefinition(
            id = UserSessionFilterValues.USER_ROLE,
            title = commonUi.role,
            options = userRoleOptions,
            isMultiple = true,
            presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
        ),
        ChoiceListingFilterDefinition(
            id = UserSessionFilterValues.EXCLUDE_USER_ROLE,
            title = "${commonUi.role} (${commonUi.exclude})",
            options = userRoleOptions,
            isMultiple = true,
            presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
        ),
        TextListingFilterDefinition(
            id = UserSessionFilterValues.IDENTIFIER,
            title = commonUi.identifier,
            placeholder = commonUi.searchPlaceholder
        ),
        TextListingFilterDefinition(
            id = UserSessionFilterValues.IDENTIFIER_ID,
            title = commonUi.identifierId,
            placeholder = commonUi.searchPlaceholder
        ),
        TextListingFilterDefinition(
            id = UserSessionFilterValues.IDENTIFIER_DISPLAY_NAME,
            title = commonUi.user,
            placeholder = commonUi.searchPlaceholder
        ),
        ChoiceListingFilterDefinition(
            id = UserSessionFilterValues.IDENTIFIER_AUTH_PROVIDER,
            title = commonUi.authProvider,
            options = authProviderOptions,
            isMultiple = true,
            presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
        ),
        ChoiceListingFilterDefinition(
            id = UserSessionFilterValues.EXCLUDE_IDENTIFIER_AUTH_PROVIDER,
            title = "${commonUi.authProvider} (${commonUi.exclude})",
            options = authProviderOptions,
            isMultiple = true,
            presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
        )
    )
}

private fun createSessionDeviceAndClientFilters(commonUi: UiStrings): List<ListingFilterDefinition> {
    val clientTypeOptions = createClientTypeOptions(commonUi)
    return listOf(
        TextListingFilterDefinition(
            id = UserSessionFilterValues.DEVICE_ID,
            title = commonUi.deviceId,
            placeholder = commonUi.searchPlaceholder
        ),
        BooleanListingFilterDefinition(
            id = UserSessionFilterValues.IS_DEVICE_ID_SET,
            title = "${commonUi.deviceId} (${commonUi.isSet})"
        ),
        TextListingFilterDefinition(
            id = UserSessionFilterValues.DEVICE_NAME,
            title = commonUi.deviceName,
            placeholder = commonUi.searchPlaceholder
        ),
        BooleanListingFilterDefinition(
            id = UserSessionFilterValues.IS_DEVICE_NAME_SET,
            title = "${commonUi.deviceName} (${commonUi.isSet})"
        ),
        ChoiceListingFilterDefinition(
            id = UserSessionFilterValues.CLIENT_TYPE,
            title = commonUi.clientType,
            options = clientTypeOptions,
            isMultiple = true,
            presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
        ),
        ChoiceListingFilterDefinition(
            id = UserSessionFilterValues.EXCLUDE_CLIENT_TYPE,
            title = "${commonUi.clientType} (${commonUi.exclude})",
            options = clientTypeOptions,
            isMultiple = true,
            presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
        ),
        BooleanListingFilterDefinition(
            id = UserSessionFilterValues.IS_CLIENT_TYPE_SET,
            title = "${commonUi.clientType} (${commonUi.isSet})"
        ),
        TextListingFilterDefinition(
            id = UserSessionFilterValues.LANGUAGE,
            title = commonUi.language,
            placeholder = commonUi.searchPlaceholder
        ),
        BooleanListingFilterDefinition(
            id = UserSessionFilterValues.IS_LANGUAGE_SET,
            title = "${commonUi.language} (${commonUi.isSet})"
        ),
        TextListingFilterDefinition(
            id = UserSessionFilterValues.APP_VERSION,
            title = commonUi.appVersion,
            placeholder = commonUi.searchPlaceholder
        ),
        BooleanListingFilterDefinition(
            id = UserSessionFilterValues.IS_APP_VERSION_SET,
            title = "${commonUi.appVersion} (${commonUi.isSet})"
        ),
        TextListingFilterDefinition(
            id = UserSessionFilterValues.OPERATION_SYSTEM_VERSION,
            title = commonUi.osVersion,
            placeholder = commonUi.searchPlaceholder
        ),
        BooleanListingFilterDefinition(
            id = UserSessionFilterValues.IS_OPERATION_SYSTEM_VERSION_SET,
            title = "${commonUi.osVersion} (${commonUi.isSet})"
        ),
        TextListingFilterDefinition(
            id = UserSessionFilterValues.USER_AGENT,
            title = commonUi.userAgent,
            placeholder = commonUi.searchPlaceholder
        ),
        BooleanListingFilterDefinition(
            id = UserSessionFilterValues.IS_USER_AGENT_SET,
            title = "${commonUi.userAgent} (${commonUi.isSet})"
        ),
        TextListingFilterDefinition(
            id = UserSessionFilterValues.IP_ADDRESS,
            title = commonUi.ipAddress,
            placeholder = commonUi.searchPlaceholder
        ),
        BooleanListingFilterDefinition(
            id = UserSessionFilterValues.IS_IP_ADDRESS_SET,
            title = "${commonUi.ipAddress} (${commonUi.isSet})"
        )
    )
}

private fun createSessionTimestampAndSearchFilters(commonUi: UiStrings): List<ListingFilterDefinition> = listOf(
    TextListingFilterDefinition(
        id = UserSessionFilterValues.EXPIRES_AT_FROM,
        title = "${commonUi.expiresAt} (${commonUi.from})",
        placeholder = commonUi.searchPlaceholder
    ),
    TextListingFilterDefinition(
        id = UserSessionFilterValues.EXPIRES_AT_TO,
        title = "${commonUi.expiresAt} (${commonUi.to})",
        placeholder = commonUi.searchPlaceholder
    ),
    TextListingFilterDefinition(
        id = UserSessionFilterValues.LAST_ACCESSED_AT_FROM,
        title = "${commonUi.lastAccessedAt} (${commonUi.from})",
        placeholder = commonUi.searchPlaceholder
    ),
    TextListingFilterDefinition(
        id = UserSessionFilterValues.LAST_ACCESSED_AT_TO,
        title = "${commonUi.lastAccessedAt} (${commonUi.to})",
        placeholder = commonUi.searchPlaceholder
    ),
    TextListingFilterDefinition(
        id = UserSessionFilterValues.LAST_REAUTHENTICATED_AT_FROM,
        title = "${commonUi.lastReauthenticatedAt} (${commonUi.from})",
        placeholder = commonUi.searchPlaceholder
    ),
    TextListingFilterDefinition(
        id = UserSessionFilterValues.LAST_REAUTHENTICATED_AT_TO,
        title = "${commonUi.lastReauthenticatedAt} (${commonUi.to})",
        placeholder = commonUi.searchPlaceholder
    ),
    TextListingFilterDefinition(
        id = UserSessionFilterValues.CREATED_AT_FROM,
        title = "${commonUi.createdAt} (${commonUi.from})",
        placeholder = commonUi.searchPlaceholder
    ),
    TextListingFilterDefinition(
        id = UserSessionFilterValues.CREATED_AT_TO,
        title = "${commonUi.createdAt} (${commonUi.to})",
        placeholder = commonUi.searchPlaceholder
    ),
    TextListingFilterDefinition(
        id = UserSessionFilterValues.UPDATED_AT_FROM,
        title = "${commonUi.updatedAt} (${commonUi.from})",
        placeholder = commonUi.searchPlaceholder
    ),
    TextListingFilterDefinition(
        id = UserSessionFilterValues.UPDATED_AT_TO,
        title = "${commonUi.updatedAt} (${commonUi.to})",
        placeholder = commonUi.searchPlaceholder
    ),
    BooleanListingFilterDefinition(
        id = UserSessionFilterValues.IS_UPDATED_AT_SET,
        title = "${commonUi.updatedAt} (${commonUi.isSet})"
    ),
    TextListingFilterDefinition(
        id = UserSessionFilterValues.SEARCH_QUERY,
        title = commonUi.searchPlaceholder,
        placeholder = commonUi.searchPlaceholder
    )
)
