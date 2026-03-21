package io.github.mudrichenkoevgeny.kmp.core.security.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.security.network.api.securitysettings.KtorSecuritySettingsApi
import io.github.mudrichenkoevgeny.kmp.core.security.network.websocket.messagehandler.SecurityWebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.security.network.api.securitysettings.SecuritySettingsApi
import io.ktor.client.HttpClient

/**
 * Internal network wiring for `core/security`.
 *
 * Provides the Ktor-backed [SecuritySettingsApi] and the [SecurityWebSocketMessageHandler] for host
 * registration alongside other [WebSocketMessageHandler] instances.
 */
internal class SecurityNetworkModule(httpClient: HttpClient) {
    /**
     * Ktor-backed implementation of [SecuritySettingsApi].
     */
    val securitySettingsApi by lazy {
        KtorSecuritySettingsApi(
            httpClient
        )
    }

    /**
     * Handler offered to the shared WebSocket pipeline for security-related frame types.
     */
    val securityWebSocketMessageHandler: WebSocketMessageHandler by lazy {
        SecurityWebSocketMessageHandler()
    }
}