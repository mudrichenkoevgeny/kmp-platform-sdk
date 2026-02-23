package io.github.mudrichenkoevgeny.kmp.core.common.network.websocket

import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.SocketFrame
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement

interface WebSocketService {
    fun observeEvents(): Flow<SocketFrame>
    suspend fun sendEvent(type: String, payload: JsonElement?)
    suspend fun sendPing()
}