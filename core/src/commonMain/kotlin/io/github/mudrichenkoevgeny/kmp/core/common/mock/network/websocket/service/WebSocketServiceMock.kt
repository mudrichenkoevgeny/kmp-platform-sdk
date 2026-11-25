package io.github.mudrichenkoevgeny.kmp.core.common.mock.network.websocket.service

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.websocket.SocketFrame
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.serialization.json.JsonElement

/**
 * Simple in-memory [WebSocketService] implementation for previews/tests.
 *
 * - `connect`/`disconnect` are no-ops
 * - [observeEvents] exposes an internal [MutableSharedFlow] of [SocketFrame]
 * - sending methods are no-ops (events are not generated automatically)
 */
@InternalApi
public class WebSocketServiceMock : WebSocketService {
    private val eventsFlow = MutableSharedFlow<SocketFrame>(extraBufferCapacity = 64)

    /**
     * Emits a [SocketFrame] to the events flow for testing.
     *
     * @param frame The socket frame to emit.
     */
    public suspend fun emit(frame: SocketFrame) {
        eventsFlow.emit(frame)
    }

    override fun connect(): Unit = Unit

    override fun disconnect(): Unit = Unit

    override fun restart(): Unit = Unit

    override fun observeEvents(): SharedFlow<SocketFrame> = eventsFlow

    override fun updateWebSocketMessageHandlers(
        webSocketMessageHandlers: List<WebSocketMessageHandler>
    ): Unit = Unit

    override suspend fun sendEvent(
        type: String,
        payload: JsonElement?,
        metadata: Map<String, String>
    ): Unit = Unit

    override suspend fun sendPing(metadata: Map<String, String>): Unit = Unit
}
