package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.audit

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventPrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.navigation.screen.management.audit.list.ManagementAuditEventListParams
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult

/**
 * Repository contract providing access to administrative audit logs and events.
 */
public interface ManagementAuditRepository {
    /**
     * Retrieves a paginated list of domain audit events.
     *
     * @param params Query and filter parameters DTO.
     * @return Paginated result containing domain audit events, or a failure.
     */
    public suspend fun getAuditEvents(
        params: ManagementAuditEventListParams? = null
    ): AppResult<PagedResult<AuditEventSummary>>

    /**
     * Retrieves specific domain audit event details by ID.
     *
     * @param eventId Unique audit event identifier string.
     * @return Domain audit event, or a failure.
     */
    public suspend fun getAuditEvent(eventId: String): AppResult<AuditEventPrivate>
}
