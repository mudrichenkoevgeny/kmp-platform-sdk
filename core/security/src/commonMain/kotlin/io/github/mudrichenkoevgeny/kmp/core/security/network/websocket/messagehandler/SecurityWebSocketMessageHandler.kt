package io.github.mudrichenkoevgeny.kmp.core.security.network.websocket.messagehandler

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandlerResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.websocket.SocketFrame
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.contract.SecurityWebSocketEventTypes

class SecurityWebSocketMessageHandler() : WebSocketMessageHandler {
    override suspend fun handle(frame: SocketFrame): WebSocketMessageHandlerResult {
        return when (frame.type) {
            SecurityWebSocketEventTypes.SECURITY_SETTINGS_UPDATED -> handleSecuritySettingsUpdated()
            else -> WebSocketMessageHandlerResult.NotHandled
        }
    }

    private fun handleSecuritySettingsUpdated(): WebSocketMessageHandlerResult {
        // todo update settings
        return WebSocketMessageHandlerResult.Handled
    }
}