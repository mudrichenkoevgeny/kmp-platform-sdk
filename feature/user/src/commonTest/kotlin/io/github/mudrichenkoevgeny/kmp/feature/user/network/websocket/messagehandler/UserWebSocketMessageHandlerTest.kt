package io.github.mudrichenkoevgeny.kmp.feature.user.network.websocket.messagehandler

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandlerResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.websocket.SocketFrame
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.contract.UserWebSocketEventTypes
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertSame

private const val FRAME_ID = "123e4567-e89b-12d3-a456-426614174000"
private const val FRAME_TS = 42L

class UserWebSocketMessageHandlerTest {

    private val handler = UserWebSocketMessageHandler()

    private fun frame(type: String) = SocketFrame(
        id = FRAME_ID,
        type = type,
        timestamp = FRAME_TS
    )

    @Test
    fun `known user websocket event types are handled`() = runTest {
        val handledTypes = listOf(
            UserWebSocketEventTypes.UNAUTHORIZED,
            UserWebSocketEventTypes.AUTH_SETTINGS_UPDATED,
            UserWebSocketEventTypes.ACCOUNT_STATUS_CHANGED,
            UserWebSocketEventTypes.SESSION_TERMINATED
        )
        for (type in handledTypes) {
            assertSame(
                WebSocketMessageHandlerResult.Handled,
                handler.handle(frame(type))
            )
        }
    }

    @Test
    fun `unknown type is not handled`() = runTest {
        assertSame(
            WebSocketMessageHandlerResult.NotHandled,
            handler.handle(frame("UNKNOWN_USER_WS_TYPE"))
        )
    }
}
