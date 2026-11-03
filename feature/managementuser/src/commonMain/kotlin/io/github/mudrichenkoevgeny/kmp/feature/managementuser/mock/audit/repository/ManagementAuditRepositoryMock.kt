package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.audit.repository

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.audit.ManagementAuditRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventPrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.navigation.screen.management.audit.list.ManagementAuditEventListParams
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.audit.domain.model.event.createTestAuditEventPrivate
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.audit.domain.model.event.createTestAuditEventSummary
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.common.domain.model.listing.createTestPagedResult

@InternalApi
open class ManagementAuditRepositoryMock(
    var getAuditEventsResult: AppResult<PagedResult<AuditEventSummary>> = AppResult.Success(
        createTestPagedResult(listOf(createTestAuditEventSummary()))
    ),
    var getAuditEventResult: AppResult<AuditEventPrivate> = AppResult.Success(createTestAuditEventPrivate())
) : ManagementAuditRepository {

    override suspend fun getAuditEvents(
        params: ManagementAuditEventListParams?
    ): AppResult<PagedResult<AuditEventSummary>> = getAuditEventsResult

    override suspend fun getAuditEvent(eventId: String): AppResult<AuditEventPrivate> = getAuditEventResult
}
