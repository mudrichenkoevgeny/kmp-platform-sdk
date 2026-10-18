package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.globallist

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.BooleanListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceFilterPresentationStyle
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterChoiceOption
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.NumberListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.option.ListingOptionsConfig
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.localization.LocalCommonStrings
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.accountlockout.AccountLockoutType
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.accountstatus.UserAccountStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole

@Composable
fun getGlobalUserListingOptionsConfig(): ListingOptionsConfig {
    val commonUi = LocalCommonStrings.current.ui
    return ListingOptionsConfig(
        sortOptions = listOf(
            ListingSortDefinition(
                id = UserSortValues.UserSortBy.LAST_LOGIN_AT.name,
                title = commonUi.lastLoginAt
            ),
            ListingSortDefinition(
                id = UserSortValues.UserSortBy.LAST_ACTIVE_AT.name,
                title = commonUi.lastActiveAt
            ),
            ListingSortDefinition(
                id = UserSortValues.UserSortBy.SCHEDULED_PERMANENT_DELETION_AT.name,
                title = commonUi.scheduledPermanentDeletionAt
            ),
            ListingSortDefinition(
                id = UserSortValues.UserSortBy.ACCOUNT_LOCKOUT_TYPE.name,
                title = commonUi.lockoutType
            ),
            ListingSortDefinition(
                id = UserSortValues.UserSortBy.TEMPORARY_LOCKOUT_UNTIL.name,
                title = commonUi.lockoutUntil
            ),
            ListingSortDefinition(
                id = UserSortValues.UserSortBy.CREATED_AT.name,
                title = commonUi.createdAt
            ),
            ListingSortDefinition(
                id = UserSortValues.UserSortBy.UPDATED_AT.name,
                title = commonUi.updatedAt
            )
        ),
        filters = listOf(
            ChoiceListingFilterDefinition(
                id = UserFilterValues.UserFilterValues.ROLE,
                title = commonUi.role,
                options = listOf(
                    ListingFilterChoiceOption(UserRole.STAFF.name, commonUi.staff),
                    ListingFilterChoiceOption(UserRole.ADMIN.name, commonUi.admin)
                ),
                isMultiple = true,
                presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
            ),
            ChoiceListingFilterDefinition(
                id = UserFilterValues.UserFilterValues.ACCOUNT_STATUS,
                title = commonUi.status,
                options = listOf(
                    ListingFilterChoiceOption(UserAccountStatus.ACTIVE.name, commonUi.active),
                    ListingFilterChoiceOption(UserAccountStatus.READ_ONLY.name, commonUi.readOnly),
                    ListingFilterChoiceOption(UserAccountStatus.BANNED.name, commonUi.banned),
                    ListingFilterChoiceOption(UserAccountStatus.SECURITY_HOLD.name, commonUi.securityHold),
                    ListingFilterChoiceOption(UserAccountStatus.PENDING_DELETION.name, commonUi.pendingDeletion)
                ),
                isMultiple = true,
                presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
            ),
            ChoiceListingFilterDefinition(
                id = UserFilterValues.UserFilterValues.ACCOUNT_LOCKOUT_TYPE,
                title = commonUi.lockoutType,
                options = listOf(
                    ListingFilterChoiceOption(AccountLockoutType.NONE.name, commonUi.lockoutNone),
                    ListingFilterChoiceOption(AccountLockoutType.INDEFINITE.name, commonUi.lockoutIndefinite),
                    ListingFilterChoiceOption(AccountLockoutType.TEMPORARY.name, commonUi.lockoutTemporary)
                ),
                isMultiple = true,
                presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
            ),
            BooleanListingFilterDefinition(
                id = UserFilterValues.UserFilterValues.IS_TOTP_ENABLED,
                title = commonUi.totpEnabled
            ),
            NumberListingFilterDefinition(
                id = UserFilterValues.UserFilterValues.AUTHORITY_LEVEL_FROM,
                title = commonUi.authorityLevelFrom,
                placeholder = "0",
                minValue = AuthorityLevel.MIN_AUTHORITY_LEVEL,
                maxValue = AuthorityLevel.MAX_AUTHORITY_LEVEL,
                defaultValueOnFocusLost = AuthorityLevel.MIN_AUTHORITY_LEVEL
            ),
            NumberListingFilterDefinition(
                id = UserFilterValues.UserFilterValues.AUTHORITY_LEVEL_TO,
                title = commonUi.authorityLevelTo,
                placeholder = "100",
                minValue = AuthorityLevel.MIN_AUTHORITY_LEVEL,
                maxValue = AuthorityLevel.MAX_AUTHORITY_LEVEL,
                defaultValueOnFocusLost = AuthorityLevel.MAX_AUTHORITY_LEVEL
            )
        )
    )
}

object AuthorityLevel {
    const val MIN_AUTHORITY_LEVEL = 0L
    const val MAX_AUTHORITY_LEVEL = 100L
}
