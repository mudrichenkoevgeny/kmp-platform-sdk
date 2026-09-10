package io.github.mudrichenkoevgeny.kmp.feature.user.network.websocket.messagehandler

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandlerResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.refreshtoken.RefreshTokenUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.websocket.SocketFrame
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.user.toUserDetails
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.contract.UserWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.user.UserDetailsPayload
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * Interprets user-related WebSocket frames (`UserWebSocketEventTypes`) for unauthorized sessions,
 * account status, and session termination. Register alongside other handlers on the app `WebSocketService`.
 *
 * Auth settings updates (`AUTH_SETTINGS_UPDATED`) are handled directly by the corresponding repositories.
 *
 * @param userStorage Storage for updating the current user snapshot.
 * @param userRepository User repository for clearing local session state on deletion.
 * @param refreshTokenUseCase Use case to trigger session refresh on unauthorized frames.
 * @param scope Coroutine scope for launching background update/clear tasks.
 */
class UserWebSocketMessageHandler(
    private val userStorage: UserStorage,
    private val userRepository: UserRepository,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val scope: CoroutineScope
) : WebSocketMessageHandler {
    override suspend fun handle(frame: SocketFrame): WebSocketMessageHandlerResult {
        return when (frame.type) {
            UserWebSocketEventTypes.UNAUTHORIZED -> handleUnauthorized()
            UserWebSocketEventTypes.USER_UPDATED -> handleUserUpdated(frame.payload)
            UserWebSocketEventTypes.SESSION_DELETED -> handleSessionDeleted()
            else -> WebSocketMessageHandlerResult.NotHandled
        }
    }

    private suspend fun handleUnauthorized(): WebSocketMessageHandlerResult {
        refreshTokenUseCase()
        return WebSocketMessageHandlerResult.Handled
    }

    private fun handleUserUpdated(payload: JsonElement?): WebSocketMessageHandlerResult {
        if (payload == null) {
            return WebSocketMessageHandlerResult.Error(CommonError.ContractViolation())
        }

        val userDetailsPayload = try {
            FoundationJson.decodeFromJsonElement<UserDetailsPayload>(payload)
        } catch (e: Exception) {
            return WebSocketMessageHandlerResult.Error(CommonError.ContractViolation(e))
        }

        scope.launch {
            userStorage.updateCurrentUser(userDetailsPayload.toUserDetails())
        }

        return WebSocketMessageHandlerResult.Handled
    }

    private fun handleSessionDeleted(): WebSocketMessageHandlerResult {
        scope.launch {
            userRepository.clearSession()
        }

        return WebSocketMessageHandlerResult.Handled
    }
}
