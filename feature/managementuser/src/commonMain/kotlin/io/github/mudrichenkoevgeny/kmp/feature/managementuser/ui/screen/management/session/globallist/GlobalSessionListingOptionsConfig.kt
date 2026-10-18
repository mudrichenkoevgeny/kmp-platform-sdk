package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.globallist

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceFilterPresentationStyle
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterChoiceOption
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.TextListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.option.ListingOptionsConfig
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.localization.LocalCommonStrings
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole

@Composable
fun getGlobalSessionListingOptionsConfig(): ListingOptionsConfig {
    val commonUi = LocalCommonStrings.current.ui
    return ListingOptionsConfig(
        sortOptions = listOf(
            ListingSortDefinition(
                id = UserSortValues.UserSessionSortBy.LAST_ACCESSED_AT.name,
                title = commonUi.lastAccessedAt
            ),
            ListingSortDefinition(
                id = UserSortValues.UserSessionSortBy.LAST_REAUTHENTICATED_AT.name,
                title = commonUi.lastReauthenticatedAt
            ),
            ListingSortDefinition(
                id = UserSortValues.UserSessionSortBy.EXPIRES_AT.name,
                title = commonUi.expiresAt
            ),
            ListingSortDefinition(
                id = UserSortValues.UserSessionSortBy.CREATED_AT.name,
                title = commonUi.createdAt
            ),
            ListingSortDefinition(
                id = UserSortValues.UserSessionSortBy.UPDATED_AT.name,
                title = commonUi.updatedAt
            )
        ),
        filters = listOf(
            ChoiceListingFilterDefinition(
                id = UserFilterValues.UserSessionFilterValues.USER_ROLE,
                title = commonUi.role,
                options = listOf(
                    ListingFilterChoiceOption(UserRole.STAFF.name, commonUi.staff),
                    ListingFilterChoiceOption(UserRole.ADMIN.name, commonUi.admin)
                ),
                isMultiple = true,
                presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
            ),
            ChoiceListingFilterDefinition(
                id = UserFilterValues.UserSessionFilterValues.USER_AUTH_PROVIDER,
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
            ChoiceListingFilterDefinition(
                id = UserFilterValues.UserSessionFilterValues.CLIENT_TYPE,
                title = commonUi.clientType,
                options = listOf(
                    ListingFilterChoiceOption(ClientType.ANDROID.value, commonUi.android),
                    ListingFilterChoiceOption(ClientType.IOS.value, commonUi.ios),
                    ListingFilterChoiceOption(ClientType.WEB.value, commonUi.web),
                    ListingFilterChoiceOption(ClientType.DESKTOP.value, commonUi.desktop)
                ),
                isMultiple = true,
                presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
            ),
            TextListingFilterDefinition(
                id = UserFilterValues.UserSessionFilterValues.USER_ID,
                title = commonUi.userId,
                placeholder = commonUi.searchPlaceholder
            ),
            TextListingFilterDefinition(
                id = UserFilterValues.UserSessionFilterValues.IDENTIFIER,
                title = commonUi.identifier,
                placeholder = commonUi.searchPlaceholder
            ),
            TextListingFilterDefinition(
                id = UserFilterValues.UserSessionFilterValues.IDENTIFIER_ID,
                title = commonUi.identifierId,
                placeholder = commonUi.searchPlaceholder
            ),
            TextListingFilterDefinition(
                id = UserFilterValues.UserSessionFilterValues.IP_ADDRESS,
                title = commonUi.ipAddress,
                placeholder = commonUi.searchPlaceholder
            ),
            TextListingFilterDefinition(
                id = UserFilterValues.UserSessionFilterValues.USER_AGENT,
                title = commonUi.userAgent,
                placeholder = commonUi.searchPlaceholder
            ),
            TextListingFilterDefinition(
                id = UserFilterValues.UserSessionFilterValues.LANGUAGE,
                title = commonUi.language,
                placeholder = commonUi.searchPlaceholder
            ),
            TextListingFilterDefinition(
                id = UserFilterValues.UserSessionFilterValues.DEVICE_ID,
                title = commonUi.deviceId,
                placeholder = commonUi.searchPlaceholder
            ),
            TextListingFilterDefinition(
                id = UserFilterValues.UserSessionFilterValues.DEVICE_NAME,
                title = commonUi.deviceName,
                placeholder = commonUi.searchPlaceholder
            ),
            TextListingFilterDefinition(
                id = UserFilterValues.UserSessionFilterValues.APP_VERSION,
                title = commonUi.appVersion,
                placeholder = commonUi.searchPlaceholder
            ),
            TextListingFilterDefinition(
                id = UserFilterValues.UserSessionFilterValues.OPERATION_SYSTEM_VERSION,
                title = commonUi.osVersion,
                placeholder = commonUi.searchPlaceholder
            )
        )
    )
}
