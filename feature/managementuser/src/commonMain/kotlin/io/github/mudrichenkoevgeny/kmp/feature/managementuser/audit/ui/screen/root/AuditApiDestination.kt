package io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.ui.screen.root

import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventId
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.toAuditEventIdOrThrow
import kotlinx.serialization.Serializable

@Serializable
sealed class AuditApiDestination {
    @Serializable
    object Main : AuditApiDestination()

    @Serializable
    data class Detail(val eventIdValue: String) : AuditApiDestination() {
        val eventId: AuditEventId get() = eventIdValue.toAuditEventIdOrThrow()
    }
}
