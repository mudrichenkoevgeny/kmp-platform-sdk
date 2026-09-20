package io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.domain.model.listing.pagedResultMock
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.repository.ManagementAuditRepository
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.mock.domain.model.event.auditEventMock
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.listing.AuditSortValues
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class GetAuditEventsUseCaseTest {

    @Test
    fun invoke_returnsPagedResult_whenRepositorySucceeds() = runTest {
        val event = auditEventMock()
        val expectedResult = pagedResultMock(listOf(event))
        val repository = object : ManagementAuditRepository {
            override suspend fun getAuditEvents(
                pageNumber: Int?,
                pageSize: Int?,
                sortBy: AuditSortValues.AuditEventSortBy?,
                sortOrder: SortOrder?
            ) = AppResult.Success(expectedResult)

            override suspend fun getAuditEvent(eventId: String) = AppResult.Success(event)
        }
        val useCase = GetAuditEventsUseCase(repository)

        val result = useCase()

        assertIs<AppResult.Success<*>>(result)
        assertEquals(expectedResult, result.data)
    }
}
