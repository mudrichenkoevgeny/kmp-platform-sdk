package io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.websocket.SocketFrame

sealed interface WebSocketMessageHandlerResult {
    object NotHandled : WebSocketMessageHandlerResult
    object Handled : WebSocketMessageHandlerResult
    data class SendSocketFrame(val socketFrame: SocketFrame) : WebSocketMessageHandlerResult
    data class Error(val appError: AppError) : WebSocketMessageHandlerResult
}