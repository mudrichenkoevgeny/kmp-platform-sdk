package io.github.mudrichenkoevgeny.kmp.core.settings.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.settings.network.api.globalsettings.KtorGlobalSettingsApi
import io.github.mudrichenkoevgeny.kmp.core.settings.network.websockets.messagehandler.SettingsWebSocketMessageHandler
import io.ktor.client.HttpClient

internal class SettingsNetworkModule(httpClient: HttpClient) {
    val globalSettingsApi by lazy {
        KtorGlobalSettingsApi(
            httpClient
        )
    }

    val settingsWebSocketMessageHandler: WebSocketMessageHandler by lazy {
        SettingsWebSocketMessageHandler()
    }
}