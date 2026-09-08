package io.github.mudrichenkoevgeny.kmp.core.security.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.security.network.securitysettings.OpenSecuritySettingsApi
import io.github.mudrichenkoevgeny.kmp.core.security.network.websocket.messagehandler.SecurityWebSocketMessageHandler

/**
 * Internal network wiring for `core/security`.
 *
 * Provides the Ktor-backed [OpenSecuritySettingsApi] and the [SecurityWebSocketMessageHandler] for host
 * registration alongside other [WebSocketMessageHandler] instances.
 */
internal class SecurityWebSocketModule {
    /**
     * Handler offered to the shared WebSocket pipeline for security-related frame types.
     */
    val securityWebSocketMessageHandler: WebSocketMessageHandler by lazy {
        SecurityWebSocketMessageHandler()
    }
}