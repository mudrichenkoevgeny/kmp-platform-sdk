package io.github.mudrichenkoevgeny.kmp.core.common.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.httpclient.HttpClientConfigPlugin
import io.github.mudrichenkoevgeny.kmp.core.common.network.httpclient.setupCommonConfig
import io.github.mudrichenkoevgeny.kmp.core.common.network.provider.AccessTokenProvider
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.CommonWebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.KtorWebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.repository.platform.PlatformRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.Logger
import kotlinx.coroutines.CoroutineScope

internal class CommonNetworkModule(
    private val baseUrl: String,
    private val httpClientConfigPlugins: List<HttpClientConfigPlugin> = emptyList(),
    private val accessTokenProvider: AccessTokenProvider,
    private val platformRepository: PlatformRepository,
    private val appScope: CoroutineScope
) {
    private val networkLogger = Logger.DEFAULT

    val httpClient by lazy {
        HttpClient {
            setupCommonConfig(
                baseUrl = baseUrl,
                networkLogger = networkLogger,
                deviceInfo = platformRepository.getDeviceInfo()
            )
            httpClientConfigPlugins.forEach { plugin ->
                plugin.install(this, networkLogger)
            }
        }
    }

    val commonWebSocketMessageHandler: WebSocketMessageHandler by lazy {
        CommonWebSocketMessageHandler()
    }

    val webSocketService: WebSocketService by lazy {
        KtorWebSocketService(
            httpClient = httpClient,
            baseUrl = baseUrl,
            networkLogger = networkLogger,
            accessTokenProvider = accessTokenProvider,
            deviceInfo = platformRepository.getDeviceInfo(),
            scope = appScope
        )
    }
}