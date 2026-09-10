package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.session.SessionRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.user.UserRepositoryMock
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class LogoutUseCaseTest {

    @Test
    fun invoke_clearsSession_whenLogoutSucceeds() = runTest {
        val repository = SessionRepositoryMock().apply {
            logoutResultProvider = { AppResult.Success(Unit) }
        }
        val userRepository = UserRepositoryMock()
        val useCase = LogoutUseCase(repository, userRepository)

        val result = useCase()

        assertIs<AppResult.Success<Unit>>(result)
        assertEquals(null, userRepository.currentUser.first())
    }

    @Test
    fun invoke_clearsSessionAndReturnsSuccess_evenWhenLogoutFails() = runTest {
        val expectedError = CommonError.Unknown()
        val repository = SessionRepositoryMock().apply {
            logoutResultProvider = { AppResult.Error(expectedError) }
        }
        val userRepository = UserRepositoryMock()
        val useCase = LogoutUseCase(repository, userRepository)

        val result = useCase()

        assertIs<AppResult.Success<Unit>>(result)
        assertEquals(null, userRepository.currentUser.first())
    }
}
