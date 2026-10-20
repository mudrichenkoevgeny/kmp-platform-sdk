package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.audit

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.audit.domain.model.event.createTestAuditEventSummary
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.common.domain.model.listing.createTestPagedResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.audit.repository.ManagementAuditRepositoryMock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class GetAuditEventsUseCaseTest {

    @Test
    fun invoke_returnsPagedResult_whenRepositorySucceeds() = runTest {
        val event = createTestAuditEventSummary()
        val expectedResult = createTestPagedResult(listOf(event))
        val repository = ManagementAuditRepositoryMock(getAuditEventsResult = AppResult.Success(expectedResult))
        val useCase = GetAuditEventsUseCase(repository)

        val result = useCase()

        assertIs<AppResult.Success<*>>(result)
        assertEquals(expectedResult, result.data)
    }
}