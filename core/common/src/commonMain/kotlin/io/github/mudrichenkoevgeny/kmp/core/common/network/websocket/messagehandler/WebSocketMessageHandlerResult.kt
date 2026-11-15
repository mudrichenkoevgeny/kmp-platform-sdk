package io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.websocket.SocketFrame

/**
 * Result of processing an incoming websocket [SocketFrame].
 *
 * The websocket service uses this to decide whether to emit the frame as an event,
 * ignore it, send an outgoing socket frame, or report an error.
 */
public sealed interface WebSocketMessageHandlerResult {
    /**
     * Handler did not recognize the frame.
     */
    public object NotHandled : WebSocketMessageHandlerResult

    /**
     * Handler processed the frame and no additional action is required.
     */
    public object Handled : WebSocketMessageHandlerResult

    /**
     * Handler requests sending a new outgoing socket frame.
     */
    public data class SendSocketFrame(public val socketFrame: SocketFrame) : WebSocketMessageHandlerResult

    /**
     * Handler failed to process the frame with an application-level [AppError].
     */
    public data class Error(public val appError: AppError) : WebSocketMessageHandlerResult
}
