package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.globallist

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.*
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.BooleanListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceFilterPresentationStyle
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterChoiceOption
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.NumberListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.option.ListingOptionsConfig
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortDefinition
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.accountlockout.AccountLockoutType
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.accountstatus.UserAccountStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import org.jetbrains.compose.resources.stringResource
import io.github.mudrichenkoevgeny.kmp.core.common.Res as CommonRes

@Composable
fun getGlobalUserListingOptionsConfig(): ListingOptionsConfig = ListingOptionsConfig(
    sortOptions = listOf(
        ListingSortDefinition(
            id = UserSortValues.UserSortBy.LAST_LOGIN_AT.name,
            title = stringResource(CommonRes.string.ui_common_last_login_at)
        ),
        ListingSortDefinition(
            id = UserSortValues.UserSortBy.LAST_ACTIVE_AT.name,
            title = stringResource(CommonRes.string.ui_common_last_active_at)
        ),
        ListingSortDefinition(
            id = UserSortValues.UserSortBy.SCHEDULED_PERMANENT_DELETION_AT.name,
            title = stringResource(CommonRes.string.ui_common_scheduled_permanent_deletion_at)
        ),
        ListingSortDefinition(
            id = UserSortValues.UserSortBy.ACCOUNT_LOCKOUT_TYPE.name,
            title = stringResource(CommonRes.string.ui_common_lockout_type)
        ),
        ListingSortDefinition(
            id = UserSortValues.UserSortBy.TEMPORARY_LOCKOUT_UNTIL.name,
            title = stringResource(CommonRes.string.ui_common_lockout_until)
        ),
        ListingSortDefinition(
            id = UserSortValues.UserSortBy.CREATED_AT.name,
            title = stringResource(CommonRes.string.ui_common_created_at)
        ),
        ListingSortDefinition(
            id = UserSortValues.UserSortBy.UPDATED_AT.name,
            title = stringResource(CommonRes.string.ui_common_updated_at)
        )
    ),
    filters = listOf(
        ChoiceListingFilterDefinition(
            id = UserFilterValues.UserFilterValues.ROLE,
            title = stringResource(CommonRes.string.ui_common_role),
            options = listOf(
                ListingFilterChoiceOption(UserRole.STAFF.name, stringResource(CommonRes.string.ui_common_staff)),
                ListingFilterChoiceOption(UserRole.ADMIN.name, stringResource(CommonRes.string.ui_common_admin))
            ),
            isMultiple = true,
            presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
        ),
        ChoiceListingFilterDefinition(
            id = UserFilterValues.UserFilterValues.ACCOUNT_STATUS,
            title = stringResource(CommonRes.string.ui_common_status),
            options = listOf(
                ListingFilterChoiceOption(UserAccountStatus.ACTIVE.name, stringResource(CommonRes.string.ui_common_active)),
                ListingFilterChoiceOption(UserAccountStatus.READ_ONLY.name, stringResource(CommonRes.string.ui_common_read_only)),
                ListingFilterChoiceOption(UserAccountStatus.BANNED.name, stringResource(CommonRes.string.ui_common_banned)),
                ListingFilterChoiceOption(UserAccountStatus.SECURITY_HOLD.name, stringResource(CommonRes.string.ui_common_security_hold)),
                ListingFilterChoiceOption(UserAccountStatus.PENDING_DELETION.name, stringResource(CommonRes.string.ui_common_pending_deletion))
            ),
            isMultiple = true,
            presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
        ),
        ChoiceListingFilterDefinition(
            id = UserFilterValues.UserFilterValues.ACCOUNT_LOCKOUT_TYPE,
            title = stringResource(CommonRes.string.ui_common_lockout_type),
            options = listOf(
                ListingFilterChoiceOption(AccountLockoutType.NONE.name, stringResource(CommonRes.string.ui_common_lockout_none)),
                ListingFilterChoiceOption(AccountLockoutType.INDEFINITE.name, stringResource(CommonRes.string.ui_common_lockout_indefinite)),
                ListingFilterChoiceOption(AccountLockoutType.TEMPORARY.name, stringResource(CommonRes.string.ui_common_lockout_temporary))
            ),
            isMultiple = true,
            presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
        ),
        BooleanListingFilterDefinition(
            id = UserFilterValues.UserFilterValues.IS_TOTP_ENABLED,
            title = stringResource(CommonRes.string.ui_common_totp_enabled)
        ),
        NumberListingFilterDefinition(
            id = UserFilterValues.UserFilterValues.AUTHORITY_LEVEL_FROM,
            title = stringResource(CommonRes.string.ui_common_authority_level_from),
            placeholder = "0",
            minValue = AuthorityLevel.MIN_AUTHORITY_LEVEL,
            maxValue = AuthorityLevel.MAX_AUTHORITY_LEVEL,
            defaultValueOnFocusLost = AuthorityLevel.MIN_AUTHORITY_LEVEL
        ),
        NumberListingFilterDefinition(
            id = UserFilterValues.UserFilterValues.AUTHORITY_LEVEL_TO,
            title = stringResource(CommonRes.string.ui_common_authority_level_to),
            placeholder = "100",
            minValue = AuthorityLevel.MIN_AUTHORITY_LEVEL,
            maxValue = AuthorityLevel.MAX_AUTHORITY_LEVEL,
            defaultValueOnFocusLost = AuthorityLevel.MAX_AUTHORITY_LEVEL
        )
    )
)

object AuthorityLevel {
    const val MIN_AUTHORITY_LEVEL = 0L
    const val MAX_AUTHORITY_LEVEL = 100L
}