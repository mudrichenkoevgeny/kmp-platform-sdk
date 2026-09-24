package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.list

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.*
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceFilterPresentationStyle
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterChoiceOption
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.TextListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.option.ListingOptionsConfig
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortDefinition
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.actor.AuditActorType
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.listing.AuditFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.listing.AuditSortValues
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.status.AuditStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import org.jetbrains.compose.resources.stringResource
import io.github.mudrichenkoevgeny.kmp.core.common.Res as CommonRes

@Composable
fun getAuditEventListingOptionsConfig(): ListingOptionsConfig = ListingOptionsConfig(
    sortOptions = listOf(
        ListingSortDefinition(
            id = AuditSortValues.AuditEventSortBy.CREATED_AT.serialName,
            title = stringResource(CommonRes.string.ui_common_created_at)
        )
    ),
    filters = listOf(
        TextListingFilterDefinition(
            id = AuditFilterValues.AuditEventFilterValues.ACTOR_ID,
            title = stringResource(CommonRes.string.ui_common_actor_id),
            placeholder = stringResource(CommonRes.string.ui_common_search_placeholder)
        ),
        ChoiceListingFilterDefinition(
            id = AuditFilterValues.AuditEventFilterValues.ACTOR_TYPE,
            title = stringResource(CommonRes.string.ui_common_actor_type),
            options = listOf(
                ListingFilterChoiceOption(AuditActorType.USER.serialName, stringResource(CommonRes.string.ui_common_user)),
                ListingFilterChoiceOption(AuditActorType.SYSTEM.serialName, stringResource(CommonRes.string.ui_common_system)),
                ListingFilterChoiceOption(AuditActorType.SERVICE.serialName, stringResource(CommonRes.string.ui_common_service))
            ),
            isMultiple = true,
            presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
        ),
        ChoiceListingFilterDefinition(
            id = AuditFilterValues.AuditEventFilterValues.ACTOR_USER_ROLE,
            title = stringResource(CommonRes.string.ui_common_role),
            options = listOf(
                ListingFilterChoiceOption(UserRole.STAFF.name, stringResource(CommonRes.string.ui_common_staff)),
                ListingFilterChoiceOption(UserRole.ADMIN.name, stringResource(CommonRes.string.ui_common_admin))
            ),
            isMultiple = true,
            presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
        ),
        TextListingFilterDefinition(
            id = AuditFilterValues.AuditEventFilterValues.ACTION,
            title = stringResource(CommonRes.string.ui_common_action),
            placeholder = stringResource(CommonRes.string.ui_common_search_placeholder)
        ),
        TextListingFilterDefinition(
            id = AuditFilterValues.AuditEventFilterValues.RESOURCE,
            title = stringResource(CommonRes.string.ui_common_resource),
            placeholder = stringResource(CommonRes.string.ui_common_search_placeholder)
        ),
        TextListingFilterDefinition(
            id = AuditFilterValues.AuditEventFilterValues.RESOURCE_ID,
            title = stringResource(CommonRes.string.ui_common_resource_id),
            placeholder = stringResource(CommonRes.string.ui_common_search_placeholder)
        ),
        ChoiceListingFilterDefinition(
            id = AuditFilterValues.AuditEventFilterValues.STATUS,
            title = stringResource(CommonRes.string.ui_common_status),
            options = listOf(
                ListingFilterChoiceOption(AuditStatus.SUCCESS.serialName, stringResource(CommonRes.string.ui_common_success)),
                ListingFilterChoiceOption(AuditStatus.FAILED.serialName, stringResource(CommonRes.string.ui_common_failed)),
                ListingFilterChoiceOption(AuditStatus.DENIED.serialName, stringResource(CommonRes.string.ui_common_denied))
            ),
            isMultiple = true,
            presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
        ),
        TextListingFilterDefinition(
            id = AuditFilterValues.AuditEventFilterValues.MESSAGE,
            title = stringResource(CommonRes.string.ui_common_message),
            placeholder = stringResource(CommonRes.string.ui_common_search_placeholder)
        )
    )
)