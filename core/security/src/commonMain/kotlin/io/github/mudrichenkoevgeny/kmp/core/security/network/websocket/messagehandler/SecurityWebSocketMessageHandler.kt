package io.github.mudrichenkoevgeny.kmp.core.security.network.websocket.messagehandler

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandlerResult
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.websocket.SocketFrame
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.contract.SecurityWebSocketEventTypes

/**
 * [WebSocketMessageHandler] for security-domain frame types (see [SecurityWebSocketEventTypes]).
 *
 * Returns [WebSocketMessageHandlerResult.NotHandled] when the frame type is not recognized. For
 * `SECURITY_SETTINGS_UPDATED`, returns [WebSocketMessageHandlerResult.Handled]; persisting parsed data
 * is handled by the security settings repository that subscribes to [WebSocketService] events.
 */
class SecurityWebSocketMessageHandler() : WebSocketMessageHandler {
    override suspend fun handle(frame: SocketFrame): WebSocketMessageHandlerResult {
        return when (frame.type) {
            SecurityWebSocketEventTypes.SECURITY_SETTINGS_UPDATED -> handleSecuritySettingsUpdated()
            else -> WebSocketMessageHandlerResult.NotHandled
        }
    }

    private fun handleSecuritySettingsUpdated(): WebSocketMessageHandlerResult {
        // todo update settings
        return WebSocketMessageHandlerResult.Handled
    }
}