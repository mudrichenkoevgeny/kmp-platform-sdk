package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.audit

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.audit.ManagementAuditRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.navigation.screen.management.audit.list.ManagementAuditEventListParams
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult

/**
 * Returns a paginated list of audit events for administrative purposes.
 *
 * @param managementAuditRepository Administrative audit repository.
 */
class GetAuditEventsUseCase(
    private val managementAuditRepository: ManagementAuditRepository
) {
    /**
     * @param params Query and filter parameters DTO.
     * @return Paginated result containing audit event summaries, or a failure.
     */
    suspend operator fun invoke(
        params: ManagementAuditEventListParams? = null
    ): AppResult<PagedResult<AuditEventSummary>> {
        return managementAuditRepository.getAuditEvents(params)
    }
}
