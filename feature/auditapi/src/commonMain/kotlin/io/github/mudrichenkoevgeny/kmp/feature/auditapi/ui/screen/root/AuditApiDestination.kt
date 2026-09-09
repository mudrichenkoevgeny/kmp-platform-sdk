package io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.root

import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventId
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
sealed class AuditApiDestination {
    @Serializable
    object Main : AuditApiDestination()

    @Serializable
    data class Detail(val eventIdValue: String) : AuditApiDestination() {
        val eventId: AuditEventId get() = AuditEventId(Uuid.parse(eventIdValue))
    }
}
