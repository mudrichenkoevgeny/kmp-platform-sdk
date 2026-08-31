package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.session.SessionRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class DeleteSessionUseCaseTest {

    @Test
    fun invoke_returnsSuccess_whenRepositorySucceeds() = runTest {
        val repository = SessionRepositoryMock().apply {
            deleteSessionResultProvider = { AppResult.Success(Unit) }
        }
        val useCase = DeleteSessionUseCase(repository)
        val sessionId = UserSessionId.generate()

        val result = useCase(sessionId)

        assertIs<AppResult.Success<Unit>>(result)
        assertEquals(sessionId, repository.lastSessionId)
    }

    @Test
    fun invoke_returnsError_whenRepositoryFails() = runTest {
        val expectedError = CommonError.Unknown()
        val repository = SessionRepositoryMock().apply {
            deleteSessionResultProvider = { AppResult.Error(expectedError) }
        }
        val useCase = DeleteSessionUseCase(repository)
        val sessionId = UserSessionId.generate()

        val result = useCase(sessionId)

        assertIs<AppResult.Error>(result)
        assertEquals(expectedError, result.error)
    }
}
