package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.session.userSessionMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.session.SessionRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class GetSessionUseCaseTest {

    @Test
    fun invoke_returnsUserSession_whenRepositorySucceeds() = runTest {
        val expectedSession = userSessionMock()
        val repository = SessionRepositoryMock().apply {
            getSessionResultProvider = { AppResult.Success(expectedSession) }
        }
        val useCase = GetSessionUseCase(repository)
        val sessionId = UserSessionId.generate()

        val result = useCase(sessionId)

        assertIs<AppResult.Success<UserSession>>(result)
        assertEquals(expectedSession, result.data)
        assertEquals(sessionId, repository.lastSessionId)
    }

    @Test
    fun invoke_returnsError_whenRepositoryFails() = runTest {
        val expectedError = CommonError.Unknown()
        val repository = SessionRepositoryMock().apply {
            getSessionResultProvider = { AppResult.Error(expectedError) }
        }
        val useCase = GetSessionUseCase(repository)
        val sessionId = UserSessionId.generate()

        val result = useCase(sessionId)

        assertIs<AppResult.Error>(result)
        assertEquals(expectedError, result.error)
    }
}
