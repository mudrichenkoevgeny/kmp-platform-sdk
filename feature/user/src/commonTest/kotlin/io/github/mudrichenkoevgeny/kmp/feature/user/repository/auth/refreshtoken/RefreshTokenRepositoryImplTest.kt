package io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.refreshtoken

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.AccessToken
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.RefreshToken
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.SessionToken
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.refreshtoken.RefreshTokenApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.wireSessionTokenResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.refreshtoken.RefreshTokenRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.token.SessionTokenResponse
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame

class RefreshTokenRepositoryImplTest {

    @Test
    fun refreshToken_forwardsWrappedRequest_andMapsSuccess() = runTest {
        val wire = wireSessionTokenResponse(
            accessToken = ACCESS_TOKEN,
            refreshToken = REFRESH_TOKEN,
            expiresAt = EXPIRES_AT_MS,
            tokenType = TOKEN_TYPE_BEARER
        )
        val api = FakeRefreshTokenApi().apply { result = AppResult.Success(wire) }
        val repo = RefreshTokenRepositoryImpl(api)

        val refreshResult = repo.refreshToken(SECRET_REFRESH_TOKEN)

        val success = assertIs<AppResult.Success<SessionToken>>(refreshResult)
        assertEquals(AccessToken(ACCESS_TOKEN), success.data.accessToken)
        assertEquals(RefreshToken(REFRESH_TOKEN), success.data.refreshToken)
        assertEquals(EXPIRES_AT_MS, success.data.expiresAt)
        assertEquals(TOKEN_TYPE_BEARER, success.data.tokenType)
        assertEquals(SECRET_REFRESH_TOKEN, api.lastRequest.refreshToken)
    }

    @Test
    fun refreshToken_propagatesApiError() = runTest {
        val err = CommonError.Unknown(isRetryable = NOT_RETRYABLE)
        val api = FakeRefreshTokenApi().apply { result = AppResult.Error(err) }
        val repo = RefreshTokenRepositoryImpl(api)

        val refreshResult = repo.refreshToken(ERROR_CASE_REFRESH_TOKEN)

        val failure = assertIs<AppResult.Error>(refreshResult)
        assertSame(err, failure.error)
    }

    private class FakeRefreshTokenApi : RefreshTokenApi {
        lateinit var lastRequest: RefreshTokenRequest
        var result: AppResult<SessionTokenResponse> = AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))

        override suspend fun refreshToken(request: RefreshTokenRequest): AppResult<SessionTokenResponse> {
            lastRequest = request
            return result
        }
    }

    private companion object {
        private const val ACCESS_TOKEN = "a1"
        private const val REFRESH_TOKEN = "r1"
        private const val EXPIRES_AT_MS = 500L
        private const val TOKEN_TYPE_BEARER = "Bearer"
        private const val SECRET_REFRESH_TOKEN = "secret-refresh"
        private const val ERROR_CASE_REFRESH_TOKEN = "x"
        private const val NOT_RETRYABLE = false
    }
}
