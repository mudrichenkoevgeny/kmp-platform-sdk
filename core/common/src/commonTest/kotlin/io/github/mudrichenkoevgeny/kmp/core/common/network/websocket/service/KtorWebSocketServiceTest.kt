package io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.network.provider.AccessTokenProvider
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandlerResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientDeviceInfo
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.websocket.SocketFrame
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@InternalApi
class KtorWebSocketServiceTest {

    private val testLogger = object : Logger {
        val logs = mutableListOf<String>()
        override fun log(message: String) {
            logs.add(message)
        }
    }

    private val testTokenFlow = MutableStateFlow<String?>("valid_token")

    private val testAccessTokenProvider = object : AccessTokenProvider {
        override val accessTokenFlow: StateFlow<String?> = testTokenFlow
    }

    private val testDeviceInfo = ClientDeviceInfo(
        clientType = null,
        deviceId = null,
        deviceName = "TestDevice",
        appVersion = "1.0.0",
        operationSystemVersion = "1.0.0",
        language = "en"
    )

    private fun createService(
        httpClient: HttpClient,
        scope: CoroutineScope
    ): KtorWebSocketService = KtorWebSocketService(
        httpClient = httpClient,
        baseUrl = "https://localhost:8080",
        webSocketPath = "/ws",
        networkLogger = testLogger,
        accessTokenProvider = testAccessTokenProvider,
        clientDeviceInfo = testDeviceInfo,
        scope = scope
    )

    private fun createMockHttpClient(): HttpClient = HttpClient(MockEngine {
        respond(
            content = "",
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
    }) {
        install(WebSockets)
    }

    @Test
    fun connect_and_disconnect_lifecycle() = runTest {
        val testScope = CoroutineScope(UnconfinedTestDispatcher(testScheduler) + Job())
        val service = createService(createMockHttpClient(), testScope)

        service.connect()
        assertTrue(testLogger.logs.any { it.contains("Connecting...") })

        service.connect()
        assertTrue(testLogger.logs.any { it.contains("Already started") })

        service.disconnect()
        assertTrue(testLogger.logs.any { it.contains("Disconnecting...") })

        testScope.cancel()
    }

    @Test
    fun restart_whenNotConnected_logsWarning() = runTest {
        val testScope = CoroutineScope(UnconfinedTestDispatcher(testScheduler) + Job())
        val service = createService(createMockHttpClient(), testScope)

        service.restart()
        assertTrue(testLogger.logs.any { it.contains("Cannot restart, service not started") })

        testScope.cancel()
    }

    @Test
    fun updateWebSocketMessageHandlers_and_observeEvents() = runTest {
        val testScope = CoroutineScope(UnconfinedTestDispatcher(testScheduler) + Job())
        val service = createService(createMockHttpClient(), testScope)

        var handlerInvoked = false
        val customHandler = object : WebSocketMessageHandler {
            override suspend fun handle(frame: SocketFrame): WebSocketMessageHandlerResult {
                return if (frame.type == "CUSTOM_TEST_TYPE") {
                    handlerInvoked = true
                    WebSocketMessageHandlerResult.Handled
                } else {
                    WebSocketMessageHandlerResult.NotHandled
                }
            }
        }

        service.updateWebSocketMessageHandlers(listOf(customHandler))

        service.sendEvent("CUSTOM_TEST_TYPE", payload = null)
        service.sendPing()
        assertFalse(handlerInvoked)

        testScope.cancel()
    }

    @Test
    fun connectionCancellation_and_exceptionInScope_handledSafelyWithoutCrashing() = runTest {
        val testScope = CoroutineScope(UnconfinedTestDispatcher(testScheduler) + Job())
        val service = createService(createMockHttpClient(), testScope)

        service.connect()
        advanceUntilIdle()

        service.disconnect()
        advanceUntilIdle()

        assertTrue(testScope.coroutineContext[Job]?.isActive == true)

        testScope.cancel()
    }

    @Test
    fun tokenChanges_restartsConnectionWhenConnected() = runTest {
        val testScope = CoroutineScope(UnconfinedTestDispatcher(testScheduler) + Job())
        val service = createService(createMockHttpClient(), testScope)

        service.connect()
        advanceUntilIdle()

        testTokenFlow.value = "new_token_123"
        advanceUntilIdle()

        assertTrue(testLogger.logs.any { it.contains("Token updated, restarting...") })

        service.disconnect()
        testScope.cancel()
    }
}
