package io.github.mudrichenkoevgeny.kmp.feature.user.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.login.KtorLoginApi
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.password.KtorPasswordApi
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.refreshtoken.KtorRefreshTokenApi
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.registration.KtorRegistrationApi
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.settings.KtorAuthSettingsApi
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.configuration.KtorUserConfigurationApi
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.security.password.KtorSecurityPasswordApi
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.security.userIdentifiers.KtorSecurityUserIdentifiersApi
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.session.KtorSessionApi
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.user.KtorUserApi
import io.github.mudrichenkoevgeny.kmp.feature.user.network.websocket.messagehandler.UserWebSocketMessageHandler
import io.ktor.client.HttpClient
import kotlin.getValue

/**
 * Lazily constructs Ktor API clients and the user WebSocket message handler from a shared [HttpClient].
 *
 * @param httpClient The host-configured Ktor client (typically from `CommonComponent`).
 */
internal class UserNetworkModule(
    private val httpClient: HttpClient
) {
    // Auth
    val loginApi by lazy { KtorLoginApi(httpClient) }
    val registrationApi by lazy { KtorRegistrationApi(httpClient) }
    val refreshTokenApi by lazy { KtorRefreshTokenApi(httpClient) }
    val passwordApi by lazy { KtorPasswordApi(httpClient) }
    val authSettingsApi by lazy { KtorAuthSettingsApi(httpClient) }

    // Security
    val securityPasswordApi by lazy { KtorSecurityPasswordApi(httpClient) }
    val securityUserIdentifiersApi by lazy { KtorSecurityUserIdentifiersApi(httpClient) }

    // Session
    val sessionApi by lazy { KtorSessionApi(httpClient) }

    // User
    val userApi by lazy { KtorUserApi(httpClient) }

    // Configuration
    val userConfigurationApi by lazy { KtorUserConfigurationApi(httpClient) }

    val userWebSocketMessageHandler: WebSocketMessageHandler by lazy {
        UserWebSocketMessageHandler()
    }
}