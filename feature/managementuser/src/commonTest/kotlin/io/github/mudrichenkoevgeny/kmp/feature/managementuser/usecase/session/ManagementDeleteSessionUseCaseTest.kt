package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.session.ManagementSessionRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class ManagementDeleteSessionUseCaseTest {

    @Test
    fun invoke_returnsSuccess_whenRepositorySucceeds() = runTest {
        val repository = ManagementSessionRepositoryMock().apply {
            deleteSessionResultProvider = { _, _ -> AppResult.Success(Unit) }
        }
        val useCase = ManagementDeleteSessionUseCase(repository)
        val userId = UserId.generate()
        val sessionId = "session_id"

        val result = useCase(userId, sessionId)

        assertIs<AppResult.Success<Unit>>(result)
        assertEquals(userId, repository.lastUserId)
        assertEquals(sessionId, repository.lastSessionId)
    }

    @Test
    fun invoke_returnsError_whenRepositoryFails() = runTest {
        val expectedError = CommonError.Unknown()
        val repository = ManagementSessionRepositoryMock().apply {
            deleteSessionResultProvider = { _, _ -> AppResult.Error(expectedError) }
        }
        val useCase = ManagementDeleteSessionUseCase(repository)
        val userId = UserId.generate()
        val sessionId = "session_id"

        val result = useCase(userId, sessionId)

        assertIs<AppResult.Error>(result)
        assertEquals(expectedError, result.error)
    }
}
