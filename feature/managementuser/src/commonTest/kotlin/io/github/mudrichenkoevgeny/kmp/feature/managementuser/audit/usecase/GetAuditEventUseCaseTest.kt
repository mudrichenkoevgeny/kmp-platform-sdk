package io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.mock.domain.model.event.auditEventMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.mock.repository.ManagementAuditRepositoryMock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class GetAuditEventUseCaseTest {

    @Test
    fun invoke_returnsEvent_whenRepositorySucceeds() = runTest {
        val event = auditEventMock()
        val repository = ManagementAuditRepositoryMock(getAuditEventResult = AppResult.Success(event))
        val useCase = GetAuditEventUseCase(repository)

        val result = useCase(event.id.value.toString())

        assertIs<AppResult.Success<*>>(result)
        assertEquals(event, result.data)
    }
}
