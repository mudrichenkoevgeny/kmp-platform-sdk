package io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler

import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.websocket.SocketFrame

/**
 * Pluggable handler for incoming WebSocket frames.
 *
 * The socket service offers each frame to registered handlers until one returns a result other than
 * [WebSocketMessageHandlerResult.NotHandled].
 */
interface WebSocketMessageHandler {
    /**
     * @param frame Incoming frame from the server.
     * @return [WebSocketMessageHandlerResult.NotHandled] if this handler does not apply; otherwise
     * [WebSocketMessageHandlerResult.Handled], [WebSocketMessageHandlerResult.SendSocketFrame], or
     * [WebSocketMessageHandlerResult.Error] as appropriate.
     */
    suspend fun handle(frame: SocketFrame): WebSocketMessageHandlerResult
}