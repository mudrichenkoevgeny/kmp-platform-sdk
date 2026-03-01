package io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler

import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.websocket.SocketFrame

interface WebSocketMessageHandler {
    suspend fun handle(frame: SocketFrame): WebSocketMessageHandlerResult
}