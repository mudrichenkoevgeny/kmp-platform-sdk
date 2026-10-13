package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.audit.domain.model.event

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.action.AuditActionType
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.actor.AuditActorType
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventId
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventPrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.metadata.AuditEventMetadata
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.resource.AuditResourceType
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.status.AuditStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.audit.action.UserAuditActionType
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.audit.resource.UserAuditResourceType
import kotlin.time.Instant

@InternalApi
fun auditEventSummaryMock(
    id: AuditEventId = AuditEventId.generate(),
    actorType: AuditActorType = AuditActorType.USER,
    action: AuditActionType = UserAuditActionType.MANAGEMENT_UPDATE_USER,
    resource: AuditResourceType = UserAuditResourceType.USER,
    status: AuditStatus = AuditStatus.SUCCESS,
    createdAt: Instant = Instant.fromEpochMilliseconds(0)
) = AuditEventSummary(
    id = id,
    actorType = actorType,
    action = action,
    resource = resource,
    status = status,
    createdAt = createdAt
)

@InternalApi
fun auditEventPrivateMock(
    id: AuditEventId = AuditEventId.generate(),
    actorId: String? = null,
    actorType: AuditActorType = AuditActorType.USER,
    actorUserRole: String? = null,
    action: AuditActionType = UserAuditActionType.MANAGEMENT_UPDATE_USER,
    resource: AuditResourceType = UserAuditResourceType.USER,
    resourceId: String? = null,
    status: AuditStatus = AuditStatus.SUCCESS,
    metadata: Set<AuditEventMetadata> = emptySet(),
    message: String? = null,
    createdAt: Instant = Instant.fromEpochMilliseconds(0)
) = AuditEventPrivate(
    id = id,
    actorId = actorId,
    actorType = actorType,
    actorUserRole = actorUserRole,
    action = action,
    resource = resource,
    resourceId = resourceId,
    status = status,
    metadata = metadata,
    message = message,
    createdAt = createdAt,
    updatedAt = null
)

@InternalApi
fun auditEventMock() = auditEventSummaryMock()