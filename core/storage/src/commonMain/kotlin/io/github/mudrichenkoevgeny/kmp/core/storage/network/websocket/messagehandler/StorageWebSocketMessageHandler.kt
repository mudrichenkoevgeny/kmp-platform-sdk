package io.github.mudrichenkoevgeny.kmp.core.storage.network.websocket.messagehandler

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandlerResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.websocket.SocketFrame
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.contract.StorageWebSocketEventTypes

/**
 * [WebSocketMessageHandler] for storage-domain frame types (see [StorageWebSocketEventTypes]).
 */
class StorageWebSocketMessageHandler : WebSocketMessageHandler {
    override suspend fun handle(frame: SocketFrame): WebSocketMessageHandlerResult {
        return WebSocketMessageHandlerResult.NotHandled
    }
}
