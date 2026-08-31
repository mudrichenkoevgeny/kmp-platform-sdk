package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.auth.google.GoogleAuthServiceMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.login.LoginRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth.AuthStorageMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.user.UserStorageMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.data.AuthData
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.AccessToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.RefreshToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.SessionToken
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.Instant

@InternalApi
class LoginByGoogleUseCaseTest {

    private val sessionToken = SessionToken(
        accessToken = AccessToken("access"),
        refreshToken = RefreshToken("refresh"),
        expiresAt = Instant.fromEpochMilliseconds(1000)
    )

    private val authDataMock = AuthData(
        sessionToken = sessionToken,
        userDetails = userDetailsMock()
    )

    @Test
    fun `should complete full flow on success`() = runTest {
        val authService = GoogleAuthServiceMock().apply {
            signInResultProvider = { AppResult.Success("google_token") }
        }
        val loginRepository = LoginRepositoryMock().apply {
            authDataResultProvider = { AppResult.Success(authDataMock) }
        }
        val authStorage = AuthStorageMock()
        val userStorage = UserStorageMock()
        val useCase = LoginByGoogleUseCase(authService, loginRepository, authStorage, userStorage)

        val result = useCase.execute()

        assertIs<AppResult.Success<AuthData>>(result)
        assertEquals(authDataMock, result.data)
        
        assertEquals(authDataMock.sessionToken.accessToken, authStorage.getAccessToken())
        assertEquals(authDataMock.userDetails, userStorage.getCurrentUser())
    }

    @Test
    fun `should abort when google auth fails`() = runTest {
        val error = CommonError.Unknown()
        val authService = GoogleAuthServiceMock().apply {
            signInResultProvider = { AppResult.Error(error) }
        }
        val loginRepository = LoginRepositoryMock()
        val authStorage = AuthStorageMock()
        val userStorage = UserStorageMock()
        val useCase = LoginByGoogleUseCase(authService, loginRepository, authStorage, userStorage)

        val result = useCase.execute()

        assertIs<AppResult.Error>(result)
        assertEquals(error, result.error)
        
        assertEquals(null, authStorage.getAccessToken())
    }

    @Test
    fun `should abort when repository exchange fails`() = runTest {
        val error = CommonError.Network(Exception("api_fail"))
        val authService = GoogleAuthServiceMock().apply {
            signInResultProvider = { AppResult.Success("google_token") }
        }
        val loginRepository = LoginRepositoryMock().apply {
            authDataResultProvider = { AppResult.Error(error) }
        }
        val authStorage = AuthStorageMock()
        val userStorage = UserStorageMock()
        val useCase = LoginByGoogleUseCase(authService, loginRepository, authStorage, userStorage)

        val result = useCase.execute()

        assertIs<AppResult.Error>(result)
        assertEquals(error, result.error)
        
        assertEquals(null, authStorage.getAccessToken())
    }
}
