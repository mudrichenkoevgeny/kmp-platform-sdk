package io.github.mudrichenkoevgeny.kmp.core.common.mock.network.websockets

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.websocket.SocketFrame
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.serialization.json.JsonElement

class MockWebSocketService : WebSocketService {
    private val _events = MutableSharedFlow<SocketFrame>()

    override fun connect() {}

    override fun disconnect() {}

    override fun restart() {}

    override fun observeEvents(): SharedFlow<SocketFrame> = _events

    override fun updateWebSocketMessageHandlers(
        webSocketMessageHandlers: List<WebSocketMessageHandler>
    ) {}

    override suspend fun sendEvent(
        type: String,
        payload: JsonElement?,
        metadata: Map<String, String>
    ) {}

    override suspend fun sendPing(metadata: Map<String, String>) {}
}