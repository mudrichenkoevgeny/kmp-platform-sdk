package io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.repository.ManagementAuditRepository
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.mock.domain.model.event.auditEventMock
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEvent
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.listing.AuditSortValues
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class GetAuditEventUseCaseTest {

    @Test
    fun invoke_returnsEvent_whenRepositorySucceeds() = runTest {
        val event = auditEventMock()
        val repository = object : ManagementAuditRepository {
            override suspend fun getAuditEvents(
                pageNumber: Int?,
                pageSize: Int?,
                sortBy: AuditSortValues.AuditEventSortBy?,
                sortOrder: SortOrder?
            ) = throw NotImplementedError()

            override suspend fun getAuditEvent(eventId: String) = AppResult.Success(event)
        }
        val useCase = GetAuditEventUseCase(repository)

        val result = useCase(event.id.value.toString())

        assertIs<AppResult.Success<*>>(result)
        assertEquals(event, result.data)
    }
}
