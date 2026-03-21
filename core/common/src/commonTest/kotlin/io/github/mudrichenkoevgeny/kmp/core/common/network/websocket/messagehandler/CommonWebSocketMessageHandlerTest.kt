package io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.toErrorIdOrNull
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.contract.CommonWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.websocket.SocketFrame
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

private const val FRAME_ID = "123e4567-e89b-12d3-a456-426614174000"
private const val FRAME_TS = 42L

class CommonWebSocketMessageHandlerTest {

    private val handler = CommonWebSocketMessageHandler()

    private fun frame(type: String) = SocketFrame(
        id = FRAME_ID,
        type = type,
        timestamp = FRAME_TS
    )

    @Test
    fun `ping responds with pong frame`() = runTest {
        val result = handler.handle(frame(CommonWebSocketEventTypes.PING))

        assertTrue(result is WebSocketMessageHandlerResult.SendSocketFrame)
        val send = result as WebSocketMessageHandlerResult.SendSocketFrame
        assertEquals(CommonWebSocketEventTypes.PONG, send.socketFrame.type)
        assertNotNull(send.socketFrame.id.toErrorIdOrNull())
    }

    @Test
    fun `pong initialize and initialized success are handled`() = runTest {
        assertSame(
            WebSocketMessageHandlerResult.Handled,
            handler.handle(frame(CommonWebSocketEventTypes.PONG))
        )
        assertSame(
            WebSocketMessageHandlerResult.Handled,
            handler.handle(frame(CommonWebSocketEventTypes.INITIALIZE))
        )
        assertSame(
            WebSocketMessageHandlerResult.Handled,
            handler.handle(frame(CommonWebSocketEventTypes.INITIALIZED_SUCCESS))
        )
    }

    @Test
    fun `unknown type is not handled`() = runTest {
        assertSame(
            WebSocketMessageHandlerResult.NotHandled,
            handler.handle(frame("UNKNOWN_CUSTOM_TYPE"))
        )
    }
}
