package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.audit

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.navigation.screen.management.audit.list.ManagementAuditEventListParams
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.network.model.event.AuditEventPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.network.model.event.AuditEventSummaryPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult

/**
 * Network API contract for administrative audit log operations.
 */
public interface ManagementAuditApi {
    /**
     * Retrieves a paginated list of audit events.
     *
     * @param params Query and filter parameters DTO.
     * @return Paged result containing audit event summaries, or a failure.
     */
    public suspend fun getAuditEvents(
        params: ManagementAuditEventListParams? = null
    ): AppResult<PagedResult<AuditEventSummaryPayload>>

    /**
     * Retrieves specific audit event details by ID.
     *
     * @param eventId Unique audit event identifier string.
     * @return Audit event payload, or a failure.
     */
    public suspend fun getAuditEvent(eventId: String): AppResult<AuditEventPrivatePayload>
}
