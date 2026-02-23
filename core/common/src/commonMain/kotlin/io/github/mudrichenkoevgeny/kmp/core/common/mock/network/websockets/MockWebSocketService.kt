package io.github.mudrichenkoevgeny.kmp.core.common.mock.network.websockets

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.WebSocketService
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.SocketFrame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.serialization.json.JsonElement

class MockWebSocketService : WebSocketService {
    private val _events = MutableSharedFlow<SocketFrame>()

    override fun observeEvents(): Flow<SocketFrame> = _events

    override suspend fun sendEvent(type: String, payload: JsonElement?) {}

    override suspend fun sendPing() {}
}