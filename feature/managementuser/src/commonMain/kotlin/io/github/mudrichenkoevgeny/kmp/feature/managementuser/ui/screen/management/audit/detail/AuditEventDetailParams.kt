package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.detail

import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventId
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.toAuditEventIdOrThrow
import kotlinx.serialization.Serializable

/**
 * Navigation arguments for audit event detail screen.
 */
@Serializable
data class AuditEventDetailParams(
    val eventIdValue: String
) {
    val eventId: AuditEventId get() = eventIdValue.toAuditEventIdOrThrow()
}
