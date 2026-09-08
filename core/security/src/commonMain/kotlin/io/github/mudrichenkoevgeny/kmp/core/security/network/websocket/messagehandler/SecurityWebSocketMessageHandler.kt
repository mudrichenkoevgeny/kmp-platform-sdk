package io.github.mudrichenkoevgeny.kmp.core.security.network.websocket.messagehandler

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandlerResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.websocket.SocketFrame
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.contract.SecurityWebSocketEventTypes

/**
 * [WebSocketMessageHandler] for security-domain frame types (see [SecurityWebSocketEventTypes]).
 */
class SecurityWebSocketMessageHandler : WebSocketMessageHandler {
    override suspend fun handle(frame: SocketFrame): WebSocketMessageHandlerResult {
        return WebSocketMessageHandlerResult.NotHandled
    }
}