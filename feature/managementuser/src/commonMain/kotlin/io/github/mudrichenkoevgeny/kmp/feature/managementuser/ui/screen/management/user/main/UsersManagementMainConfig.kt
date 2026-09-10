package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.main

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.Res as CommonRes
import io.github.mudrichenkoevgeny.kmp.core.common.*
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.BooleanListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceFilterPresentationStyle
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterChoiceOption
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.NumberListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.option.ListingOptionsConfig
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortDefinition
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.accountstatus.UserAccountStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import org.jetbrains.compose.resources.stringResource

@Composable
fun getUsersManagementMainListingConfig(): ListingOptionsConfig = ListingOptionsConfig(
    sortOptions = listOf(
        ListingSortDefinition(
            id = "created_at",
            title = stringResource(CommonRes.string.ui_common_created_at)
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
            presentationStyle = ChoiceFilterPresentationStyle.CHIPS
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
        BooleanListingFilterDefinition(
            id = UserFilterValues.UserFilterValues.IS_TOTP_ENABLED,
            title = stringResource(CommonRes.string.ui_common_totp_enabled)
        ),
        NumberListingFilterDefinition(
            id = UserFilterValues.UserFilterValues.AUTHORITY_LEVEL_FROM,
            title = stringResource(CommonRes.string.ui_common_authority_level_from),
            placeholder = "e.g. 1"
        ),
        NumberListingFilterDefinition(
            id = UserFilterValues.UserFilterValues.AUTHORITY_LEVEL_TO,
            title = stringResource(CommonRes.string.ui_common_authority_level_to),
            placeholder = "e.g. 10"
        )
    )
)
