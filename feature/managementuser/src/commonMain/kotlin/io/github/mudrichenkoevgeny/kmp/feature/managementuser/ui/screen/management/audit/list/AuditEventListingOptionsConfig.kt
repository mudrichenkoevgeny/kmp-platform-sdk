package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.list

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceFilterPresentationStyle
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterChoiceOption
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.TextListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.option.ListingOptionsConfig
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.localization.LocalCommonStrings
import io.github.mudrichenkoevgeny.kmp.feature.user.localization.LocalUserStrings
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.actor.AuditActorType
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.listing.AuditEventFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.listing.AuditEventSortBy
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.status.AuditStatus
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserRole

/**
 * Creates the listing options configuration for audit event sorting and filtering.
 *
 * @return [ListingOptionsConfig] populated with available audit event sort definitions and filter controls.
 */
@Composable
public fun getAuditEventListingOptionsConfig(): ListingOptionsConfig {
    val commonUi = LocalCommonStrings.current.ui
    val userUi = LocalUserStrings.current.ui

    val actorTypeOptions = listOf(
        ListingFilterChoiceOption(AuditActorType.USER.value, commonUi.user),
        ListingFilterChoiceOption(AuditActorType.SYSTEM.value, commonUi.system),
        ListingFilterChoiceOption(AuditActorType.SERVICE.value, commonUi.service)
    )

    val roleOptions = listOf(
        ListingFilterChoiceOption(UserRole.STAFF.value, commonUi.staff),
        ListingFilterChoiceOption(UserRole.ADMIN.value, commonUi.admin)
    )

    val statusOptions = listOf(
        ListingFilterChoiceOption(AuditStatus.SUCCESS.value, commonUi.success),
        ListingFilterChoiceOption(AuditStatus.FAILED.value, commonUi.failed),
        ListingFilterChoiceOption(AuditStatus.DENIED.value, commonUi.denied)
    )

    return ListingOptionsConfig(
        sortOptions = listOf(
            ListingSortDefinition(
                id = AuditEventSortBy.CREATED_AT.value,
                title = commonUi.createdAt
            )
        ),
        filters = listOf(
            TextListingFilterDefinition(
                id = AuditEventFilterValues.EVENT_ID,
                title = userUi.auditEventId,
                placeholder = commonUi.searchPlaceholder
            ),
            TextListingFilterDefinition(
                id = AuditEventFilterValues.ACTOR_ID,
                title = commonUi.actorId,
                placeholder = commonUi.searchPlaceholder
            ),
            ChoiceListingFilterDefinition(
                id = AuditEventFilterValues.ACTOR_TYPE,
                title = commonUi.actorType,
                options = actorTypeOptions,
                isMultiple = true,
                presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
            ),
            ChoiceListingFilterDefinition(
                id = AuditEventFilterValues.EXCLUDE_ACTOR_TYPE,
                title = "${commonUi.actorType} (${commonUi.exclude})",
                options = actorTypeOptions,
                isMultiple = true,
                presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
            ),
            ChoiceListingFilterDefinition(
                id = AuditEventFilterValues.ACTOR_USER_ROLE,
                title = commonUi.role,
                options = roleOptions,
                isMultiple = true,
                presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
            ),
            TextListingFilterDefinition(
                id = AuditEventFilterValues.ACTION,
                title = commonUi.action,
                placeholder = commonUi.searchPlaceholder
            ),
            TextListingFilterDefinition(
                id = AuditEventFilterValues.EXCLUDE_ACTION,
                title = "${commonUi.action} (${commonUi.exclude})",
                placeholder = commonUi.searchPlaceholder
            ),
            TextListingFilterDefinition(
                id = AuditEventFilterValues.RESOURCE,
                title = commonUi.resource,
                placeholder = commonUi.searchPlaceholder
            ),
            TextListingFilterDefinition(
                id = AuditEventFilterValues.RESOURCE_ID,
                title = commonUi.resourceId,
                placeholder = commonUi.searchPlaceholder
            ),
            ChoiceListingFilterDefinition(
                id = AuditEventFilterValues.STATUS,
                title = commonUi.status,
                options = statusOptions,
                isMultiple = true,
                presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
            ),
            ChoiceListingFilterDefinition(
                id = AuditEventFilterValues.EXCLUDE_STATUS,
                title = "${commonUi.status} (${commonUi.exclude})",
                options = statusOptions,
                isMultiple = true,
                presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
            ),
            TextListingFilterDefinition(
                id = AuditEventFilterValues.CREATED_AT_FROM,
                title = "${commonUi.createdAt} (${commonUi.from})",
                placeholder = commonUi.searchPlaceholder
            ),
            TextListingFilterDefinition(
                id = AuditEventFilterValues.CREATED_AT_TO,
                title = "${commonUi.createdAt} (${commonUi.to})",
                placeholder = commonUi.searchPlaceholder
            ),
            TextListingFilterDefinition(
                id = AuditEventFilterValues.SEARCH_QUERY,
                title = commonUi.searchPlaceholder,
                placeholder = commonUi.searchPlaceholder
            )
        )
    )
}
