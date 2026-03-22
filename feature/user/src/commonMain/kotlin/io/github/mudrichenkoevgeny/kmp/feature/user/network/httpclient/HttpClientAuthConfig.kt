package io.github.mudrichenkoevgeny.kmp.feature.user.network.httpclient

import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.AccessToken
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.RefreshToken
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.network.auth.IsPublicApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.refreshtoken.RefreshTokenRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.token.SessionTokenResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.auth.refreshtoken.RefreshTokenRoutes
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlin.time.Clock

private const val LOGGER_AUTH_PREFIX = "Auth"

/**
 * Installs Ktor `Auth` with bearer token loading, refresh, and conditional header attachment.
 *
 * [AuthStorage] supplies access and refresh tokens. Requests marked with [IsPublicApi] skip the
 * `Authorization` header. The refresh flow posts to the refresh route, updates storage on success,
 * and clears tokens when refresh fails.
 *
 * @param baseUrl API origin prepended to refresh path constants from shared routes.
 * @param networkLogger Logger used for auth lifecycle messages.
 * @param authStorage Persistent token and expiry source.
 */
fun HttpClientConfig<*>.setupAuthConfig(
    baseUrl: String,
    networkLogger: Logger,
    authStorage: AuthStorage
) {
    install(Auth) {
        bearer {
            loadTokens {
                val now = Clock.System.now().toEpochMilliseconds()
                val expiresAt = authStorage.getExpiresAt()

                if (expiresAt <= now) {
                    networkLogger.log("$LOGGER_AUTH_PREFIX: Access token expired or not found")
                    return@loadTokens null
                }

                val accessToken = authStorage.getAccessToken()
                val refreshToken = authStorage.getRefreshToken()

                if (accessToken != null && refreshToken != null) {
                    BearerTokens(accessToken.value, refreshToken.value)
                } else {
                    networkLogger.log("$LOGGER_AUTH_PREFIX: Missing token components in storage")
                    null
                }
            }

            refreshTokens {
                val refreshToken = authStorage.getRefreshToken()
                    ?: run {
                        networkLogger.log("$LOGGER_AUTH_PREFIX: Refresh token not found in storage")
                        return@refreshTokens null
                    }

                networkLogger.log("$LOGGER_AUTH_PREFIX: Refreshing tokens...")

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

                    networkLogger.log("$LOGGER_AUTH_PREFIX: Tokens successfully refreshed")

                    BearerTokens(
                        accessToken = tokenResponse.accessToken,
                        refreshToken = tokenResponse.refreshToken
                    )
                } catch (e: Exception) {
                    networkLogger.log("$LOGGER_AUTH_PREFIX: Token refresh failed: ${e.message}")
                    authStorage.clearTokens()
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