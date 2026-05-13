package io.github.mudrichenkoevgeny.kmp.core.security.network.websocket.messagehandler

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandlerResult
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.security.repository.SecuritySettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.websocket.SocketFrame
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.core.security.mapper.securitysettings.toSecuritySettings
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.contract.SecurityWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.securitysettings.SecuritySettingsPayload
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * [WebSocketMessageHandler] for security-domain frame types (see [SecurityWebSocketEventTypes]).
 *
 * Returns [WebSocketMessageHandlerResult.NotHandled] when the frame type is not recognized. For
 * `SECURITY_SETTINGS_UPDATED`, returns [WebSocketMessageHandlerResult.Handled]; persisting parsed data
 * is handled by the security settings repository that subscribes to [WebSocketService] events.
 */
class SecurityWebSocketMessageHandler(
    private val securitySettingsRepository: SecuritySettingsRepository,
    private val scope: CoroutineScope
) : WebSocketMessageHandler {
    override suspend fun handle(frame: SocketFrame): WebSocketMessageHandlerResult {
        return when (frame.type) {
            SecurityWebSocketEventTypes.SECURITY_SETTINGS_UPDATED -> handleSecuritySettingsUpdated(
                frame.payload
            )
            else -> WebSocketMessageHandlerResult.NotHandled
        }
    }

    private fun handleSecuritySettingsUpdated(
        payload: JsonElement?
    ): WebSocketMessageHandlerResult {
        if (payload == null) {
            return WebSocketMessageHandlerResult.Error(CommonError.ContractViolation())
        }
        val securitySettingsPayload = try {
            FoundationJson.decodeFromJsonElement<SecuritySettingsPayload>(payload)
        } catch (e: Exception) {
            return WebSocketMessageHandlerResult.Error(CommonError.ContractViolation(e))
        }

        scope.launch {
            securitySettingsRepository.updateSecuritySettings(securitySettingsPayload.toSecuritySettings())
        }

        return WebSocketMessageHandlerResult.Handled
    }
}