package io.github.mudrichenkoevgeny.kmp.feature.user.network.websocket.messagehandler

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandlerResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.websocket.SocketFrame
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.contract.UserWebSocketEventTypes

class UserWebSocketMessageHandler() : WebSocketMessageHandler {
    override suspend fun handle(frame: SocketFrame): WebSocketMessageHandlerResult {
        return when (frame.type) {
            UserWebSocketEventTypes.UNAUTHORIZED -> handleUnauthorized()
            UserWebSocketEventTypes.AUTH_SETTINGS_UPDATED -> handleAuthSettingsUpdated()
            UserWebSocketEventTypes.ACCOUNT_STATUS_CHANGED -> handleAccountStatusChanged()
            UserWebSocketEventTypes.SESSION_TERMINATED -> handleSessionTerminated()
            else -> WebSocketMessageHandlerResult.NotHandled
        }
    }

    private suspend fun handleUnauthorized(): WebSocketMessageHandlerResult {
        // todo call refreshToken. then accessTokenFlow will automatically update
        return WebSocketMessageHandlerResult.Handled
    }

    private fun handleAuthSettingsUpdated(): WebSocketMessageHandlerResult {
        // todo update settings
        return WebSocketMessageHandlerResult.Handled
    }

    private fun handleAccountStatusChanged(): WebSocketMessageHandlerResult {
        // todo
        return WebSocketMessageHandlerResult.Handled
    }

    private fun handleSessionTerminated(): WebSocketMessageHandlerResult {
        // todo
        return WebSocketMessageHandlerResult.Handled
    }
}