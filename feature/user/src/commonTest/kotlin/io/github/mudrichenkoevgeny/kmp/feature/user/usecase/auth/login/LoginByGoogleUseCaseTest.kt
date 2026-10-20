package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.auth.google.GoogleAuthServiceMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.login.LoginRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth.AuthStorageMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.user.UserStorageMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.data.AuthData
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.AccessToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.RefreshToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.SessionToken
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.user.createTestUserPrivate
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
        expiresAt = Instant.fromEpochMilliseconds(1000),
        sessionId = UserSessionId.generate(),
        identifierId = UserIdentifierId.generate()
    )

    private val authDataMock = AuthData(
        sessionToken = sessionToken,
        userPrivate = createTestUserPrivate()
    )

    @Test
    fun `should complete full flow on success`() = runTest {
        val googleAuthService = GoogleAuthServiceMock().apply {
            signInResultProvider = { AppResult.Success("id_token") }
        }
        val loginRepository = LoginRepositoryMock().apply {
            authDataResultProvider = { AppResult.Success(authDataMock) }
        }
        val userStorage = UserStorageMock()
        val authStorage = AuthStorageMock()

        val useCase = LoginByGoogleUseCase(
            authService = googleAuthService,
            loginRepository = loginRepository,
            authStorage = authStorage,
            userStorage = userStorage
        )

        val result = useCase.execute()

        assertIs<AppResult.Success<AuthData>>(result)
        assertEquals(authDataMock, result.data)
        assertEquals(authDataMock.userPrivate, userStorage.getCurrentUser())
    }

    @Test
    fun `should fail if idToken fetch fails`() = runTest {
        val error = CommonError.Unknown()
        val googleAuthService = GoogleAuthServiceMock().apply {
            signInResultProvider = { AppResult.Error(error) }
        }
        val loginRepository = LoginRepositoryMock()
        val userStorage = UserStorageMock()
        val authStorage = AuthStorageMock()

        val useCase = LoginByGoogleUseCase(
            authService = googleAuthService,
            loginRepository = loginRepository,
            authStorage = authStorage,
            userStorage = userStorage
        )

        val result = useCase.execute()

        assertIs<AppResult.Error>(result)
        assertEquals(error, result.error)
    }

    @Test
    fun `should fail if repository login fails`() = runTest {
        val error = CommonError.Unknown()
        val googleAuthService = GoogleAuthServiceMock().apply {
            signInResultProvider = { AppResult.Success("id_token") }
        }
        val loginRepository = LoginRepositoryMock().apply {
            authDataResultProvider = { AppResult.Error(error) }
        }
        val userStorage = UserStorageMock()
        val authStorage = AuthStorageMock()

        val useCase = LoginByGoogleUseCase(
            authService = googleAuthService,
            loginRepository = loginRepository,
            authStorage = authStorage,
            userStorage = userStorage
        )

        val result = useCase.execute()

        assertIs<AppResult.Error>(result)
        assertEquals(error, result.error)
    }
}
