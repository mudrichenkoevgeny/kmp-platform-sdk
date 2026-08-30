package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.refreshtoken

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.api.auth.refreshtoken.RefreshTokenApiMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.token.sessionTokenPayloadMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.AccessToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.RefreshToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.SessionToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.token.RefreshTokenPayload
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame

@InternalApi
class SelfManagementRefreshTokenRepositoryImplTest {

    private val api = RefreshTokenApiMock()
    private val repository = SelfManagementRefreshTokenRepositoryImpl(api)

    @Test
    fun `refreshToken forwards request and maps success`() = runTest {
        val wire = sessionTokenPayloadMock(
            accessToken = ACCESS_TOKEN,
            refreshToken = REFRESH_TOKEN,
            expiresAt = EXPIRES_AT_MS,
            tokenType = TOKEN_TYPE_BEARER
        )
        api.refreshTokenResult = AppResult.Success(wire)

        val result = repository.refreshToken(SECRET_REFRESH_TOKEN)

        val success = assertIs<AppResult.Success<SessionToken>>(result)
        assertEquals(AccessToken(ACCESS_TOKEN), success.data.accessToken)
        assertEquals(RefreshToken(REFRESH_TOKEN), success.data.refreshToken)
        assertEquals(EXPIRES_AT_MS, success.data.expiresAt.toEpochMilliseconds())
        assertEquals(TOKEN_TYPE_BEARER, success.data.tokenType)
        assertEquals(RefreshTokenPayload(SECRET_REFRESH_TOKEN), api.lastRefreshTokenRequest)
    }

    @Test
    fun `refreshToken propagates api error`() = runTest {
        val err = CommonError.Unknown()
        api.refreshTokenResult = AppResult.Error(err)

        val result = repository.refreshToken(SECRET_REFRESH_TOKEN)

        val failure = assertIs<AppResult.Error>(result)
        assertSame(err, failure.error)
    }

    private companion object {
        private const val ACCESS_TOKEN = "a1"
        private const val REFRESH_TOKEN = "r1"
        private const val EXPIRES_AT_MS = 500L
        private const val TOKEN_TYPE_BEARER = "Bearer"
        private const val SECRET_REFRESH_TOKEN = "secret-refresh"
    }
}