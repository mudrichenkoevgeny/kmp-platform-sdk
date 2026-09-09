package io.github.mudrichenkoevgeny.kmp.feature.auditapi.network.api

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.listing.AuditSortValues
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.network.model.event.AuditEventPayload

/**
 * Network API contract for administrative audit log operations.
 */
interface ManagementAuditApi {
    /**
     * Retrieves a paginated list of audit events.
     *
     * @param pageNumber One-based page index.
     * @param pageSize Maximum items returned per page.
     * @param sortBy Field to sort by.
     * @param sortOrder Sorting direction.
     * @return Paginated result containing audit event payloads, or a failure.
     */
    suspend fun getAuditEvents(
        pageNumber: Int? = null,
        pageSize: Int? = null,
        sortBy: AuditSortValues.AuditEventSortBy? = null,
        sortOrder: SortOrder? = null
    ): AppResult<PagedResult<AuditEventPayload>>

    /**
     * Retrieves specific audit event details by ID.
     *
     * @param eventId Unique audit event identifier string.
     * @return Audit event payload, or a failure.
     */
    suspend fun getAuditEvent(eventId: String): AppResult<AuditEventPayload>
}
