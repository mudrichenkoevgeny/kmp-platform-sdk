package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.refreshtoken

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.token.sessionTokenPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.refreshtoken.RefreshTokenRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth.AuthStorageMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.AccessToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.RefreshToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.SessionToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.token.toSessionToken
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.Instant

@InternalApi
class RefreshTokenUseCaseTest {

    @Test
    fun execute_returnsInvalidRefreshToken_whenNoRefreshTokenStored() = runTest {
        val useCase = RefreshTokenUseCase(
            refreshTokenRepository = RefreshTokenRepositoryMock(),
            authStorage = AuthStorageMock()
        )

        val refreshResult = useCase()

        val err = assertIs<AppResult.Error>(refreshResult)
        assertIs<UserError.InvalidRefreshToken>(err.error)
    }

    @Test
    fun execute_updatesStorage_whenRefreshSucceeds() = runTest {
        val authStorage = AuthStorageMock()
        authStorage.updateTokens(
            AccessToken(OLD_ACCESS_TOKEN),
            RefreshToken(STORED_REFRESH_TOKEN),
            expiresAt = Instant.fromEpochMilliseconds(EXPIRES_AT_BEFORE_REFRESH)
        )
        val wire = sessionTokenPayloadMock(
            accessToken = NEW_ACCESS_TOKEN,
            refreshToken = NEW_REFRESH_TOKEN,
            expiresAt = EXPIRES_AT_AFTER_REFRESH,
            tokenType = TOKEN_TYPE_BEARER
        )
        val expectedSession = wire.toSessionToken()
        val refreshRepo = RefreshTokenRepositoryMock().apply {
            refreshTokenResultProvider = { AppResult.Success(expectedSession) }
        }
        val useCase = RefreshTokenUseCase(refreshRepo, authStorage)

        val refreshResult = useCase()

        val success = assertIs<AppResult.Success<SessionToken>>(refreshResult)
        assertEquals(expectedSession.accessToken, success.data.accessToken)
        assertEquals(STORED_REFRESH_TOKEN, refreshRepo.lastRefreshToken)
        assertEquals(AccessToken(NEW_ACCESS_TOKEN), authStorage.getAccessToken())
        assertEquals(RefreshToken(NEW_REFRESH_TOKEN), authStorage.getRefreshToken())
        assertEquals(EXPIRES_AT_AFTER_REFRESH, authStorage.getExpiresAt())
    }

    private companion object {
        private const val OLD_ACCESS_TOKEN = "old-access"
        private const val STORED_REFRESH_TOKEN = "stored-refresh"
        private const val EXPIRES_AT_BEFORE_REFRESH = 1L
        private const val NEW_ACCESS_TOKEN = "new-access"
        private const val NEW_REFRESH_TOKEN = "new-refresh"
        private const val EXPIRES_AT_AFTER_REFRESH = 200L
        private const val TOKEN_TYPE_BEARER = "Bearer"
    }
}
