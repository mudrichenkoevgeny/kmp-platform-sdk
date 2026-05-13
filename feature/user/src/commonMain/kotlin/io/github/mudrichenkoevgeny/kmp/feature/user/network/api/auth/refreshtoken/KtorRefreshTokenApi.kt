package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.refreshtoken

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.network.auth.markAsPublic
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.token.RefreshTokenPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.token.SessionTokenPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.open.auth.refreshtoken.OpenRefreshTokenRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

/** [RefreshTokenApi] backed by [HttpClient]; the refresh route uses [markAsPublic]. */
class KtorRefreshTokenApi(
    private val client: HttpClient
) : RefreshTokenApi {
    override suspend fun refreshToken(
        request: RefreshTokenPayload
    ): AppResult<SessionTokenPayload> = client.callResult {
        post(OpenRefreshTokenRoutes.REFRESH_TOKEN) {
            markAsPublic()
            setBody(request)
        }
    }
}