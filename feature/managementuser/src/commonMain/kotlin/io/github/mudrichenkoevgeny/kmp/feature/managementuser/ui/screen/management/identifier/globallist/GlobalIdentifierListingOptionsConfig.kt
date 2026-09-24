package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.globallist

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.*
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceFilterPresentationStyle
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterChoiceOption
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.TextListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.option.ListingOptionsConfig
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortDefinition
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import org.jetbrains.compose.resources.stringResource
import io.github.mudrichenkoevgeny.kmp.core.common.Res as CommonRes

@Composable
fun getGlobalIdentifierListingOptionsConfig(): ListingOptionsConfig = ListingOptionsConfig(
    sortOptions = listOf(
        ListingSortDefinition(
            id = UserSortValues.UserIdentifierSortBy.CREATED_AT.name,
            title = stringResource(CommonRes.string.ui_common_created_at)
        ),
        ListingSortDefinition(
            id = UserSortValues.UserIdentifierSortBy.UPDATED_AT.name,
            title = stringResource(CommonRes.string.ui_common_updated_at)
        )
    ),
    filters = listOf(
        ChoiceListingFilterDefinition(
            id = UserFilterValues.UserIdentifierFilterValues.USER_AUTH_PROVIDER,
            title = stringResource(CommonRes.string.ui_common_auth_provider),
            options = listOf(
                ListingFilterChoiceOption(UserAuthProvider.EMAIL.serialName, stringResource(CommonRes.string.ui_common_email)),
                ListingFilterChoiceOption(UserAuthProvider.PHONE.serialName, stringResource(CommonRes.string.ui_common_phone)),
                ListingFilterChoiceOption(UserAuthProvider.GOOGLE.serialName, stringResource(CommonRes.string.ui_common_google)),
                ListingFilterChoiceOption(UserAuthProvider.APPLE.serialName, stringResource(CommonRes.string.ui_common_apple))
            ),
            isMultiple = true,
            presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
        ),
        TextListingFilterDefinition(
            id = UserFilterValues.UserIdentifierFilterValues.USER_ID,
            title = stringResource(CommonRes.string.ui_common_user_id),
            placeholder = stringResource(CommonRes.string.ui_common_search_placeholder)
        ),
        TextListingFilterDefinition(
            id = UserFilterValues.UserIdentifierFilterValues.IDENTIFIER,
            title = stringResource(CommonRes.string.ui_common_identifier),
            placeholder = stringResource(CommonRes.string.ui_common_search_placeholder)
        )
    )
)