package io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.user

import co.touchlab.kermit.Logger
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandlerResult
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.user.OpenUserApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.user.toUserPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.contract.UserWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.user.UserPrivatePayload
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * Implements [UserRepository] by observing [UserStorage], coordinating network updates via [OpenUserApi],
 * and listening for live [UserWebSocketEventTypes.USER_UPDATED] events via [WebSocketService].
 *
 * @param userStorage Local source for the current user stream and persistence.
 * @param authStorage Auth storage for tokens.
 * @param openUserApi User HTTP API for profile and account management.
 * @param webSocketService Source of push updates for user changes.
 * @param repositoryScope Scope used to collect WebSocket events throughout the repository's lifecycle.
 */
class OpenUserRepositoryImpl(
    private val userStorage: UserStorage,
    private val authStorage: AuthStorage,
    private val openUserApi: OpenUserApi,
    private val webSocketService: WebSocketService,
    repositoryScope: CoroutineScope
) : UserRepository {

    private val mutex = Mutex()

    override val currentUser: Flow<UserPrivate?> = userStorage.observeCurrentUser()

    init {
        repositoryScope.launch {
            webSocketService.observeEvents()
                .collect { webSocketFrame ->
                    when (webSocketFrame.type) {
                        UserWebSocketEventTypes.USER_UPDATED -> {
                            handleUserUpdated(webSocketFrame.payload)
                        }
                        UserWebSocketEventTypes.SESSION_DELETED -> {
                            handleSessionDeleted()
                        }
                    }
                }
        }
    }

    override suspend fun refreshCurrentUser(): AppResult<UserPrivate> {
        return mutex.withLock {
            openUserApi.getUser().mapSuccess { userPrivatePayload: UserPrivatePayload ->
                val userPrivate = userPrivatePayload.toUserPrivate()
                userStorage.updateCurrentUser(userPrivate)
                userPrivate
            }
        }
    }

    override suspend fun scheduleUserDeletion(): AppResult<UserPrivate> {
        return mutex.withLock {
            openUserApi.scheduleUserDeletion().mapSuccess { userPrivatePayload: UserPrivatePayload ->
                val userPrivate = userPrivatePayload.toUserPrivate()
                userStorage.updateCurrentUser(userPrivate)
                userPrivate
            }
        }
    }

    override suspend fun restoreUser(): AppResult<UserPrivate> {
        return mutex.withLock {
            openUserApi.restoreUser().mapSuccess { userPrivatePayload: UserPrivatePayload ->
                val userPrivate = userPrivatePayload.toUserPrivate()
                userStorage.updateCurrentUser(userPrivate)
                userPrivate
            }
        }
    }

    override suspend fun clearSession() {
        userStorage.clear()
        authStorage.clearTokens()
    }

    private suspend fun handleUserUpdated(payload: JsonElement?): WebSocketMessageHandlerResult {
        if (payload == null) {
            return WebSocketMessageHandlerResult.Error(CommonError.ContractViolation())
        }

        val userPrivatePayload = try {
            FoundationJson.decodeFromJsonElement<UserPrivatePayload>(payload)
        } catch (e: Exception) {
            return WebSocketMessageHandlerResult.Error(CommonError.ContractViolation(e))
        }

        userStorage.updateCurrentUser(userPrivatePayload.toUserPrivate())

        return WebSocketMessageHandlerResult.Handled
    }

    private suspend fun handleSessionDeleted() {
        Logger.i { "Received SESSION_DELETED: clearing local user and auth data" }
        clearSession()
    }
}