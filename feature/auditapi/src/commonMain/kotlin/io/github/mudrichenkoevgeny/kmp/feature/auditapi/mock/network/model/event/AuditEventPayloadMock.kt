package io.github.mudrichenkoevgeny.kmp.feature.auditapi.mock.network.model.event

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.actor.AuditActorType
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.status.AuditStatus
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.network.model.event.AuditEventMetadataPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.network.model.event.AuditEventPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.audit.action.UserAuditActionType
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.audit.resource.UserAuditResourceType

@InternalApi
fun auditEventPayloadMock(
    id: String = "550e8400-e29b-41d4-a716-446655440000",
    actorType: String = AuditActorType.USER.name,
    action: String = UserAuditActionType.MANAGEMENT_UPDATE_USER.name,
    resource: String = UserAuditResourceType.USER.name,
    status: String = AuditStatus.SUCCESS.name,
    createdAt: Long = 1000L,
    actorId: String? = null,
    metadata: List<AuditEventMetadataPayload> = emptyList(),
) = AuditEventPayload(
    id = id,
    actorType = actorType,
    action = action,
    resource = resource,
    status = status,
    createdAt = createdAt,
    actorId = actorId,
    metadata = metadata,
)
