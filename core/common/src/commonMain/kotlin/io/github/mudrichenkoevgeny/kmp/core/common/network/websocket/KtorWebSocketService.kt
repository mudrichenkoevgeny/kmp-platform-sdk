package io.github.mudrichenkoevgeny.kmp.core.common.network.websocket

import io.github.mudrichenkoevgeny.kmp.core.common.network.provider.AccessTokenProvider
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.contract.CommonWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.contract.WebSocketContract
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.SocketFrame
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
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonElement
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class KtorWebSocketService(
    private val httpClient: HttpClient,
    private val baseUrl: String,
    private val networkLogger: Logger,
    private val accessTokenProvider: AccessTokenProvider,
    private val scope: CoroutineScope
) : WebSocketService {

    private var connectionJob: Job? = null

    private val _events = MutableSharedFlow<SocketFrame>()
    private val outgoingChannel = Channel<SocketFrame>(Channel.BUFFERED)

    init {
        observeTokenChanges()
    }

    override fun observeEvents(): Flow<SocketFrame> = _events.asSharedFlow()

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun sendEvent(type: String, payload: JsonElement?) {
        val frame = SocketFrame(
            id = Uuid.random().toHexDashString(),
            type = type,
            payload = payload,
            timestamp = Clock.System.now().toEpochMilliseconds()
        )
        outgoingChannel.send(frame)
    }

    override suspend fun sendPing() {
        sendEvent(CommonWebSocketEventTypes.PING, payload = null)
    }

    private fun startConnection() {
        connectionJob = scope.launch {
            var currentDelay = INITIAL_RECONNECT_DELAY_MS

            while (isActive) {
                try {
                    val socketHost = baseUrl
                        .removePrefix(PREFIX_HTTPS)
                        .removePrefix(PREFIX_HTTP)
                        .removePrefix(PREFIX_WSS)
                        .removePrefix(PREFIX_WS)

                    httpClient.webSocket(
                        method = HttpMethod.Get,
                        host = socketHost,
                        path = WebSocketContract.WS_REALTIME_PATH
                    ) {
                        networkLogger.log("$LOGGER_SOCKET_PREFIX: Connected to $socketHost${WebSocketContract.WS_REALTIME_PATH}")

                        currentDelay = INITIAL_RECONNECT_DELAY_MS

                        val readJob = launch { handleIncomingFrames() }
                        val writeJob = launch { handleOutgoingFrames() }

                        readJob.join()
                        writeJob.cancel()
                    }
                } catch (e: Exception) {
                    networkLogger.log("Socket: Connection error: ${e.message}")
                }

                if (isActive) {
                    networkLogger.log("Socket: Retrying in $currentDelay ms...")
                    delay(currentDelay)

                    currentDelay = (currentDelay * 2).coerceAtMost(MAX_RECONNECT_DELAY_MS)
                }
            }
        }
    }

    private fun restartConnection() {
        connectionJob?.cancel()
        startConnection()
    }

    private suspend fun WebSocketSession.handleIncomingFrames() {
        try {
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val text = frame.readText()
                    runCatching {
                        FoundationJson.decodeFromString<SocketFrame>(text)
                    }.onSuccess { event ->
                        if (event.type == CommonWebSocketEventTypes.PING) {
                            networkLogger.log("$LOGGER_SOCKET_PREFIX: Received PING, sending PONG")
                            sendEvent(CommonWebSocketEventTypes.PONG, null)
                        } else {
                            _events.emit(event)
                        }
                    }.onFailure { e ->
                        networkLogger.log("$LOGGER_SOCKET_PREFIX: Failed to parse frame: ${e.message}")
                    }
                }
            }
        } catch (e: Exception) {
            networkLogger.log("$LOGGER_SOCKET_PREFIX: Receiving loop stopped: ${e.message}")
        }
    }

    private suspend fun WebSocketSession.handleOutgoingFrames() {
        try {
            outgoingChannel.consumeAsFlow().collect { socketFrame ->
                val jsonText = FoundationJson.encodeToString(socketFrame)
                send(Frame.Text(jsonText))
            }
        } catch (e: Exception) {
            networkLogger.log("$LOGGER_SOCKET_PREFIX: Sending loop stopped: ${e.message}")
        }
    }

    private fun observeTokenChanges() {
        scope.launch {
            accessTokenProvider.accessTokenFlow
                .distinctUntilChanged()
                .collect {
                    networkLogger.log("$LOGGER_SOCKET_PREFIX: Token updated, restarting...")
                    restartConnection()
                }
        }
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