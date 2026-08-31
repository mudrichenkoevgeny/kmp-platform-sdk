package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.session.SessionRepositoryMock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class ReauthenticateSessionUseCaseTest {

    @Test
    fun invoke_returnsSuccess_whenRepositorySucceeds() = runTest {
        val repository = SessionRepositoryMock().apply {
            reauthenticateSessionResultProvider = { _, _ -> AppResult.Success(Unit) }
        }
        val useCase = ReauthenticateSessionUseCase(repository)

        val result = useCase(TEST_MFA_TOKEN, TEST_CODE)

        assertIs<AppResult.Success<Unit>>(result)
        assertEquals(TEST_MFA_TOKEN, repository.lastMfaToken)
        assertEquals(TEST_CODE, repository.lastCode)
    }

    @Test
    fun invoke_returnsError_whenRepositoryFails() = runTest {
        val expectedError = CommonError.Unknown()
        val repository = SessionRepositoryMock().apply {
            reauthenticateSessionResultProvider = { _, _ -> AppResult.Error(expectedError) }
        }
        val useCase = ReauthenticateSessionUseCase(repository)

        val result = useCase(TEST_MFA_TOKEN, TEST_CODE)

        assertIs<AppResult.Error>(result)
        assertEquals(expectedError, result.error)
    }

    private companion object {
        private const val TEST_MFA_TOKEN = "token"
        private const val TEST_CODE = "123456"
    }
}
