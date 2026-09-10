package io.github.mudrichenkoevgeny.kmp.feature.auditapi.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.repository.ManagementAuditRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEvent
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.actor.AuditActorType
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.status.AuditStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.listing.AuditSortValues

/**
 * Returns a paginated list of audit events for administrative purposes.
 *
 * @param managementAuditRepository Administrative audit repository.
 */
class GetAuditEventsUseCase(
    private val managementAuditRepository: ManagementAuditRepository
) {
    /**
     * @param pageNumber One-based page index.
     * @param pageSize Maximum items returned per page.
     * @param sortBy Field to sort by.
     * @param sortOrder Sorting direction.
     * @return Paginated result containing audit events, or a failure.
     */
    suspend operator fun invoke(
        pageNumber: Int? = null,
        pageSize: Int? = null,
        sortBy: AuditSortValues.AuditEventSortBy? = null,
        sortOrder: SortOrder? = null,
        actorIds: List<String>? = null,
        actorTypes: List<AuditActorType>? = null,
        actorUserRoles: List<UserRole>? = null,
        actions: List<String>? = null,
        resources: List<String>? = null,
        resourceIds: List<String>? = null,
        statuses: List<AuditStatus>? = null,
        messages: List<String>? = null
    ): AppResult<PagedResult<AuditEvent>> {
        return managementAuditRepository.getAuditEvents(
            pageNumber = pageNumber,
            pageSize = pageSize,
            sortBy = sortBy,
            sortOrder = sortOrder,
            actorIds = actorIds,
            actorTypes = actorTypes,
            actorUserRoles = actorUserRoles,
            actions = actions,
            resources = resources,
            resourceIds = resourceIds,
            statuses = statuses,
            messages = messages
        )
    }
}
