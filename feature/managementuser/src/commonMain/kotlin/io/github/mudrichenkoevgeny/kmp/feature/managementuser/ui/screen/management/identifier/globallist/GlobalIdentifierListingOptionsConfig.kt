package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.globallist

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceFilterPresentationStyle
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterChoiceOption
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.TextListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.option.ListingOptionsConfig
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.ui_common_auth_provider
import io.github.mudrichenkoevgeny.kmp.core.common.ui_common_created_at
import io.github.mudrichenkoevgeny.kmp.core.common.ui_common_email
import io.github.mudrichenkoevgeny.kmp.core.common.ui_common_google
import io.github.mudrichenkoevgeny.kmp.core.common.ui_common_identifier
import io.github.mudrichenkoevgeny.kmp.core.common.ui_common_phone
import io.github.mudrichenkoevgeny.kmp.core.common.ui_common_search_placeholder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserFilterValues
import org.jetbrains.compose.resources.stringResource
import io.github.mudrichenkoevgeny.kmp.core.common.Res as CommonRes

@Composable
fun getGlobalIdentifierListingOptionsConfig(): ListingOptionsConfig = ListingOptionsConfig(
    sortOptions = listOf(
        ListingSortDefinition(
            id = "created_at",
            title = stringResource(CommonRes.string.ui_common_created_at)
        )
    ),
    filters = listOf(
        ChoiceListingFilterDefinition(
            id = UserFilterValues.UserIdentifierFilterValues.USER_AUTH_PROVIDER,
            title = stringResource(CommonRes.string.ui_common_auth_provider),
            options = listOf(
                ListingFilterChoiceOption(UserAuthProvider.EMAIL.serialName, stringResource(CommonRes.string.ui_common_email)),
                ListingFilterChoiceOption(UserAuthProvider.PHONE.serialName, stringResource(CommonRes.string.ui_common_phone)),
                ListingFilterChoiceOption(UserAuthProvider.GOOGLE.serialName, stringResource(CommonRes.string.ui_common_google))
            ),
            isMultiple = true,
            presentationStyle = ChoiceFilterPresentationStyle.CHIPS
        ),
        TextListingFilterDefinition(
            id = UserFilterValues.UserIdentifierFilterValues.IDENTIFIER,
            title = stringResource(CommonRes.string.ui_common_identifier),
            placeholder = stringResource(CommonRes.string.ui_common_search_placeholder)
        )
    )
)
