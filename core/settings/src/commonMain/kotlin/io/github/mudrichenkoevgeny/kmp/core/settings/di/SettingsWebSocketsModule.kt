package io.github.mudrichenkoevgeny.kmp.core.settings.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.settings.network.websockets.messagehandler.SettingsWebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.GlobalSettingsRepository
import kotlinx.coroutines.CoroutineScope

/**
 * Internal network wiring for `core/settings`.
 *
 * Provides the Ktor-backed the [SettingsWebSocketMessageHandler] for host
 * registration alongside other [WebSocketMessageHandler] instances.
 */
internal class SettingsWebSocketsModule(
    globalSettingsRepository: GlobalSettingsRepository,
    scope: CoroutineScope
) {

    /**
     * Handler offered to the shared WebSocket pipeline for settings-related frame types.
     */
    val settingsWebSocketMessageHandler: WebSocketMessageHandler by lazy {
        SettingsWebSocketMessageHandler(
            globalSettingsRepository = globalSettingsRepository,
            scope = scope
        )
    }
}