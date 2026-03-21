package io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.websocket.SocketFrame
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement

/**
 * SDK-level WebSocket service abstraction.
 *
 * Implementations are responsible for:
 * - managing the WebSocket connection lifecycle (`connect`/`disconnect`/`restart`)
 * - exposing received socket frames via [observeEvents]
 * - routing incoming frames through a list of [WebSocketMessageHandler]s
 * - sending outgoing events (`sendEvent`, `sendPing`)
 */
interface WebSocketService {
    /**
     * Starts the connection and begins listening for frames.
     */
    fun connect()

    /**
     * Stops the connection and releases resources.
     */
    fun disconnect()

    /**
     * Restarts the connection when it is already started.
     */
    fun restart()

    /**
     * Hot stream of frames that were not handled by any registered [WebSocketMessageHandler].
     *
     * @return Flow of unhandled incoming frames.
     */
    fun observeEvents(): Flow<SocketFrame>

    /**
     * Updates the set of message handlers used to process incoming frames.
     *
     * @param webSocketMessageHandlers Handlers tried in order until one handles the frame.
     */
    fun updateWebSocketMessageHandlers(webSocketMessageHandlers: List<WebSocketMessageHandler>)

    /**
     * Sends an event frame to the server.
     *
     * @param type Event type code.
     * @param payload Optional JSON payload.
     * @param metadata Additional metadata attached to the outgoing frame.
     */
    suspend fun sendEvent(
        type: String,
        payload: JsonElement?,
        metadata: Map<String, String> = emptyMap()
    )

    /**
     * Convenience method for sending a `PING` event.
     */
    suspend fun sendPing(metadata: Map<String, String> = emptyMap())
}