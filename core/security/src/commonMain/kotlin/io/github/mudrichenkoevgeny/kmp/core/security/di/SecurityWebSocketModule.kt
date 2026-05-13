package io.github.mudrichenkoevgeny.kmp.core.security.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.security.network.securitysettings.SecuritySettingsApi
import io.github.mudrichenkoevgeny.kmp.core.security.network.websocket.messagehandler.SecurityWebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.security.repository.SecuritySettingsRepository
import kotlinx.coroutines.CoroutineScope

/**
 * Internal network wiring for `core/security`.
 *
 * Provides the Ktor-backed [SecuritySettingsApi] and the [SecurityWebSocketMessageHandler] for host
 * registration alongside other [WebSocketMessageHandler] instances.
 */
internal class SecurityWebSocketModule(
    securitySettingsRepository: SecuritySettingsRepository,
    scope: CoroutineScope
) {
    /**
     * Handler offered to the shared WebSocket pipeline for security-related frame types.
     */
    val securityWebSocketMessageHandler: WebSocketMessageHandler by lazy {
        SecurityWebSocketMessageHandler(
            securitySettingsRepository = securitySettingsRepository,
            scope = scope
        )
    }
}