package io.github.mudrichenkoevgeny.kmp.core.common.mock.network.model.websocket

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.contract.CommonWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.websocket.SocketFrame
import kotlinx.serialization.json.JsonElement

@InternalApi
fun socketFrameMock(
    id: String = "123e4567-e89b-12d3-a456-426614174000",
    type: String = CommonWebSocketEventTypes.PING,
    payload: JsonElement? = null
) = SocketFrame(
    id = id,
    type = type,
    payload = payload,
    metadata = emptyMap(),
    timestamp = 0L
)