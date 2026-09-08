package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.session.SessionRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth.AuthStorageMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.user.UserStorageMock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue

@InternalApi
class LogoutUseCaseTest {

    @Test
    fun invoke_clearsStorage_whenLogoutSucceeds() = runTest {
        val repository = SessionRepositoryMock().apply {
            logoutResultProvider = { AppResult.Success(Unit) }
        }
        val authStorage = AuthStorageMock()
        val userStorage = UserStorageMock()
        val useCase = LogoutUseCase(repository, authStorage, userStorage)

        val result = useCase()

        assertIs<AppResult.Success<Unit>>(result)
        assertTrue(authStorage.isTokensCleared)
        assertTrue(userStorage.isCleared)
    }

    @Test
    fun invoke_clearsStorageAndReturnsSuccess_evenWhenLogoutFails() = runTest {
        val expectedError = CommonError.Unknown()
        val repository = SessionRepositoryMock().apply {
            logoutResultProvider = { AppResult.Error(expectedError) }
        }
        val authStorage = AuthStorageMock()
        val userStorage = UserStorageMock()
        val useCase = LogoutUseCase(repository, authStorage, userStorage)

        val result = useCase()

        assertIs<AppResult.Success<Unit>>(result)
        assertTrue(authStorage.isTokensCleared)
        assertTrue(userStorage.isCleared)
    }
}
