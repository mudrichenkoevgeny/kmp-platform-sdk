package io.github.mudrichenkoevgeny.kmp.feature.user.network.httpclient

import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.AccessToken
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.RefreshToken
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.network.auth.IsPublicApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.refreshtoken.RefreshTokenRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.token.SessionTokenResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.route.auth.refreshtoken.RefreshTokenRoutes
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlin.time.Clock

fun HttpClientConfig<*>.setupAuthConfig(
    baseUrl: String,
    authStorage: AuthStorage
) {
    install(Auth) {
        bearer {
            loadTokens {
                val now = Clock.System.now().toEpochMilliseconds()
                val expiresAt = authStorage.getExpiresAt()

                if (expiresAt <= now) {
                    return@loadTokens null
                }

                val accessToken = authStorage.getAccessToken()
                val refreshToken = authStorage.getRefreshToken()

                if (accessToken != null && refreshToken != null) {
                    BearerTokens(accessToken.value, refreshToken.value)
                } else {
                    null
                }
            }

            refreshTokens {
                val refreshToken = authStorage.getRefreshToken()
                    ?: return@refreshTokens null

                try {
                    val tokenResponse = client
                        .post("$baseUrl${RefreshTokenRoutes.REFRESH}") {
                            markAsRefreshTokenRequest()
                            setBody(RefreshTokenRequest(refreshToken.value))
                        }.body<SessionTokenResponse>()

                    authStorage.updateTokens(
                        accessToken = AccessToken(tokenResponse.accessToken),
                        refreshToken = RefreshToken(tokenResponse.refreshToken),
                        expiresAt = tokenResponse.expiresAt
                    )

                    BearerTokens(
                        accessToken = tokenResponse.accessToken,
                        refreshToken = tokenResponse.refreshToken
                    )
                } catch (e: Exception) {
                    authStorage.clear()
                    null
                }
            }

            sendWithoutRequest { request ->
                val isPublic = request.attributes.getOrNull(IsPublicApi) == true
                !isPublic
            }
        }
    }
}