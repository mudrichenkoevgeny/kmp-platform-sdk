package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.audit.domain.model.event

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.action.AuditActionType
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.actor.AuditActorType
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEvent
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventId
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditValueSensitivity
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.metadata.AuditEventMetadata
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.resource.AuditResourceType
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.status.AuditStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.audit.action.UserAuditActionType
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.audit.resource.UserAuditResourceType
import kotlin.time.Instant

@InternalApi
fun auditEventMock(
    id: AuditEventId = AuditEventId.generate(),
    actorId: String? = null,
    actorType: AuditActorType = AuditActorType.USER,
    actorUserRole: String? = null,
    action: AuditActionType = UserAuditActionType.MANAGEMENT_UPDATE_USER,
    resource: AuditResourceType = UserAuditResourceType.USER,
    resourceId: String? = null,
    resourceValueSensitivity: AuditValueSensitivity = AuditValueSensitivity.NON_SENSITIVE,
    status: AuditStatus = AuditStatus.SUCCESS,
    metadata: Set<AuditEventMetadata> = emptySet(),
    message: String? = null,
    createdAt: Instant = Instant.fromEpochMilliseconds(0)
) = AuditEvent(
    id = id,
    actorId = actorId,
    actorType = actorType,
    actorUserRole = actorUserRole,
    action = action,
    resource = resource,
    resourceId = resourceId,
    resourceValueSensitivity = resourceValueSensitivity,
    status = status,
    metadata = metadata,
    message = message,
    createdAt = createdAt
)