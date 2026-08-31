package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.session.ManagementSessionRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.session.userSessionMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class ManagementGetSessionUseCaseTest {

    @Test
    fun invoke_returnsSession_whenRepositorySucceeds() = runTest {
        val expectedSession = userSessionMock()
        val repository = ManagementSessionRepositoryMock().apply {
            getSessionResultProvider = { AppResult.Success(expectedSession) }
        }
        val useCase = ManagementGetSessionUseCase(repository)
        val sessionId = "session_id"

        val result = useCase(sessionId)

        assertIs<AppResult.Success<UserSession>>(result)
        assertEquals(expectedSession, result.data)
        assertEquals(sessionId, repository.lastSessionId)
    }

    @Test
    fun invoke_returnsError_whenRepositoryFails() = runTest {
        val expectedError = CommonError.Unknown()
        val repository = ManagementSessionRepositoryMock().apply {
            getSessionResultProvider = { AppResult.Error(expectedError) }
        }
        val useCase = ManagementGetSessionUseCase(repository)
        val sessionId = "session_id"

        val result = useCase(sessionId)

        assertIs<AppResult.Error>(result)
        assertEquals(expectedError, result.error)
    }
}
