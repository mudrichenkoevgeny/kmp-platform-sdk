package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.audit

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.audit.ManagementAuditApi
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.action.CompositeAuditActionTypeParser
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.actor.AuditActorType
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.listing.AuditSortValues
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.metadata.CompositeAuditMetadataKeyParser
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.resource.CompositeAuditResourceTypeParser
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.status.AuditStatus
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.mapper.audit.toAuditEventPrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.mapper.audit.toAuditEventSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.network.model.event.AuditEventPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.network.model.event.AuditEventSummaryPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.audit.action.UserAuditActionType
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.audit.resource.UserAuditResourceType
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.audit.network.model.event.createTestAuditEventPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.audit.network.model.event.createTestAuditEventSummaryPayloadList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class ManagementAuditRepositoryImplTest {

    private val actionParser = CompositeAuditActionTypeParser(setOf(UserAuditActionType.MANAGEMENT_UPDATE_USER))
    private val resourceParser = CompositeAuditResourceTypeParser(setOf(UserAuditResourceType.USER))
    private val metadataParser = CompositeAuditMetadataKeyParser(emptySet())

    private val eventSummaryPayloadList = createTestAuditEventSummaryPayloadList()

    private val api = object : ManagementAuditApi {
        var getAuditEventsResult: AppResult<PagedResult<AuditEventSummaryPayload>> = AppResult.Success(
            PagedResult(eventSummaryPayloadList, eventSummaryPayloadList.size.toLong(), 1, 20, 1L)
        )
        var getAuditEventResult: AppResult<AuditEventPrivatePayload> = AppResult.Success(createTestAuditEventPrivatePayload())

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
        ): AppResult<PagedResult<AuditEventSummaryPayload>> = getAuditEventsResult

        override suspend fun getAuditEvent(eventId: String): AppResult<AuditEventPrivatePayload> = getAuditEventResult
    }

    private val repository = ManagementAuditRepositoryImpl(
        managementAuditApi = api,
        compositeAuditActionTypeParser = actionParser,
        compositeAuditResourceTypeParser = resourceParser,
        compositeAuditMetadataKeyParser = metadataParser
    )

    @Test
    fun getAuditEvents_mapsPagedPayloadToDomainModel_onSuccess() = runTest {
        val result = repository.getAuditEvents()

        assertIs<AppResult.Success<*>>(result)
        val data = (result as AppResult.Success).data
        assertEquals(eventSummaryPayloadList.size, data.items.size)
        assertEquals(eventSummaryPayloadList.first().toAuditEventSummary(actionParser, resourceParser), data.items.first())
    }

    @Test
    fun getAuditEvent_mapsPayloadToDomainModel_onSuccess() = runTest {
        val result = repository.getAuditEvent("550e8400-e29b-41d4-a716-446655440000")

        assertIs<AppResult.Success<*>>(result)
        assertEquals(
            createTestAuditEventPrivatePayload().toAuditEventPrivate(actionParser, resourceParser, metadataParser),
            (result as AppResult.Success).data
        )
    }
}