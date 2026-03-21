package io.github.mudrichenkoevgeny.kmp.core.settings.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.settings.network.api.globalsettings.GlobalSettingsApi
import io.github.mudrichenkoevgeny.kmp.core.settings.network.api.globalsettings.KtorGlobalSettingsApi
import io.github.mudrichenkoevgeny.kmp.core.settings.network.websockets.messagehandler.SettingsWebSocketMessageHandler
import io.ktor.client.HttpClient

/**
 * Internal network wiring for `core/settings`.
 *
 * Provides the Ktor-backed [GlobalSettingsApi] and the [SettingsWebSocketMessageHandler] for host
 * registration alongside other [WebSocketMessageHandler] instances.
 */
internal class SettingsNetworkModule(httpClient: HttpClient) {
    /**
     * Ktor-backed implementation of [GlobalSettingsApi].
     */
    val globalSettingsApi by lazy {
        KtorGlobalSettingsApi(
            httpClient
        )
    }

    /**
     * Handler offered to the shared WebSocket pipeline for settings-related frame types.
     */
    val settingsWebSocketMessageHandler: WebSocketMessageHandler by lazy {
        SettingsWebSocketMessageHandler()
    }
}