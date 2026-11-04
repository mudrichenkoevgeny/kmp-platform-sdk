package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list

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
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserIdentifierFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserIdentifierSortBy

/**
 * Creates a [ListingOptionsConfig] for user identifier listing screens.
 *
 * @param includeUserFilter Whether to include the user ID filter field.
 * @return Configured [ListingOptionsConfig] for filtering and sorting user identifiers.
 */
@Composable
fun getIdentifierListingOptionsConfig(includeUserFilter: Boolean = true): ListingOptionsConfig {
    val commonUi = LocalCommonStrings.current.ui

    val authProviderOptions = listOf(
        ListingFilterChoiceOption(UserAuthProvider.EMAIL.value, commonUi.email),
        ListingFilterChoiceOption(UserAuthProvider.PHONE.value, commonUi.phone),
        ListingFilterChoiceOption(UserAuthProvider.GOOGLE.value, commonUi.google),
        ListingFilterChoiceOption(UserAuthProvider.APPLE.value, commonUi.apple)
    )

    val filters = mutableListOf<ListingFilterDefinition>(
        TextListingFilterDefinition(
            id = UserIdentifierFilterValues.IDENTIFIER_ID,
            title = commonUi.identifierId,
            placeholder = commonUi.searchPlaceholder
        )
    )

    if (includeUserFilter) {
        filters.add(
            TextListingFilterDefinition(
                id = UserIdentifierFilterValues.USER_ID,
                title = commonUi.userId,
                placeholder = commonUi.searchPlaceholder
            )
        )
    }

    filters.addAll(
        listOf(
            ChoiceListingFilterDefinition(
                id = UserIdentifierFilterValues.USER_AUTH_PROVIDER,
                title = commonUi.authProvider,
                options = authProviderOptions,
                isMultiple = true,
                presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
            ),
            ChoiceListingFilterDefinition(
                id = UserIdentifierFilterValues.EXCLUDE_USER_AUTH_PROVIDER,
                title = "${commonUi.authProvider} (${commonUi.exclude})",
                options = authProviderOptions,
                isMultiple = true,
                presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
            ),
            TextListingFilterDefinition(
                id = UserIdentifierFilterValues.IDENTIFIER,
                title = commonUi.identifier,
                placeholder = commonUi.searchPlaceholder
            ),
            TextListingFilterDefinition(
                id = UserIdentifierFilterValues.DISPLAY_NAME,
                title = commonUi.user,
                placeholder = commonUi.searchPlaceholder
            ),
            TextListingFilterDefinition(
                id = UserIdentifierFilterValues.EXTERNAL_PROVIDER_EMAIL,
                title = commonUi.email,
                placeholder = commonUi.searchPlaceholder
            ),
            BooleanListingFilterDefinition(
                id = UserIdentifierFilterValues.IS_EXTERNAL_PROVIDER_EMAIL_SET,
                title = "${commonUi.email} (${commonUi.isSet})"
            ),
            TextListingFilterDefinition(
                id = UserIdentifierFilterValues.CREATED_AT_FROM,
                title = "${commonUi.createdAt} (${commonUi.from})",
                placeholder = commonUi.searchPlaceholder
            ),
            TextListingFilterDefinition(
                id = UserIdentifierFilterValues.CREATED_AT_TO,
                title = "${commonUi.createdAt} (${commonUi.to})",
                placeholder = commonUi.searchPlaceholder
            ),
            TextListingFilterDefinition(
                id = UserIdentifierFilterValues.UPDATED_AT_FROM,
                title = "${commonUi.updatedAt} (${commonUi.from})",
                placeholder = commonUi.searchPlaceholder
            ),
            TextListingFilterDefinition(
                id = UserIdentifierFilterValues.UPDATED_AT_TO,
                title = "${commonUi.updatedAt} (${commonUi.to})",
                placeholder = commonUi.searchPlaceholder
            ),
            BooleanListingFilterDefinition(
                id = UserIdentifierFilterValues.IS_UPDATED_AT_SET,
                title = "${commonUi.updatedAt} (${commonUi.isSet})"
            ),
            TextListingFilterDefinition(
                id = UserIdentifierFilterValues.SEARCH_QUERY,
                title = commonUi.searchPlaceholder,
                placeholder = commonUi.searchPlaceholder
            )
        )
    )

    return ListingOptionsConfig(
        sortOptions = listOf(
            ListingSortDefinition(
                id = UserIdentifierSortBy.CREATED_AT.name,
                title = commonUi.createdAt
            ),
            ListingSortDefinition(
                id = UserIdentifierSortBy.UPDATED_AT.name,
                title = commonUi.updatedAt
            )
        ),
        filters = filters
    )
}
