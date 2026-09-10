package io.github.mudrichenkoevgeny.kmp.feature.auditapi.repository

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEvent
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.actor.AuditActorType
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.status.AuditStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.listing.AuditSortValues

/**
 * Repository contract providing access to administrative audit logs and events.
 */
interface ManagementAuditRepository {
    /**
     * Retrieves a paginated list of domain audit events.
     *
     * @param pageNumber One-based page index.
     * @param pageSize Maximum items returned per page.
     * @param sortBy Field to sort by.
     * @param sortOrder Sorting direction.
     * @return Paginated result containing domain audit events, or a failure.
     */
    suspend fun getAuditEvents(
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
    ): AppResult<PagedResult<AuditEvent>>

    /**
     * Retrieves specific domain audit event details by ID.
     *
     * @param eventId Unique audit event identifier string.
     * @return Domain audit event, or a failure.
     */
    suspend fun getAuditEvent(eventId: String): AppResult<AuditEvent>
}
