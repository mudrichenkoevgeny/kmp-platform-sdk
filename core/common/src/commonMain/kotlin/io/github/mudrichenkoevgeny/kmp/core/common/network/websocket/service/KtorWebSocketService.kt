package io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service

import io.github.mudrichenkoevgeny.kmp.core.common.network.provider.AccessTokenProvider
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.mapper.toWebSocketInitializePayload
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandlerResult
import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model.DeviceInfo
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.contract.CommonWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.contract.WebSocketContract
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.websocket.SocketFrame
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.ktor.client.HttpClient
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlin.time.Clock
import kotlin.uuid.Uuid

/**
 * Ktor-based implementation of [WebSocketService].
 *
 * It connects to the `WebSocketContract` endpoint, routes incoming frames through a list of
 * [WebSocketMessageHandler]s, and exposes unhandled frames through [observeEvents].
 *
 * The service restarts the connection automatically when access token changes
 * (see [AccessTokenProvider.accessTokenFlow]).
 */
class KtorWebSocketService(
    private val httpClient: HttpClient,
    private val baseUrl: String,
    private val networkLogger: Logger,
    private val accessTokenProvider: AccessTokenProvider,
    private val deviceInfo: DeviceInfo,
    private val scope: CoroutineScope
) : WebSocketService {

    private var connectionJob: Job? = null

    private var isConnectionStarted = false
    private val _events = MutableSharedFlow<SocketFrame>()
    private val outgoingChannel = Channel<SocketFrame>(Channel.BUFFERED)

    private var webSocketMessageHandlers: List<WebSocketMessageHandler> = emptyList()

    init {
        observeTokenChanges()
    }

    override fun connect() {
        if (isConnectionStarted) {
            networkLogger.log("$LOGGER_SOCKET_PREFIX: Already started")
            return
        }
        networkLogger.log("$LOGGER_SOCKET_PREFIX: Connecting...")
        isConnectionStarted = true
        startConnection()
    }

    override fun disconnect() {
        networkLogger.log("$LOGGER_SOCKET_PREFIX: Disconnecting...")
        isConnectionStarted = false
        connectionJob?.cancel()
        connectionJob = null
    }

    override fun restart() {
        if (!isConnectionStarted) {
            networkLogger.log("$LOGGER_SOCKET_PREFIX: Cannot restart, service not started")
            return
        }
        restartConnection()
    }

    override fun observeEvents(): Flow<SocketFrame> = _events.asSharedFlow()

    override fun updateWebSocketMessageHandlers(
        webSocketMessageHandlers: List<WebSocketMessageHandler>
    ) {
        this.webSocketMessageHandlers = webSocketMessageHandlers
    }

    override suspend fun sendEvent(
        type: String,
        payload: JsonElement?,
        metadata: Map<String, String>
    ) {
        val frame = SocketFrame(
            id = Uuid.random().toHexDashString(),
            type = type,
            payload = payload,
            metadata = metadata,
            timestamp = Clock.System.now().toEpochMilliseconds()
        )
        outgoingChannel.send(frame)
    }

    override suspend fun sendPing(metadata: Map<String, String>) {
        sendEvent(CommonWebSocketEventTypes.PING, payload = null, metadata)
    }

    private fun startConnection() {
        connectionJob?.cancel()

        connectionJob = scope.launch {
            var currentDelay = INITIAL_RECONNECT_DELAY_MS

            while (isActive && isConnectionStarted) {
                try {
                    val socketHost = baseUrl
                        .removePrefix(PREFIX_HTTPS)
                        .removePrefix(PREFIX_HTTP)
                        .removePrefix(PREFIX_WSS)
                        .removePrefix(PREFIX_WS)

                    val accessToken = accessTokenProvider.accessTokenFlow.value

                    val webSocketPath = if (accessToken != null) {
                        "${WebSocketContract.WS_REALTIME_PATH}?token=$accessToken"
                    } else {
                        WebSocketContract.WS_REALTIME_PATH
                    }

                    httpClient.webSocket(
                        method = HttpMethod.Get,
                        host = socketHost,
                        path = webSocketPath
                    ) {
                        currentDelay = INITIAL_RECONNECT_DELAY_MS

                        networkLogger.log("$LOGGER_SOCKET_PREFIX: Connected to $socketHost${WebSocketContract.WS_REALTIME_PATH}")

                        sendInitializeFrame()

                        val readJob = launch { handleIncomingFrames() }
                        val writeJob = launch { handleOutgoingFrames() }

                        readJob.join()
                        writeJob.cancel()
                    }
                } catch (e: Exception) {
                    if (isActive && isConnectionStarted) {
                        networkLogger.log("Socket: Connection error: ${e.message}")
                    }
                }

                if (isActive && isConnectionStarted) {
                    networkLogger.log("Socket: Next retry in $currentDelay ms")
                    delay(currentDelay)
                    currentDelay = (currentDelay * 2).coerceAtMost(MAX_RECONNECT_DELAY_MS)
                }
            }
        }
    }

    private fun restartConnection() {
        connectionJob?.cancel()
        if (isConnectionStarted) {
            startConnection()
        }
    }

    private suspend fun WebSocketSession.handleIncomingFrames() {
        try {
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val text = frame.readText()

                    val socketFrame = try {
                        FoundationJson.decodeFromString<SocketFrame>(text)
                    } catch (e: Exception) {
                        networkLogger.log("$LOGGER_SOCKET_PREFIX: Failed to parse frame: ${e.message}")
                        continue
                    }

                    processSocketFrame(socketFrame)
                }
            }
        } catch (e: Exception) {
            if (e !is kotlinx.coroutines.channels.ClosedReceiveChannelException) {
                networkLogger.log("$LOGGER_SOCKET_PREFIX: Receiving loop stopped: ${e.message}")
            }
        }
    }

    private suspend fun processSocketFrame(socketFrame: SocketFrame) {
        var isHandled = false

        for (handler in webSocketMessageHandlers) {
            val result = handler.handle(socketFrame)

            if (result !is WebSocketMessageHandlerResult.NotHandled) {
                isHandled = true
                applyHandlerResult(result)
                break
            }
        }

        if (!isHandled) {
            _events.emit(socketFrame)
        }
    }

    private suspend fun applyHandlerResult(result: WebSocketMessageHandlerResult) {
        when (result) {
            is WebSocketMessageHandlerResult.SendSocketFrame -> {
                outgoingChannel.send(result.socketFrame)
            }
            is WebSocketMessageHandlerResult.Error -> {
                networkLogger.log("$LOGGER_SOCKET_PREFIX: Handler error: ${result.appError}")
            }
            else -> Unit
        }
    }

    private suspend fun WebSocketSession.handleOutgoingFrames() {
        try {
            outgoingChannel.receiveAsFlow().collect { socketFrame ->
                val jsonText = FoundationJson.encodeToString(socketFrame)
                send(Frame.Text(jsonText))
            }
        } catch (e: Exception) {
            networkLogger.log("$LOGGER_SOCKET_PREFIX: Sending loop stopped: ${e.message}")
        }
    }

    private fun observeTokenChanges() {
        scope.launch {
            accessTokenProvider.accessTokenFlow.collect {
                if (isConnectionStarted) {
                    networkLogger.log("$LOGGER_SOCKET_PREFIX: Token updated, restarting...")
                    restartConnection()
                }
            }
        }
    }

    private suspend fun sendInitializeFrame() {
        val payload = deviceInfo.toWebSocketInitializePayload()
        sendEvent(
            type = CommonWebSocketEventTypes.INITIALIZE,
            payload = FoundationJson.encodeToJsonElement(payload)
        )
    }

    companion object {
        private const val INITIAL_RECONNECT_DELAY_MS = 2000L
        private const val MAX_RECONNECT_DELAY_MS = 60000L

        private const val PREFIX_HTTPS = "https://"
        private const val PREFIX_HTTP = "http://"
        private const val PREFIX_WSS = "wss://"
        private const val PREFIX_WS = "ws://"

        private const val LOGGER_SOCKET_PREFIX = "Socket"
    }
}