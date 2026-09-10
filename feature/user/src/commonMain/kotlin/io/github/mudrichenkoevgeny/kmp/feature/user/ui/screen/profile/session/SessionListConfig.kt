package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.Res as CommonRes
import io.github.mudrichenkoevgeny.kmp.core.common.*
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceFilterPresentationStyle
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterChoiceOption
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.TextListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.option.ListingOptionsConfig
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortDefinition
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserFilterValues
import org.jetbrains.compose.resources.stringResource

@Composable
fun getSessionListListingConfig(): ListingOptionsConfig = ListingOptionsConfig(
    sortOptions = listOf(
        ListingSortDefinition(
            id = "created_at",
            title = stringResource(CommonRes.string.ui_common_created_at)
        )
    ),
    filters = listOf(
        ChoiceListingFilterDefinition(
            id = UserFilterValues.UserSessionFilterValues.CLIENT_TYPE,
            title = stringResource(CommonRes.string.ui_common_client_type),
            options = listOf(
                ListingFilterChoiceOption(ClientType.ANDROID.serialName, stringResource(CommonRes.string.ui_common_android)),
                ListingFilterChoiceOption(ClientType.IOS.serialName, stringResource(CommonRes.string.ui_common_ios)),
                ListingFilterChoiceOption(ClientType.WEB.serialName, stringResource(CommonRes.string.ui_common_web)),
                ListingFilterChoiceOption(ClientType.DESKTOP.serialName, stringResource(CommonRes.string.ui_common_desktop))
            ),
            isMultiple = true,
            presentationStyle = ChoiceFilterPresentationStyle.CHIPS
        ),
        ChoiceListingFilterDefinition(
            id = UserFilterValues.UserSessionFilterValues.USER_AUTH_PROVIDER,
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
            id = UserFilterValues.UserSessionFilterValues.IP_ADDRESS,
            title = stringResource(CommonRes.string.ui_common_ip_address),
            placeholder = stringResource(CommonRes.string.ui_common_search_placeholder)
        ),
        TextListingFilterDefinition(
            id = UserFilterValues.UserSessionFilterValues.DEVICE_NAME,
            title = stringResource(CommonRes.string.ui_common_device_name),
            placeholder = stringResource(CommonRes.string.ui_common_search_placeholder)
        )
    )
)
