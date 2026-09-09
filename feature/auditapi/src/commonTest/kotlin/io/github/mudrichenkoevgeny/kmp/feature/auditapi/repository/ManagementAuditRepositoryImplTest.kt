package io.github.mudrichenkoevgeny.kmp.feature.auditapi.repository

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.mock.network.model.event.auditEventPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.network.api.ManagementAuditApi
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.action.CompositeAuditActionTypeParser
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.listing.AuditSortValues
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.metadata.CompositeAuditMetadataKeyParser
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.resource.CompositeAuditResourceTypeParser
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.mapper.audit.toAuditEvent
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.network.model.event.AuditEventPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.audit.action.UserAuditActionType
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.audit.resource.UserAuditResourceType
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class ManagementAuditRepositoryImplTest {

    private val actionParser = CompositeAuditActionTypeParser(setOf(UserAuditActionType.MANAGEMENT_UPDATE_USER))
    private val resourceParser = CompositeAuditResourceTypeParser(setOf(UserAuditResourceType.USER))
    private val metadataParser = CompositeAuditMetadataKeyParser(emptySet())

    private val api = object : ManagementAuditApi {
        var getAuditEventsResult: AppResult<PagedResult<AuditEventPayload>> = AppResult.Success(
            PagedResult(listOf(auditEventPayloadMock()), 1L, 1, 20, 1L),
        )
        var getAuditEventResult: AppResult<AuditEventPayload> = AppResult.Success(auditEventPayloadMock())

        override suspend fun getAuditEvents(
            pageNumber: Int?,
            pageSize: Int?,
            sortBy: AuditSortValues.AuditEventSortBy?,
            sortOrder: SortOrder?,
        ): AppResult<PagedResult<AuditEventPayload>> = getAuditEventsResult

        override suspend fun getAuditEvent(eventId: String): AppResult<AuditEventPayload> = getAuditEventResult
    }

    private val repository = ManagementAuditRepositoryImpl(
        managementAuditApi = api,
        compositeAuditActionTypeParser = actionParser,
        compositeAuditResourceTypeParser = resourceParser,
        compositeAuditMetadataKeyParser = metadataParser,
    )

    @Test
    fun getAuditEvents_mapsPagedPayloadToDomainModel_onSuccess() = runTest {
        val result = repository.getAuditEvents()

        assertIs<AppResult.Success<*>>(result)
        val data = (result as AppResult.Success).data
        assertEquals(1, data.items.size)
        assertEquals(auditEventPayloadMock().toAuditEvent(actionParser, resourceParser, metadataParser), data.items.first())
    }

    @Test
    fun getAuditEvent_mapsPayloadToDomainModel_onSuccess() = runTest {
        val result = repository.getAuditEvent("550e8400-e29b-41d4-a716-446655440000")

        assertIs<AppResult.Success<*>>(result)
        assertEquals(
            auditEventPayloadMock().toAuditEvent(actionParser, resourceParser, metadataParser),
            (result as AppResult.Success).data,
        )
    }
}
