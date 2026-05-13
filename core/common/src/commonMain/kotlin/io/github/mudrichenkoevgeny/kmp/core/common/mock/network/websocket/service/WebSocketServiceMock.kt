package io.github.mudrichenkoevgeny.kmp.core.common.mock.network.websocket.service

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.websocket.SocketFrame
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.serialization.json.JsonElement

/**
 * Simple in-memory [io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService] implementation for previews/tests.
 *
 * - `connect`/`disconnect` are no-ops
 * - [observeEvents] exposes an internal [kotlinx.coroutines.flow.MutableSharedFlow] of [io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.websocket.SocketFrame]
 * - sending methods are no-ops (events are not generated automatically)
 */
@InternalApi
class WebSocketServiceMock : WebSocketService {
    private val _events = MutableSharedFlow<SocketFrame>(extraBufferCapacity = 64)

    suspend fun emit(frame: SocketFrame) {
        _events.emit(frame)
    }

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