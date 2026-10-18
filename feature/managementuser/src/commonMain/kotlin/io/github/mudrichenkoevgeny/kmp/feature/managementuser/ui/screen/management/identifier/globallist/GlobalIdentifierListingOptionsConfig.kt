package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.globallist

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceFilterPresentationStyle
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterChoiceOption
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.TextListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.option.ListingOptionsConfig
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.localization.LocalCommonStrings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues

@Composable
fun getGlobalIdentifierListingOptionsConfig(): ListingOptionsConfig {
    val commonUi = LocalCommonStrings.current.ui
    return ListingOptionsConfig(
        sortOptions = listOf(
            ListingSortDefinition(
                id = UserSortValues.UserIdentifierSortBy.CREATED_AT.name,
                title = commonUi.createdAt
            ),
            ListingSortDefinition(
                id = UserSortValues.UserIdentifierSortBy.UPDATED_AT.name,
                title = commonUi.updatedAt
            )
        ),
        filters = listOf(
            ChoiceListingFilterDefinition(
                id = UserFilterValues.UserIdentifierFilterValues.USER_AUTH_PROVIDER,
                title = commonUi.authProvider,
                options = listOf(
                    ListingFilterChoiceOption(UserAuthProvider.EMAIL.value, commonUi.email),
                    ListingFilterChoiceOption(UserAuthProvider.PHONE.value, commonUi.phone),
                    ListingFilterChoiceOption(UserAuthProvider.GOOGLE.value, commonUi.google),
                    ListingFilterChoiceOption(UserAuthProvider.APPLE.value, commonUi.apple)
                ),
                isMultiple = true,
                presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
            ),
            TextListingFilterDefinition(
                id = UserFilterValues.UserIdentifierFilterValues.USER_ID,
                title = commonUi.userId,
                placeholder = commonUi.searchPlaceholder
            ),
            TextListingFilterDefinition(
                id = UserFilterValues.UserIdentifierFilterValues.IDENTIFIER,
                title = commonUi.identifier,
                placeholder = commonUi.searchPlaceholder
            )
        )
    )
}
