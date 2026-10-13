package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.audit.repository

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.audit.domain.model.event.auditEventPrivateMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.audit.domain.model.event.auditEventSummaryMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.audit.ManagementAuditRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.actor.AuditActorType
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventPrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.listing.AuditSortValues
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.status.AuditStatus
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole

@InternalApi
open class ManagementAuditRepositoryMock(
    var getAuditEventsResult: AppResult<PagedResult<AuditEventSummary>> = AppResult.Success(
        PagedResult(listOf(auditEventSummaryMock()), 1L, 1, 20, 1L)
    ),
    var getAuditEventResult: AppResult<AuditEventPrivate> = AppResult.Success(auditEventPrivateMock())
) : ManagementAuditRepository {

    override suspend fun getAuditEvents(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: AuditSortValues.AuditEventSortBy?,
        sortOrder: SortOrder?,
        actorIds: List<String>?,
        actorTypes: List<AuditActorType>?,
        actorUserRoles: List<UserRole>?,
        actions: List<String>?,
        resources: List<String>?,
        resourceIds: List<String>?,
        statuses: List<AuditStatus>?,
        messages: List<String>?
    ): AppResult<PagedResult<AuditEventSummary>> = getAuditEventsResult

    override suspend fun getAuditEvent(eventId: String): AppResult<AuditEventPrivate> = getAuditEventResult
}