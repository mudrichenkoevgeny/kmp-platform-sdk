package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.refreshtoken

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.network.auth.markAsPublic
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.refreshtoken.RefreshTokenRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.token.SessionTokenResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.auth.refreshtoken.RefreshTokenRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class KtorRefreshTokenApi(
    private val client: HttpClient
) : RefreshTokenApi {
    override suspend fun refreshToken(
        request: RefreshTokenRequest
    ): AppResult<SessionTokenResponse> = client.callResult {
        post(RefreshTokenRoutes.REFRESH) {
            markAsPublic()
            setBody(request)
        }
    }
}