package io.github.mudrichenkoevgeny.kmp.feature.user.network.websocket.messagehandler

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.network.model.websocket.socketFrameMock
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandlerResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.user.userDetailsPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.refreshtoken.RefreshTokenRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth.AuthStorageMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.user.UserStorageMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.refreshtoken.RefreshTokenUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.contract.UserWebSocketEventTypes
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.encodeToJsonElement
import kotlin.test.Test
import kotlin.test.assertSame

@InternalApi
class UserWebSocketMessageHandlerTest {

    private val userStorage = UserStorageMock()
    private val authStorage = AuthStorageMock()
    private val refreshTokenUseCase = RefreshTokenUseCase(
        refreshTokenRepository = RefreshTokenRepositoryMock(),
        authStorage = authStorage
    )

    private fun createHandler(scope: TestScope) = UserWebSocketMessageHandler(
        userStorage = userStorage,
        authStorage = authStorage,
        refreshTokenUseCase = refreshTokenUseCase,
        scope = scope
    )

    @Test
    fun `known user websocket event types are handled`() = runTest {
        val handler = createHandler(this)

        val frames = listOf(
            socketFrameMock(type = UserWebSocketEventTypes.UNAUTHORIZED),
            socketFrameMock(
                type = UserWebSocketEventTypes.USER_UPDATED,
                payload = FoundationJson.encodeToJsonElement(userDetailsPayloadMock())
            ),
            socketFrameMock(type = UserWebSocketEventTypes.SESSION_DELETED)
        )

        for (f in frames) {
            val result = handler.handle(f)
            assertSame(
                expected = WebSocketMessageHandlerResult.Handled,
                actual = result,
                message = "Failed for type: ${f.type}"
            )
        }
    }

    @Test
    fun `auth settings updated is not handled`() = runTest {
        val handler = createHandler(this)
        assertSame(
            expected = WebSocketMessageHandlerResult.NotHandled,
            actual = handler.handle(socketFrameMock(type = UserWebSocketEventTypes.OPEN_AUTH_SETTINGS_UPDATED))
        )
    }

    @Test
    fun `unknown type is not handled`() = runTest {
        val handler = createHandler(this)
        assertSame(
            expected = WebSocketMessageHandlerResult.NotHandled,
            actual = handler.handle(socketFrameMock(type = "UNKNOWN_USER_WS_TYPE"))
        )
    }
}
