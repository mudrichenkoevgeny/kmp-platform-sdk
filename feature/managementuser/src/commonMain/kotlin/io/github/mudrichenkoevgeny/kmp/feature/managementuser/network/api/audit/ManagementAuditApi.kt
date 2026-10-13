package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.audit

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.actor.AuditActorType
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.listing.AuditSortValues
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.status.AuditStatus
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.network.model.event.AuditEventPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.network.model.event.AuditEventSummaryPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole

/**
 * Network API contract for administrative audit log operations.
 */
interface ManagementAuditApi {
    /**
     * Retrieves a paginated list of audit events.
     *
     * @param pageNumber Zero-based page index.
     * @param pageSize Number of items per page.
     * @param sortBy Field by which to sort results.
     * @param sortOrder Sorting direction (ascending or descending).
     * @param actorIds Filter by actor IDs.
     * @param actorTypes Filter by actor types.
     * @param actorUserRoles Filter by actor user roles.
     * @param actions Filter by action names.
     * @param resources Filter by resource types.
     * @param resourceIds Filter by resource IDs.
     * @param statuses Filter by audit statuses.
     * @param messages Filter by message substrings.
     * @return Paged result containing audit event summaries, or a failure.
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
    ): AppResult<PagedResult<AuditEventSummaryPayload>>

    /**
     * Retrieves specific audit event details by ID.
     *
     * @param eventId Unique audit event identifier string.
     * @return Audit event payload, or a failure.
     */
    suspend fun getAuditEvent(eventId: String): AppResult<AuditEventPrivatePayload>
}