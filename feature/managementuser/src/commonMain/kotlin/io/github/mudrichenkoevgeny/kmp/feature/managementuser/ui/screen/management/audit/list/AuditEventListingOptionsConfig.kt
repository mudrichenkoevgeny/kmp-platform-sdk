package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.list

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceFilterPresentationStyle
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterChoiceOption
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.TextListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.option.ListingOptionsConfig
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.localization.LocalCommonStrings
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.actor.AuditActorType
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.listing.AuditFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.listing.AuditSortValues
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.status.AuditStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole

@Composable
fun getAuditEventListingOptionsConfig(): ListingOptionsConfig {
    val commonUi = LocalCommonStrings.current.ui
    return ListingOptionsConfig(
        sortOptions = listOf(
            ListingSortDefinition(
                id = AuditSortValues.AuditEventSortBy.CREATED_AT.value,
                title = commonUi.createdAt
            )
        ),
        filters = listOf(
            TextListingFilterDefinition(
                id = AuditFilterValues.AuditEventFilterValues.ACTOR_ID,
                title = commonUi.actorId,
                placeholder = commonUi.searchPlaceholder
            ),
            ChoiceListingFilterDefinition(
                id = AuditFilterValues.AuditEventFilterValues.ACTOR_TYPE,
                title = commonUi.actorType,
                options = listOf(
                    ListingFilterChoiceOption(AuditActorType.USER.value, commonUi.user),
                    ListingFilterChoiceOption(AuditActorType.SYSTEM.value, commonUi.system),
                    ListingFilterChoiceOption(AuditActorType.SERVICE.value, commonUi.service)
                ),
                isMultiple = true,
                presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
            ),
            ChoiceListingFilterDefinition(
                id = AuditFilterValues.AuditEventFilterValues.ACTOR_USER_ROLE,
                title = commonUi.role,
                options = listOf(
                    ListingFilterChoiceOption(UserRole.STAFF.name, commonUi.staff),
                    ListingFilterChoiceOption(UserRole.ADMIN.name, commonUi.admin)
                ),
                isMultiple = true,
                presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
            ),
            TextListingFilterDefinition(
                id = AuditFilterValues.AuditEventFilterValues.ACTION,
                title = commonUi.action,
                placeholder = commonUi.searchPlaceholder
            ),
            TextListingFilterDefinition(
                id = AuditFilterValues.AuditEventFilterValues.RESOURCE,
                title = commonUi.resource,
                placeholder = commonUi.searchPlaceholder
            ),
            TextListingFilterDefinition(
                id = AuditFilterValues.AuditEventFilterValues.RESOURCE_ID,
                title = commonUi.resourceId,
                placeholder = commonUi.searchPlaceholder
            ),
            ChoiceListingFilterDefinition(
                id = AuditFilterValues.AuditEventFilterValues.STATUS,
                title = commonUi.status,
                options = listOf(
                    ListingFilterChoiceOption(AuditStatus.SUCCESS.value, commonUi.success),
                    ListingFilterChoiceOption(AuditStatus.FAILED.value, commonUi.failed),
                    ListingFilterChoiceOption(AuditStatus.DENIED.value, commonUi.denied)
                ),
                isMultiple = true,
                presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
            ),
            TextListingFilterDefinition(
                id = AuditFilterValues.AuditEventFilterValues.MESSAGE,
                title = commonUi.message,
                placeholder = commonUi.searchPlaceholder
            )
        )
    )
}
