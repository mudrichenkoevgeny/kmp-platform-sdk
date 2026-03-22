package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.refreshtoken

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth.MockAuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.AccessToken
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.RefreshToken
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.SessionToken
import io.github.mudrichenkoevgeny.kmp.feature.user.mapper.token.toSessionToken
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.refreshtoken.RefreshTokenRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.wireSessionTokenResponse
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class RefreshTokenUseCaseTest {

    @Test
    fun execute_returnsInvalidRefreshToken_whenNoRefreshTokenStored() = runTest {
        val useCase = RefreshTokenUseCase(
            refreshTokenRepository = FakeRefreshTokenRepository(),
            authStorage = MockAuthStorage()
        )

        val refreshResult = useCase.execute()

        val err = assertIs<AppResult.Error>(refreshResult)
        assertIs<UserError.InvalidRefreshToken>(err.error)
    }

    @Test
    fun execute_updatesStorage_whenRefreshSucceeds() = runTest {
        val authStorage = MockAuthStorage()
        authStorage.updateTokens(
            AccessToken(OLD_ACCESS_TOKEN),
            RefreshToken(STORED_REFRESH_TOKEN),
            expiresAt = EXPIRES_AT_BEFORE_REFRESH
        )
        val wire = wireSessionTokenResponse(
            accessToken = NEW_ACCESS_TOKEN,
            refreshToken = NEW_REFRESH_TOKEN,
            expiresAt = EXPIRES_AT_AFTER_REFRESH,
            tokenType = TOKEN_TYPE_BEARER
        )
        val expectedSession = wire.toSessionToken()
        val refreshRepo = FakeRefreshTokenRepository().apply {
            result = AppResult.Success(wire.toSessionToken())
        }
        val useCase = RefreshTokenUseCase(refreshRepo, authStorage)

        val refreshResult = useCase.execute()

        val success = assertIs<AppResult.Success<SessionToken>>(refreshResult)
        assertEquals(expectedSession.accessToken, success.data.accessToken)
        assertEquals(STORED_REFRESH_TOKEN, refreshRepo.lastRefreshTokenPassed)
        assertEquals(AccessToken(NEW_ACCESS_TOKEN), authStorage.getAccessToken())
        assertEquals(RefreshToken(NEW_REFRESH_TOKEN), authStorage.getRefreshToken())
        assertEquals(EXPIRES_AT_AFTER_REFRESH, authStorage.getExpiresAt())
    }

    private class FakeRefreshTokenRepository : RefreshTokenRepository {
        var result: AppResult<SessionToken> = AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
        var lastRefreshTokenPassed: String? = null

        override suspend fun refreshToken(refreshToken: String): AppResult<SessionToken> {
            lastRefreshTokenPassed = refreshToken
            return result
        }
    }

    private companion object {
        private const val OLD_ACCESS_TOKEN = "old-access"
        private const val STORED_REFRESH_TOKEN = "stored-refresh"
        private const val EXPIRES_AT_BEFORE_REFRESH = 1L
        private const val NEW_ACCESS_TOKEN = "new-access"
        private const val NEW_REFRESH_TOKEN = "new-refresh"
        private const val EXPIRES_AT_AFTER_REFRESH = 200L
        private const val TOKEN_TYPE_BEARER = "Bearer"
        private const val NOT_RETRYABLE = false
    }
}
