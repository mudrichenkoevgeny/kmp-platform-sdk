package io.github.mudrichenkoevgeny.kmp.feature.settingsapi.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.settings.network.globalsettings.GlobalSettingsApi
import io.github.mudrichenkoevgeny.kmp.feature.settingsapi.network.globalsettings.KtorGlobalSettingsApi
import io.github.mudrichenkoevgeny.kmp.core.settings.network.websockets.messagehandler.SettingsWebSocketMessageHandler
import io.ktor.client.HttpClient

/**
 * Internal network wiring for `core/settings`.
 *
 * Provides the Ktor-backed [GlobalSettingsApi] and the [SettingsWebSocketMessageHandler] for host
 * registration alongside other [WebSocketMessageHandler] instances.
 */
internal class SettingsNetworkModule(
    httpClient: HttpClient
) {
    /**
     * Ktor-backed implementation of [GlobalSettingsApi].
     */
    val globalSettingsApi by lazy {
        KtorGlobalSettingsApi(
            httpClient
        )
    }
}