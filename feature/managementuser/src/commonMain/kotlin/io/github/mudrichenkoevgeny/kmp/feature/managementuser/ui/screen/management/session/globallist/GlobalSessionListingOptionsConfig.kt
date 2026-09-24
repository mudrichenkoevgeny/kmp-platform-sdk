package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.globallist

import androidx.compose.runtime.Composable
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
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import org.jetbrains.compose.resources.stringResource
import io.github.mudrichenkoevgeny.kmp.core.common.Res as CommonRes

@Composable
fun getGlobalSessionListingOptionsConfig(): ListingOptionsConfig = ListingOptionsConfig(
    sortOptions = listOf(
        ListingSortDefinition(
            id = UserSortValues.UserSessionSortBy.LAST_ACCESSED_AT.name,
            title = stringResource(CommonRes.string.ui_common_last_accessed_at)
        ),
        ListingSortDefinition(
            id = UserSortValues.UserSessionSortBy.LAST_REAUTHENTICATED_AT.name,
            title = stringResource(CommonRes.string.ui_common_last_reauthenticated_at)
        ),
        ListingSortDefinition(
            id = UserSortValues.UserSessionSortBy.EXPIRES_AT.name,
            title = stringResource(CommonRes.string.ui_common_expires_at)
        ),
        ListingSortDefinition(
            id = UserSortValues.UserSessionSortBy.CREATED_AT.name,
            title = stringResource(CommonRes.string.ui_common_created_at)
        ),
        ListingSortDefinition(
            id = UserSortValues.UserSessionSortBy.UPDATED_AT.name,
            title = stringResource(CommonRes.string.ui_common_updated_at)
        )
    ),
    filters = listOf(
        ChoiceListingFilterDefinition(
            id = UserFilterValues.UserSessionFilterValues.USER_ROLE,
            title = stringResource(CommonRes.string.ui_common_role),
            options = listOf(
                ListingFilterChoiceOption(UserRole.STAFF.name, stringResource(CommonRes.string.ui_common_staff)),
                ListingFilterChoiceOption(UserRole.ADMIN.name, stringResource(CommonRes.string.ui_common_admin))
            ),
            isMultiple = true,
            presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
        ),
        ChoiceListingFilterDefinition(
            id = UserFilterValues.UserSessionFilterValues.USER_AUTH_PROVIDER,
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
            presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
        ),
        TextListingFilterDefinition(
            id = UserFilterValues.UserSessionFilterValues.USER_ID,
            title = stringResource(CommonRes.string.ui_common_user_id),
            placeholder = stringResource(CommonRes.string.ui_common_search_placeholder)
        ),
        TextListingFilterDefinition(
            id = UserFilterValues.UserSessionFilterValues.IDENTIFIER,
            title = stringResource(CommonRes.string.ui_common_identifier),
            placeholder = stringResource(CommonRes.string.ui_common_search_placeholder)
        ),
        TextListingFilterDefinition(
            id = UserFilterValues.UserSessionFilterValues.IDENTIFIER_ID,
            title = stringResource(CommonRes.string.ui_common_identifier_id),
            placeholder = stringResource(CommonRes.string.ui_common_search_placeholder)
        ),
        TextListingFilterDefinition(
            id = UserFilterValues.UserSessionFilterValues.IP_ADDRESS,
            title = stringResource(CommonRes.string.ui_common_ip_address),
            placeholder = stringResource(CommonRes.string.ui_common_search_placeholder)
        ),
        TextListingFilterDefinition(
            id = UserFilterValues.UserSessionFilterValues.USER_AGENT,
            title = stringResource(CommonRes.string.ui_common_user_agent),
            placeholder = stringResource(CommonRes.string.ui_common_search_placeholder)
        ),
        TextListingFilterDefinition(
            id = UserFilterValues.UserSessionFilterValues.LANGUAGE,
            title = stringResource(CommonRes.string.ui_common_language),
            placeholder = stringResource(CommonRes.string.ui_common_search_placeholder)
        ),
        TextListingFilterDefinition(
            id = UserFilterValues.UserSessionFilterValues.DEVICE_ID,
            title = stringResource(CommonRes.string.ui_common_device_id),
            placeholder = stringResource(CommonRes.string.ui_common_search_placeholder)
        ),
        TextListingFilterDefinition(
            id = UserFilterValues.UserSessionFilterValues.DEVICE_NAME,
            title = stringResource(CommonRes.string.ui_common_device_name),
            placeholder = stringResource(CommonRes.string.ui_common_search_placeholder)
        ),
        TextListingFilterDefinition(
            id = UserFilterValues.UserSessionFilterValues.APP_VERSION,
            title = stringResource(CommonRes.string.ui_common_app_version),
            placeholder = stringResource(CommonRes.string.ui_common_search_placeholder)
        ),
        TextListingFilterDefinition(
            id = UserFilterValues.UserSessionFilterValues.OPERATION_SYSTEM_VERSION,
            title = stringResource(CommonRes.string.ui_common_os_version),
            placeholder = stringResource(CommonRes.string.ui_common_search_placeholder)
        )
    )
)