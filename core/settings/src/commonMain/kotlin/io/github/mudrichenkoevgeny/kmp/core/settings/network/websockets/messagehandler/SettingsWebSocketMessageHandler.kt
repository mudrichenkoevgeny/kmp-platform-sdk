package io.github.mudrichenkoevgeny.kmp.core.settings.network.websockets.messagehandler

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandlerResult
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.GlobalSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.websocket.SocketFrame
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.mapper.globalsettings.toGlobalSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.contract.SettingsWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.model.globalsettings.GlobalSettingsPayload
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * [WebSocketMessageHandler] for settings-domain frame types (see [SettingsWebSocketEventTypes]).
 *
 * Returns [WebSocketMessageHandlerResult.NotHandled] when the frame type is not recognized. For
 * `GLOBAL_SETTINGS_UPDATED`, returns [WebSocketMessageHandlerResult.Handled]; persisting parsed data
 * is handled by the settings repository that subscribes to [WebSocketService] events.
 *
 * @param globalSettingsRepository Repository for updating the global settings state.
 * @param scope Coroutine scope for launching background update tasks.
 */
class SettingsWebSocketMessageHandler(
    private val globalSettingsRepository: GlobalSettingsRepository,
    private val scope: CoroutineScope
) : WebSocketMessageHandler {
    override suspend fun handle(frame: SocketFrame): WebSocketMessageHandlerResult {
        return when (frame.type) {
            SettingsWebSocketEventTypes.GLOBAL_SETTINGS_UPDATED -> handleGlobalSettingsUpdated(
                frame.payload
            )
            else -> WebSocketMessageHandlerResult.NotHandled
        }
    }

    private fun handleGlobalSettingsUpdated(
        payload: JsonElement?
    ): WebSocketMessageHandlerResult {
        if (payload == null) {
            return WebSocketMessageHandlerResult.Error(CommonError.ContractViolation())
        }
        val globalSettingsPayload = try {
            FoundationJson.decodeFromJsonElement<GlobalSettingsPayload>(payload)
        } catch (e: Exception) {
            return WebSocketMessageHandlerResult.Error(CommonError.ContractViolation(e))
        }

        scope.launch {
            globalSettingsRepository.updateGlobalSettings(globalSettingsPayload.toGlobalSettings())
        }

        return WebSocketMessageHandlerResult.Handled
    }
}