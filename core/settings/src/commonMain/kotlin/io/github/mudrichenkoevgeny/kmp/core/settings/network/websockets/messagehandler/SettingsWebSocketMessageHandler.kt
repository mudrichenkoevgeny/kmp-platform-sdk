package io.github.mudrichenkoevgeny.kmp.core.settings.network.websockets.messagehandler

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandlerResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.websocket.SocketFrame
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.contract.SettingsWebSocketEventTypes

class SettingsWebSocketMessageHandler() : WebSocketMessageHandler {
    override suspend fun handle(frame: SocketFrame): WebSocketMessageHandlerResult {
        return when (frame.type) {
            SettingsWebSocketEventTypes.GLOBAL_SETTINGS_UPDATED -> handleGlobalSettingsUpdated()
            else -> WebSocketMessageHandlerResult.NotHandled
        }
    }

    private fun handleGlobalSettingsUpdated(): WebSocketMessageHandlerResult {
        // todo update settings
        return WebSocketMessageHandlerResult.Handled
    }
}