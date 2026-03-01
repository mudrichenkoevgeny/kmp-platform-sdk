package io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.websocket.SocketFrame
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement

interface WebSocketService {
    fun connect()
    fun disconnect()
    fun restart()
    fun observeEvents(): Flow<SocketFrame>
    fun updateWebSocketMessageHandlers(webSocketMessageHandlers: List<WebSocketMessageHandler>)
    suspend fun sendEvent(
        type: String,
        payload: JsonElement?,
        metadata: Map<String, String> = emptyMap()
    )
    suspend fun sendPing(metadata: Map<String, String> = emptyMap())
}