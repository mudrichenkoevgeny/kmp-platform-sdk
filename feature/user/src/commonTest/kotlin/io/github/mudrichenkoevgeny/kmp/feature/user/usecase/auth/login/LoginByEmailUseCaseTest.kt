package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.auth.data.authDataPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.login.LoginRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth.AuthStorageMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.user.UserStorageMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.data.AuthData
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.auth.data.toAuthData
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

@InternalApi
class LoginByEmailUseCaseTest {

    @Test
    fun execute_persistsTokensAndUser_whenLoginSucceeds() = runTest {
        val authData = authDataPayloadMock().toAuthData()
        val loginRepository = LoginRepositoryMock().apply {
            authDataResultProvider = { AppResult.Success(authData) }
        }
        val authStorage = AuthStorageMock()
        val userStorage = UserStorageMock()
        val useCase = LoginByEmailUseCase(loginRepository, authStorage, userStorage)

        val loginResult = useCase.execute(TEST_EMAIL, TEST_PASSWORD)

        assertIs<AppResult.Success<AuthData>>(loginResult)
        assertEquals(TEST_EMAIL, loginRepository.lastEmail)
        assertEquals(TEST_PASSWORD, loginRepository.lastPassword)
        assertEquals(authData.sessionToken.accessToken, authStorage.getAccessToken())
        assertEquals(authData.sessionToken.refreshToken, authStorage.getRefreshToken())
        assertEquals<Long?>(authData.sessionToken.expiresAt.toEpochMilliseconds(), authStorage.getExpiresAt())
        assertEquals(authData.userDetails, userStorage.getCurrentUser())
    }

    @Test
    fun execute_doesNotPersist_whenLoginFails() = runTest {
        val err = CommonError.Unknown(isRetryable = false)
        val loginRepository = LoginRepositoryMock().apply {
            authDataResultProvider = { AppResult.Error(err) }
        }
        val authStorage = AuthStorageMock()
        val userStorage = UserStorageMock()
        val useCase = LoginByEmailUseCase(loginRepository, authStorage, userStorage)

        val loginResult = useCase.execute(TEST_EMAIL, TEST_PASSWORD)

        assertIs<AppResult.Error>(loginResult)
        assertNull(authStorage.getAccessToken())
        assertNull(authStorage.getRefreshToken())
        assertNull(userStorage.getCurrentUser())
    }

    private companion object {
        private const val TEST_EMAIL = "a@b.c"
        private const val TEST_PASSWORD = "pw"
    }
}