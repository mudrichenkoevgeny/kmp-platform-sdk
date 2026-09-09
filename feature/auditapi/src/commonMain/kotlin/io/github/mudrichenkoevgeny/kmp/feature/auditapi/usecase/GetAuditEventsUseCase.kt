package io.github.mudrichenkoevgeny.kmp.feature.auditapi.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.repository.ManagementAuditRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEvent
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
        sortOrder: SortOrder? = null
    ): AppResult<PagedResult<AuditEvent>> {
        return managementAuditRepository.getAuditEvents(
            pageNumber = pageNumber,
            pageSize = pageSize,
            sortBy = sortBy,
            sortOrder = sortOrder
        )
    }
}
