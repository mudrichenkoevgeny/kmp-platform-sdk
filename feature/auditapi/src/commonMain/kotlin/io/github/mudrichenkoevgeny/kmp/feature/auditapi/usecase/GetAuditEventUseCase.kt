package io.github.mudrichenkoevgeny.kmp.feature.auditapi.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.repository.ManagementAuditRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEvent

/**
 * Retrieves full details of a specific audit event.
 *
 * @param managementAuditRepository Administrative audit repository.
 */
class GetAuditEventUseCase(
    private val managementAuditRepository: ManagementAuditRepository
) {
    /**
     * @param eventId Unique audit event identifier string.
     * @return Detailed audit event model, or a failure.
     */
    suspend operator fun invoke(eventId: String): AppResult<AuditEvent> {
        return managementAuditRepository.getAuditEvent(eventId)
    }
}
