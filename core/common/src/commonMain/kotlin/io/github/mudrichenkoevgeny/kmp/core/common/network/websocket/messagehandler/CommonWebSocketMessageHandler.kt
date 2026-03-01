package io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler

import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.contract.CommonWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.websocket.SocketFrame
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class CommonWebSocketMessageHandler() : WebSocketMessageHandler {
    override suspend fun handle(frame: SocketFrame): WebSocketMessageHandlerResult {
        return when (frame.type) {
            CommonWebSocketEventTypes.PING -> handlePing()
            CommonWebSocketEventTypes.PONG -> WebSocketMessageHandlerResult.Handled
            CommonWebSocketEventTypes.INITIALIZE -> WebSocketMessageHandlerResult.Handled
            CommonWebSocketEventTypes.INITIALIZED_SUCCESS -> WebSocketMessageHandlerResult.Handled
            else -> WebSocketMessageHandlerResult.NotHandled
        }
    }

    @OptIn(ExperimentalUuidApi::class)
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