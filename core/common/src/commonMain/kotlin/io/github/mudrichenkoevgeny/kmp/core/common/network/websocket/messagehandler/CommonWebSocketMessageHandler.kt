package io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler

import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.contract.CommonWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.websocket.SocketFrame
import kotlin.time.Clock
import kotlin.uuid.Uuid

/**
 * Default SDK websocket handler for framework-level events:
 * - ping/pong keep-alive
 * - initialize / initialized success flow (contract-specific)
 *
 * Handlers can be replaced/extended by app/feature-level [WebSocketMessageHandler] implementations
 * registered in `WebSocketService.updateWebSocketMessageHandlers`.
 */
class CommonWebSocketMessageHandler() : WebSocketMessageHandler {
    /**
     * Handles a received websocket [SocketFrame] and returns the decision/result.
     *
     * For unknown frames it returns [WebSocketMessageHandlerResult.NotHandled]
     * so that the service can route it as an unhandled event.
     */
    override suspend fun handle(frame: SocketFrame): WebSocketMessageHandlerResult {
        return when (frame.type) {
            CommonWebSocketEventTypes.PING -> handlePing()
            CommonWebSocketEventTypes.PONG -> WebSocketMessageHandlerResult.Handled
            CommonWebSocketEventTypes.INITIALIZE -> WebSocketMessageHandlerResult.Handled
            CommonWebSocketEventTypes.INITIALIZED_SUCCESS -> WebSocketMessageHandlerResult.Handled
            else -> WebSocketMessageHandlerResult.NotHandled
        }
    }

    private fun handlePing(): WebSocketMessageHandlerResult {
        return WebSocketMessageHandlerResult.SendSocketFrame(
            SocketFrame(
                id = Uuid.random().toHexDashString(),
                type = CommonWebSocketEventTypes.PONG,
                timestamp = Clock.System.now().toEpochMilliseconds()
            )
        )
    }
}